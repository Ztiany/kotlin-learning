package me.ztiany.kotlin.newf.kt21.language

private fun processList(elements: List<String>): Boolean {
    for (element in elements) {
        val variable = element.toBigDecimalOrNull() ?: run {
            println("Element is null or invalid, continuing...")
            continue
        }
        if (variable.intValueExact() == 0) return true // If variable is zero, return true
    }
    return false
}

/**
 * Kotlin 2.1.0 adds a preview of another long-awaited feature, the ability to use non-local break
 * and continue. This feature expands the toolset you can use in the scope of inline functions and
 * reduces boilerplate code in your project.
 *
 * Previously, you could use only non-local returns. Now, Kotlin also supports break and continue
 * jump expressions non-locally. This means that you can apply them within lambdas passed as
 * arguments to an inline function that encloses the loop:
 */
fun main() {
    processList(listOf("1", "2", "3", "a", "b", "c"))
}