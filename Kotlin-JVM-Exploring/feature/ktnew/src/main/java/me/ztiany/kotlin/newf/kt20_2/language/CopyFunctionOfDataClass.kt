package me.ztiany.kotlin.newf.kt20_2.language

// Triggers a warning in 2.0.20
private data class PositiveInteger private constructor(val number: Int) {
    companion object {
        fun create(number: Int): PositiveInteger? =
            if (number > 0) PositiveInteger(number) else null
    }
}

/**
 * Currently, if you create a data class using a private constructor, the automatically generated
 * copy() function doesn't have the same visibility. This can cause problems later in your code.
 * In future Kotlin releases, we will introduce the behavior that the default visibility of the
 * copy() function is the same as the constructor. This change will be introduced gradually to help
 * you migrate your code as smoothly as possible.
 *
 * Our migration plan starts with Kotlin 2.0.20, which issues warnings in your code where the
 * visibility will change in the future. For example:
 */
fun main() {
    val positiveNumber = PositiveInteger.create(42) ?: return
    // Triggers a warning in 2.0.20
    val negativeNumber = positiveNumber.copy(number = -1)
    // Warning: Non-public primary constructor is exposed via the generated 'copy()' method of
    // the 'data' class.
    // The generated 'copy()' will change its visibility in future releases.
}