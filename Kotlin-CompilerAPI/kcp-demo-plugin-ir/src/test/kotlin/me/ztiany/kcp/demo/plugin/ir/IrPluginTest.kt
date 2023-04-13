package me.ztiany.kcp.demo.plugin.ir

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import kotlin.test.assertEquals
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Test

private const val code = """

    fun main() {
        println(debug())
    }
    
    fun debug() = "Hello, World!"
    
    fun test(condition:Boolean){
            if(condition){
                println("Good")
            }else{
                println("Bad")
            }
    }

"""

/**
 * Testing Kotlin/JVM compiler plugins is really easy thanks to the kotlin-compile-testing library!
 * This library allows you to compile Kotlin source strings with your ComponentRegistrar in tests which makes debugging easy.
 * The resulting compiled files can even be loaded via a ClassLoader if you want to execute them which we will discuss later.
 */
class IrPluginTest {

    @OptIn(ExperimentalCompilerApi::class)
    @Test
    fun `IR plugin success`() {

        val result = compile(
            sourceFile = SourceFile.kotlin("main.kt", code)
        )

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

}

@OptIn(ExperimentalCompilerApi::class)
fun compile(
    sourceFiles: List<SourceFile>,
    plugin: ComponentRegistrar = TemplateComponentRegistrar(),
): KotlinCompilation.Result {
    return KotlinCompilation().apply {
        sources = sourceFiles
        useIR = true
        componentRegistrars = listOf(plugin)
        inheritClassPath = true
    }.compile()
}

@OptIn(ExperimentalCompilerApi::class)
fun compile(
    sourceFile: SourceFile,
    plugin: ComponentRegistrar = TemplateComponentRegistrar(),
): KotlinCompilation.Result {
    return compile(listOf(sourceFile), plugin)
}