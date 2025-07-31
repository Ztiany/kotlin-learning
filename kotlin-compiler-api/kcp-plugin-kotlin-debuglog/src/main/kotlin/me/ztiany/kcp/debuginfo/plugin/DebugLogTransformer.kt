package me.ztiany.kcp.debuginfo.plugin

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.FirIncompatiblePluginAPI
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irCatch
import org.jetbrains.kotlin.backend.common.lower.irThrow
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.buildVariable
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrTryImpl
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class DebugLogTransformer(
    //An IrPluginContext provides access to the IR code and the compiler environment.
    private val pluginContext: IrPluginContext,
    //An IrClassSymbol for the annotation use to mark functions for debug logging,
    private val annotationClass: IrClassSymbol,
    //An IrSimpleFunctionSymbol for the function used to log debug messages.
    private val logFunction: IrSimpleFunctionSymbol,
) : IrElementTransformerVoidWithContext() {

    private val typeUnit = pluginContext.irBuiltIns.unitType

    private val typeThrowable = pluginContext.irBuiltIns.throwableType

    @OptIn(FirIncompatiblePluginAPI::class)
    private val classMonotonic = pluginContext.referenceClass(FqName("kotlin.time.TimeSource.Monotonic"))!!

    @OptIn(FirIncompatiblePluginAPI::class)
    private val funMarkNow = pluginContext.referenceFunctions(FqName("kotlin.time.TimeSource.markNow")).single()

    @OptIn(FirIncompatiblePluginAPI::class)
    private val funElapsedNow = pluginContext.referenceFunctions(FqName("kotlin.time.TimeMark.elapsedNow")).single()

    /**
     * we can override the visitFunctionNew function to intercept transformation of function statements.
     * This visit function is specific to IrElementTransformerVoidWithContext as IrFunction is an element
     * which is added to the context stack the abstract transformer maintains.
     */
    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        // A function body can be null. So to see if we should transform an intercepted function we need to check if
        // it has a body and has the correct annotation applied. For the annotation check, we can use the extension
        // function hasAnnotation.
        val body = declaration.body ?: return super.visitFunctionNew(declaration)
        if (declaration.hasAnnotation(annotationClass)) {
            declaration.body = irDebug(declaration, body)
        }
        return super.visitFunctionNew(declaration)
    }

    /**
     * from:
     *
     * ```
     * @DebugLog
     * fun greet(greeting: String = "Hello", name: String = "World"): String {
     *   return "${'$'}greeting, ${'$'}name!"
     * }
     * ```
     *
     * to:
     *
     * ```
     * @DebugLog
     * fun greet(greeting: String = "Hello", name: String = "World"): String {
     *   println("⇢ greet(greeting=$greeting, name=$name)")
     *   val startTime = TimeSource.Monotonic.markNow()
     *   try {
     *     val result = "${'$'}greeting, ${'$'}name!"
     *     println("⇠ greet [${startTime.elapsedNow()}] = $result")
     *     return result
     *   } catch (t: Throwable) {
     *     println("⇠ greet [${startTime.elapsedNow()}] = $t")
     *     throw t
     *   }
     * }
     * ```
     */
    private fun irDebug(
        function: IrFunction,
        body: IrBody
    ): IrBlockBody {
        return DeclarationIrBuilder(pluginContext, function.symbol).irBlockBody {
            //添加一个 println，打印函数名和参数
            +irDebugEnter(function)

            //To create a temporary local variable you can use the irTemporary builder.
            val startTime = irTemporary(irCall(funMarkNow).also { call ->
                call.dispatchReceiver = irGetObject(classMonotonic)
            })

            //构建一个 try 块
            val tryBlock = irBlock(resultType = function.returnType) {
                for (statement in body.statements) {
                    +statement
                }
                if (function.returnType == typeUnit) {
                    +irDebugExit(function, startTime)
                }
            }.transform(DebugLogReturnTransformer(function, startTime), null)

            /*
                To build a try-catch statement, there unfortunately is no builder function so we need to construct the
                implementation class directly.

                Since a try block is an expression in Kotlin, we need to provide a result type for the IrTry. We also need
                to build a variable for each catch expression to reference the caught throwable inside a irCatch builder.

                All of this will be built within a IrBuilderWithScope which provides scope, startOffset, and endOffset
                as class properties.
             */
            val throwable = buildVariable(
                scope.getLocalDeclarationParent(),
                startOffset,
                endOffset,
                IrDeclarationOrigin.CATCH_PARAMETER,
                Name.identifier("t"),
                typeThrowable
            )

            +IrTryImpl(startOffset, endOffset, tryBlock.type).also { irTry ->
                irTry.tryResult = tryBlock

                irTry.catches += irCatch(throwable, irBlock {
                    +irDebugExit(function, startTime, irGet(throwable))
                    +irThrow(irGet(throwable))
                })
            }
        }
    }

    /**
     * This secondary transformer is used to convert return statements so the result can be logged before exiting the function.
     *
     * ```
     * val result = "${'$'}greeting, ${'$'}name!"
     * println("⇠ greet [${startTime.elapsedNow()}] = $result")
     * return result
     * ```
     * To replicate the above transformation, we can intercept IrReturn elements with a secondary transformer.
     */
    inner class DebugLogReturnTransformer(
        private val function: IrFunction,
        private val startTime: IrVariable
    ) : IrElementTransformerVoidWithContext() {

        //If the function has no return expression, then method visitReturn will not be called.
        override fun visitReturn(expression: IrReturn): IrExpression {
            /*
                We should only transform IrReturn expressions that return from the annotated function.
                This can be checked by looking at the returnTargetSymbol property of the IrReturn element.

                This is done to ensure that the return statement being visited belongs to the current function,
                and not to a nested function or lambda expression.

                returnTargetSymbol means the function that the return statement is returning from.
             */
            if (expression.returnTargetSymbol != function.symbol) {
                return super.visitReturn(expression)
            }

            return DeclarationIrBuilder(pluginContext, function.symbol).irBlock {
                val result = irTemporary(expression.value)
                +irDebugExit(function, startTime, irGet(result))
                +expression.apply {
                    value = irGet(result)
                }
            }
        }

    }

    private fun IrBuilderWithScope.irDebugEnter(
        function: IrFunction
    ): IrCall {
        val concat = irConcat()
        concat.addArgument(irString("⇢ ${function.name}("))
        for ((index, valueParameter) in function.valueParameters.withIndex()) {
            if (index > 0) concat.addArgument(irString(", "))
            concat.addArgument(irString("${valueParameter.name}="))
            concat.addArgument(irGet(valueParameter))
        }
        concat.addArgument(irString(")"))

        return irCall(logFunction).also { call ->
            call.putValueArgument(0, concat)
        }
    }

    private fun IrBuilderWithScope.irDebugExit(
        function: IrFunction,
        startTime: IrValueDeclaration,
        result: IrExpression? = null
    ): IrCall {
        val concat = irConcat()
        concat.addArgument(irString("⇠ ${function.name} ["))
        concat.addArgument(irCall(funElapsedNow).also { call ->
            call.dispatchReceiver = irGet(startTime)
        })

        if (result != null) {
            concat.addArgument(irString("] = "))
            concat.addArgument(result)
        } else {
            concat.addArgument(irString("]"))
        }

        return irCall(logFunction).also { call ->
            call.putValueArgument(0, concat)
        }
    }

}