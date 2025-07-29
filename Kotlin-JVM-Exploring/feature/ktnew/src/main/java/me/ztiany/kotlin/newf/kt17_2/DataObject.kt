package me.ztiany.kotlin.newf.kt17_2

sealed class ReadResult {
    data class Number(val value: Int) : ReadResult()
    data class Text(val value: String) : ReadResult()
    data object EndOfFile : ReadResult()
}

/**
 * This release introduces a new type of object declaration for you to use: data object. Data object
 * behaves conceptually identical to a regular object declaration but comes with a clean toString
 * representation out of the box.
 */
fun main() {
    println(ReadResult.Number(1)) // Number(value=1)
    println(ReadResult.Text("Foo")) // Text(value=Foo)
    println(ReadResult.EndOfFile) // EndOfFile
}