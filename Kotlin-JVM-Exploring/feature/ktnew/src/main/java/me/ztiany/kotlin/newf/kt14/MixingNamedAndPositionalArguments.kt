package me.ztiany.kotlin.newf.kt14


private fun reformat(
    str: String,
    uppercaseFirstLetter: Boolean = true,
    wordSeparator: Char = ' '
) {
    // ...
}

/**
 *In Kotlin 1.3, when you called a function with named arguments, you had to place all the arguments without names (positional arguments) before the first named argument.
 * For example, you could call f(1, y = 2), but you couldn't call f(x = 1, 2).
 *
 * In Kotlin 1.4, there is no such limitation – you can now specify a name for an argument in the middle of a set of positional arguments. Moreover,
 * you can mix positional and named arguments any way you like, as long as they remain in the correct order.
 */
fun main() {
    //Function call with a named argument in the middle.【按照参数的位置按顺序进行传递】
    reformat("This is a String!", uppercaseFirstLetter = false, '-')
}
