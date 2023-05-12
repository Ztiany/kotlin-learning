package me.ztiany.kotlin.newf.kt13

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

private val lock = Any()

fun main() {

}

//1: Improving smartcasts analysis by declaring the relation between a function's call outcome and the passed arguments values:
@OptIn(ExperimentalContracts::class)
private fun requireTrue(condition: Boolean) {
    // This is a syntax form which tells the compiler:
    // "if this function returns successfully, then the passed 'condition' is true"
    contract { returns() implies condition }
    if (!condition) throw IllegalArgumentException()
}

private fun testRequire(s: String?) {
    requireTrue(s is String)
    // s is smartcast to 'String' here, because otherwise
    // 'require' would have thrown an exception
    println("s.length: ${s.length}")
}

@OptIn(ExperimentalContracts::class)
private fun String?.customIsNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@customIsNullOrEmpty != null)
    }
    return this == null || isEmpty()
}

//2: Improving the variable initialization analysis in the presence of high-order functions:
private fun testSynchronize() {
    val x: Int
    synchronize(lock) {
        x = 42 // Compiler knows that lambda passed to 'synchronize' is called
        // exactly once, so no reassignment is reported
    }
    println(x) // Compiler knows that lambda will be definitely called, performing
    // initialization, so 'x' is considered to be initialized here
}

@OptIn(ExperimentalContracts::class)
private fun synchronize(lock: Any?, block: () -> Unit) {
    // It tells the compiler:
    // "This function will invoke 'block' here and now, and exactly one time"
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
}
