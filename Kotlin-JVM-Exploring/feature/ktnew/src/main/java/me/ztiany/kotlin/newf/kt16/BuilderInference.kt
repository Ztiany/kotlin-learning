package me.ztiany.kotlin.newf.kt16

fun main() {
    val list = buildList {
        add("a")
        set(1, null)
    }
}