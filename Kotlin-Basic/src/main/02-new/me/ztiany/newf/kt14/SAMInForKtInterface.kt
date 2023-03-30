package me.ztiany.newf.kt14

/**
 * Before Kotlin 1.4.0, you could apply SAM (Single Abstract Method) conversions only when working with Java methods and Java interfaces from Kotlin.
 * From now on, you can use SAM conversions for Kotlin interfaces as well. To do so, mark a Kotlin interface explicitly as functional with the fun modifier.
 */
fun main() {
    println("Is 7 even? - ${isEven.accept(7)}")
}

private fun interface IntPredicate {
    fun accept(i: Int): Boolean
}

private val isEven = IntPredicate { it % 2 == 0 }
