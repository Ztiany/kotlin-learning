package me.ztiany.kotlin.newf.kt17

private interface Bar {
    fun foo() = "foo"
}

// Allow implementation by delegation to an inlined value of an inline class
@JvmInline
private value class BarWrapper(val bar: Bar) : Bar by bar

/**
 * If you want to create a lightweight wrapper for a value or class instance, it's necessary to implement all interface methods by hand.
 * Implementation by delegation solves this issue, but it did not work with inline classes before 1.7.0. This restriction has been removed,
 * so you can now create lightweight wrappers that do not allocate memory in most cases.
 */
fun main() {
    //BarWrapper 将会被内联，不会产生这个类的对象。
    val bw = BarWrapper(object : Bar {})
    println(bw.foo())
}