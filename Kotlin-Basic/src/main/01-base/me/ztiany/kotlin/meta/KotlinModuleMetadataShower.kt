package me.ztiany.kotlin.meta

import kotlinx.metadata.jvm.KotlinModuleMetadata
import org.jetbrains.kotlin.kotlinp.KotlinpSettings
import org.jetbrains.kotlin.kotlinp.ModuleFilePrinter
import java.io.File

fun main() {
    val settings = KotlinpSettings(true)
    KotlinModuleMetadata.read(
        File("build/classes/kotlin/main/META-INF/Kotlin-Basic.kotlin_module")
            .readBytes())?.let {
            println(ModuleFilePrinter(settings).print(it))
        }
}