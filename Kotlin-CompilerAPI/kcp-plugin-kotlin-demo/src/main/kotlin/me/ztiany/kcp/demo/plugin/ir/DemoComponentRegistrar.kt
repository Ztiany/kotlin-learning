package me.ztiany.kcp.demo.plugin.ir

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration


/**
 * A Kotlin compiler plugin starts with a ComponentRegistrar and CommandLineProcessor implementation.
 * Both of these classes are loaded by the Kotlin compiler using a ServiceLoader so we will use the
 * auto-service annotation processor to automatically generate the required files.
 *
 * The ComponentRegistrar is where the actual work of a compiler plugin actually begins. The registrar is
 * responsible for registering extension components with the project being compiled.
 *
 * There are a number of extension points currently available in the Kotlin compiler, but the one we will
 * be focusing on is IrGenerationExtension, which allows for navigating and transforming Kotlin IR.
 */
@OptIn(ExperimentalCompilerApi::class)
@AutoService(ComponentRegistrar::class)
class DemoComponentRegistrar(
    private val defaultString: String,
    private val defaultFile: String,
) : ComponentRegistrar {

    @Suppress("unused") // Used by service loader
    constructor() : this(
        defaultString = "Hello, World!", defaultFile = "file.txt"
    )

    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration
    ) {
        val messageCollector = configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)

        val string = configuration.get(DemoCommandLineProcessor.ARG_STRING, defaultString)
        val file = configuration.get(DemoCommandLineProcessor.ARG_FILE, defaultFile)

        IrGenerationExtension.registerExtension(project, DemoIrGenerationExtension(messageCollector, string, file))
    }

}