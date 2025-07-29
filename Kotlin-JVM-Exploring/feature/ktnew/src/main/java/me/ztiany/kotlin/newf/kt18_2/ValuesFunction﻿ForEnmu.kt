package me.ztiany.kotlin.newf.kt18_2

private enum class Color(val colorName: String, val rgb: String) {
    RED("Red", "#FF0000"),
    ORANGE("Orange", "#FF7F00"),
    YELLOW("Yellow", "#FFFF00")
}

@OptIn(ExperimentalStdlibApi::class)
private fun findByRgb(rgb: String): Color? = Color.entries.find { it.rgb == rgb }

fun main() {
    println(findByRgb("#FF0000"))
}