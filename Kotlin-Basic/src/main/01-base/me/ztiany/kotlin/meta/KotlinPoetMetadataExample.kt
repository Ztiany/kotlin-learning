package me.ztiany.kotlin.meta

import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.toKmClass

private data class Taco(val seasoning: String, val soft: Boolean) {

    fun prepare() {

    }

}

@OptIn(KotlinPoetMetadataPreview::class)
fun main() {
    val kmClass = Taco::class.toKmClass()

    // Now you can access misc information about Taco from a Kotlin lens
    println("--------------Class name: ${kmClass.name}")
    println(kmClass.name)
    println("--------------Class properties:")
    kmClass.properties.forEach { println(it.name) }
    println("--------------Class functions:")
    kmClass.functions.forEach { println(it.name) }
}