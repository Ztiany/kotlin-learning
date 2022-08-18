package core.flow

import kotlinx.coroutines.flow.*

/**
 *exceptions handling.
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 2020/9/30 15:39
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

private suspend fun tryCatchFlow() {
    try {
        simpleInt().collect { value ->
            println(value)
            check(value <= 1) { "Collected $value" }
        }
    } catch (e: Throwable) {
        println("Caught $e")
    }
}

private suspend fun catchFlow() {
    //The emitter can use a catch operator that preserves this exception transparency and allows encapsulation of its exception handling.
    // The catch intermediate operator, honoring exception transparency, catches only upstream exceptions (that is an exception from all the operators above catch, but not below it).
    simpleStr()
            .catch { e -> emit("Caught $e") } // emit on exception
            .collect { value ->
                println(value)
            }
}

private suspend fun catchAllFlow() {
    // By moving the body of the collect operator into onEach and putting it before the catch operator.
    // We can combine the declarative nature of the catch operator with a desire to handle all the exceptions.
    simpleInt()
            .onEach { value ->
                check(value <= 1) { "Collected $value" }
                println(value)
            }
            .catch { e -> println("Caught $e") }
            .collect()
}
