package me.ztiany.ksp.processors

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import me.ztiany.ksp.annotaions.IntSummable

class IntSummableProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    private lateinit var intType: KSType

    override fun process(resolver: Resolver): List<KSAnnotated> {
        intType = resolver.builtIns.intType
        val symbols = resolver.getSymbolsWithAnnotation(IntSummable::class.qualifiedName!!).filterNot { it.validate() }

        symbols.filter { it is KSClassDeclaration && it.validate() }
            .forEach {
                it.accept(
                    IntSummableVisitor(
                        environment.logger,
                        environment.codeGenerator,
                        intType
                    ),
                    Unit
                )
            }

        return symbols.toList()
    }

}