package me.ztiany.newf.kt17_2

import sun.jvm.hotspot.oops.CellTypeState.value

/**
 * Our research shows that this new operator does a better job at expressing open-ended ranges and making it clear that the upper bound is not included.
 *
 * Here is an example of using the ..< operator in a when expression:
 */
@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val value = 4.0
    when (value) {
        in 0.0..<0.25 -> {} // First quarter
        in 0.25..<0.5 -> {} // Second quarter
        in 0.5..<0.75 -> {} // Third quarter
        in 0.75..1.0 -> {}  // Last quarter  <- Note closed range here
    }
}