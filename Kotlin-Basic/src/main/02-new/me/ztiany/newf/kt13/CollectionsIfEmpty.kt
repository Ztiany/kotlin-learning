package me.ztiany.newf.kt13

/**
 * Collections, maps, object arrays, char sequences, and sequences now have an ifEmpty function,
 * which allows specifying a fallback value that will be used instead of the receiver if it is empty:
 */
fun main() {
    printAllUppercase(listOf("foo", "Bar"))
    printAllUppercase(listOf("FOO", "BAR"))

    val s = "    \n"
    println(s.ifBlank { "<blank>" })
    println(s.ifBlank { null })
}

fun printAllUppercase(data: List<String>) {
    val result = data
        .filter { it.all { c -> c.isUpperCase() } }
        .ifEmpty { listOf("<no uppercase>") }

    result.forEach { println(it) }
}