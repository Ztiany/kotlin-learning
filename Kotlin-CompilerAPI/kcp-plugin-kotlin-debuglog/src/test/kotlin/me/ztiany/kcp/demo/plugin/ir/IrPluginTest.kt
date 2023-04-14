package me.ztiany.kcp.demo.plugin.ir

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import me.ztiany.kcp.debuginfo.plugin.DebugLogComponentRegistrar
import kotlin.test.assertEquals
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Test

private val code_test = """
      
      import me.ztiany.kcp.annotations.debuglog.DebugLog
      
      fun main() {
        println(greet())
        println(greet(name = "Kotlin IR"))
      }
      
      @DebugLog
      fun greet(greeting: String = "Hello", name: String = "World"): String { 
        Thread.sleep(15) // simulate work
        return "${'$'}greeting, ${'$'}name!"
      }
      
""".trimIndent()

/**
 * Testing Kotlin/JVM compiler plugins is really easy thanks to the kotlin-compile-testing library!
 * This library allows you to compile Kotlin source strings with your ComponentRegistrar in tests which makes debugging easy.
 * The resulting compiled files can even be loaded via a ClassLoader if you want to execute them which we will discuss later.
 */
class IrPluginTest {

    @OptIn(ExperimentalCompilerApi::class)
    @Test
    fun testIRTransformation() {

        val result = compile(
            sourceFile = SourceFile.kotlin("main.kt", code_test),
            plugin = DebugLogComponentRegistrar()
        )

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

        val kClazz = result.classLoader.loadClass("MainKt")
        val main = kClazz.declaredMethods.single { it.name == "main" && it.parameterCount == 0 }
        main.invoke(null)
    }

}

@OptIn(ExperimentalCompilerApi::class)
fun compile(
    sourceFiles: List<SourceFile>,
    plugin: ComponentRegistrar
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
    plugin: ComponentRegistrar
): KotlinCompilation.Result {
    return compile(listOf(sourceFile), plugin)
}