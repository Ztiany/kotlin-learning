package me.ztiany.kcp.demo.plugin.ir

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

/**
 * A Kotlin compiler plugin starts with a ComponentRegistrar and CommandLineProcessor implementation.
 * Both of these classes are loaded by the Kotlin compiler using a ServiceLoader so we will use the
 * auto-service annotation processor to automatically generate the required files.
 *
 * The CommandLineProcessor is pretty easy to understand: It defines the Kotlin compiler plugin ID string (same as in Gradle plugin)
 * and expected command line options. The processor is also responsible for parsing the command line options of the plugin and placing
 * them in a CompilerConfiguration. It should read and process the same values defined by the Gradle plugin.
 */
@OptIn(ExperimentalCompilerApi::class)
@AutoService(CommandLineProcessor::class)
class DemoCommandLineProcessor : CommandLineProcessor {

    override val pluginId = DEMO_KOTLIN_PLUGIN_ID

    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption(
            optionName = OPTION_STRING,
            valueDescription = "string",
            description = "sample string argument",
            required = false,
        ),
        CliOption(
            optionName = OPTION_FILE,
            valueDescription = "file",
            description = "sample file argument",
            required = false,
        ),
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option.optionName) {
            OPTION_STRING -> configuration.put(ARG_STRING, value)
            OPTION_FILE -> configuration.put(ARG_FILE, value)
            else -> throw IllegalArgumentException("Unexpected config option ${option.optionName}")
        }
    }

    companion object {
        private const val OPTION_STRING = "string"
        private const val OPTION_FILE = "file"

        val ARG_STRING = CompilerConfigurationKey<String>(OPTION_STRING)
        val ARG_FILE = CompilerConfigurationKey<String>(OPTION_FILE)
    }

}