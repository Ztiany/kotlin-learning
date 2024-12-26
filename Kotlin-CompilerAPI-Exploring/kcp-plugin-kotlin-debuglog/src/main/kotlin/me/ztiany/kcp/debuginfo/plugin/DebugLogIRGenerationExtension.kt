package me.ztiany.kcp.debuginfo.plugin

import org.jetbrains.kotlin.backend.common.extensions.FirIncompatiblePluginAPI
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.name.FqName

class DebugLogIRGenerationExtension(
    private val messageCollector: MessageCollector
) : IrGenerationExtension {

    @OptIn(FirIncompatiblePluginAPI::class)
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        messageCollector.report(CompilerMessageSeverity.INFO, "DebugInfoIRGenerationExtension: generating started.")

        println("------------------------------before transform:")
        println(moduleFragment.dump())

        val debugLogAnnotation = pluginContext.referenceClass(FqName("me.ztiany.kcp.annotations.debuglog.DebugLog"))
        if (debugLogAnnotation == null) {
            messageCollector.report(
                CompilerMessageSeverity.WARNING,
                "DebugInfoIRGenerationExtension: DebugLog annotation not found."
            )
            return
        }

        val typeAnyNullable = pluginContext.irBuiltIns.anyNType

        val funPrintln = pluginContext.referenceFunctions(FqName("kotlin.io.println"))
            .single {
                val parameters = it.owner.valueParameters
                parameters.size == 1 && parameters[0].type == typeAnyNullable
            }

        println("------------------------------after transform:")
        val transformed =
            moduleFragment.transform(DebugLogTransformer(pluginContext, debugLogAnnotation, funPrintln), null)
        println(transformed.dump())

        messageCollector.report(CompilerMessageSeverity.INFO, "DebugInfoIRGenerationExtension: generating ended.")
    }

}