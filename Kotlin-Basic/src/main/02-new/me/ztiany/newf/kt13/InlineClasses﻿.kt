package me.ztiany.newf.kt13

fun main() {
    // In the next line no constructor call happens, and
    // at the runtime 'name' contains just string "Kotlin"
    val name = Name("Kotlin")
    println(name.s)
}

// 这是 Kotlin 1.3 时的语法，现在已经废弃了。
//private inline class Name(val s: String)

/**
 inline classes must have exactly one property:
 */
@JvmInline
private value class Name(val s: String)
