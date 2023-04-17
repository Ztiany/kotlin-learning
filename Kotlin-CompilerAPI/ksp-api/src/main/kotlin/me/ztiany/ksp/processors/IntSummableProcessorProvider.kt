package me.ztiany.ksp.processors

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class IntSummableProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        environment.logger.info("IntSummableProcessorProvider.create()")
        return IntSummableProcessor(environment)
    }

}