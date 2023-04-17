package me.ztiany.kcp.debuginfo.plugin

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CommandLineProcessor::class)
class DebugLogCommandLineProcessor : CommandLineProcessor {

    override val pluginId: String = DEBUG_LOG_KOTLIN_PLUGIN_ID

    override val pluginOptions: Collection<CliOption> = listOf(
        CliOption(
            optionName = OPTION_ENABLED,
            valueDescription = "bool <true | false>",
            description = "If the DebugLog annotation should be applied",
            required = false,
        ),
    )


    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration
    ) {
        return when (option.optionName) {
            OPTION_ENABLED -> configuration.put(ARG_ENABLED, value.toBoolean())
            else -> throw IllegalArgumentException("Unexpected config option ${option.optionName}")
        }
    }

    companion object {
        private const val OPTION_ENABLED = "enabled"

        val ARG_ENABLED = CompilerConfigurationKey<Boolean>(OPTION_ENABLED)
    }

}