package me.ztiany.kotlin.newf.kt14


fun main() {
    val colors = listOf(
        "red",
        "green",
        "blue", //trailing comma
    )
}

private fun reformat(
    str: String,
    uppercaseFirstLetter: Boolean = true,
    wordSeparator: Char = 'a', //trailing comma
) {
    // ...
}