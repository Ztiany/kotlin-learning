package me.ztiany.kotlin.newf.kt19_2.stdlib

import kotlin.enums.enumEntries

private enum class RGB { RED, GREEN, BLUE }

@OptIn(ExperimentalStdlibApi::class)
private inline fun <reified T : Enum<T>> printAllValues() {
    print(enumEntries<T>().joinToString { it.name })
}

fun main() {
    printAllValues<RGB>()
}