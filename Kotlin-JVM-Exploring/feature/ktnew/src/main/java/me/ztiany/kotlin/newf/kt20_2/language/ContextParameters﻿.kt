package me.ztiany.kotlin.newf.kt20_2.language

/**
 * In Kotlin 1.6.20, we introduced context receivers as an Experimental feature. After listening to
 * community feedback, we've decided not to continue with this approach and will take a different
 * direction.
 *
 * In future Kotlin releases, context receivers will be replaced by context parameters. Context
 * parameters are still in the design phase, and you can find the proposal in the KEEP.
 *
 * Since the implementation of context parameters requires significant changes to the compiler,
 * we've decided not to support context receivers and context parameters simultaneously. This
 * decision greatly simplifies the implementation and minimizes the risk of unstable behavior.
 *
 * We understand that context receivers are already being used by a large number of developers.
 * Therefore, we will begin gradually removing support for context receivers. Our migration plan
 * starts with Kotlin 2.0.20, where warnings are issued in your code when context receivers are
 * used with the `-Xcontext-receivers` compiler option.
 */
fun main() {
    println("Context Parameters is coming!")
}