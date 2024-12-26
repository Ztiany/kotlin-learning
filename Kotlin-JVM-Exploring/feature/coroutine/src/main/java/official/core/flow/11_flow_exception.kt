package official.core.flow

import common.printAligned
import kotlinx.coroutines.flow.*

/**
 * Flow collection can complete with an exception when an emitter or code inside the operators throw an exception.
 * There are several ways to handle these exceptions.
 */
suspend fun main() {
    tryCatchFlow()
    catchFlow()
    catchAllFlow()
}

private fun simpleInt(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i) // emit next value
    }
}

private fun simpleStr() = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit("Emitting $i") // emit next value
    }
}

/**
 * **Collector try and catch**: A collector can use Kotlin's try/catch block to handle exceptions:
 *
 * **Everything is caught**: The example actually catches any exception happening in the emitter or in any
 * intermediate or terminal operators.
 */
private suspend fun tryCatchFlow() {
    "try catch".printAligned()
    try {
        simpleInt().collect { value ->
            println(value)
            check(value <= 1) { "Collected $value" }
        }
    } catch (e: Throwable) {
        println("Caught $e")
    }
}

/**
 * The emitter can use a catch operator that preserves this exception transparency and allows encapsulation of its exception handling.
 * The body of the catch operator can analyze an exception and react to it in different ways depending on which exception was caught:
 *
 *  1. Exceptions can be rethrown using throw.
 *  2. Exceptions can be turned into emission of values using emit from the body of catch.
 *  3. Exceptions can be ignored, logged, or processed by some other code.
 *
 *  The catch intermediate operator, honoring exception transparency, catches only upstream exceptions (that is an exception from
 *  all the operators above catch, but not below it). If the block in collect { ... } (placed below catch) throws an exception then it escapes:
 */
private suspend fun catchFlow() {
    "catch-operator".printAligned()
    simpleStr()
        .catch { e -> emit("Caught $e") } // emit on exception
        .collect { value ->
            println(value)
        }
}

/**
 * We can combine the declarative nature of the catch operator with a desire to handle all the exceptions, by moving the
 * body of the collect operator into onEach and putting it before the catch operator. Collection of this flow must be
 * triggered by a call to collect() without parameters:
 */
private suspend fun catchAllFlow() {
    "catch-all".printAligned()
    simpleInt()
        .onEach { value ->
            check(value <= 1) { "Collected $value" }
            println(value)
        }
        .catch { e -> println("Caught $e") }
        .collect()
}
