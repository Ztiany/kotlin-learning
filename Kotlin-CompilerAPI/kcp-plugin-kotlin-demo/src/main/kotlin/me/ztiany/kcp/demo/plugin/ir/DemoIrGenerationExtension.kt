package me.ztiany.kcp.demo.plugin.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.dump

/**
 * An instance of IrGenerationExtension can be created and registered within a ComponentRegistrar.
 * This instance will be called during compilation when IR code needs to be generated (or transformed).
 * The entry point for this extension provides an IrModuleFragment and IrPluginContext which provide
 * everything we need to navigate and transform the module IR code.
 */
class DemoIrGenerationExtension(
    private val messageCollector: MessageCollector,
    private val string: String,
    private val file: String
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        println(moduleFragment.dump())
        messageCollector.report(CompilerMessageSeverity.INFO, "Argument 'string' = $string")
        messageCollector.report(CompilerMessageSeverity.INFO, "Argument 'file' = $file")
    }

}