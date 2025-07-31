package official.core.flow

import common.printAligned
import kotlinx.coroutines.flow.*

/**
 * When flow collection completes (normally or exceptionally) it may need to execute an action.
 * As you may have already noticed, it can be done in two ways: imperative or declarative.
 */
suspend fun main() {
    imperatively()
    //declarative()
    declarativeWithCatch()
}

private fun simpleInt(): Flow<Int> = flow {
    for (i in 1..6) {
        println("Emitting $i")
        emit(i) // emit next value
    }
}.onEach {
    check(it <= 1) { "Collected $it" }
}

/**
 * In addition to try/catch, a collector can also use a finally block to execute an action upon collect completion.
 *
 * Note: 会中断协程，因为没有使用协程内置的错误处理机制。
 */
private suspend fun imperatively() {
    "imperatively".printAligned()
    try {
        simpleInt().collect {
            println("received $it")
        }
    } catch (e: Exception) {
        println("imperatively: $e")
    } finally {
        println("imperatively: Done")
    }
}

/**
 * For the declarative approach, flow has onCompletion intermediate operator that is invoked when the flow has completely
 * collected.
 *
 * The previous example can be rewritten using an onCompletion operator and produces the same output.
 *
 * - The key advantage of onCompletion is a nullable Throwable parameter of the lambda that can be used to determine whether
 * the flow collection was completed normally or exceptionally. In the following example the simple flow throws an exception
 * after emitting the number 1.
 * - Another difference with catch operator is that onCompletion sees all exceptions and receives a null exception only on
 * successful completion of the upstream flow (without cancellation or failure).
 */
private suspend fun declarative() {
    "onCompletion".printAligned()
    flow {
        emit(1)
        throw RuntimeException()
    }
        .onCompletion {
            if (it == null) {
                println("declarative done successfully")
            } else {
                println("declarative done exceptionally: $it")
            }
        }
        .collect {
            println(it)
        }
}

/**

 */
private suspend fun declarativeWithCatch() {
    "onCompletion catch".printAligned()
    simpleInt()
        .onCompletion { cause ->
            println("declarativeWithCatch: Flow completed exceptionally with $cause")
        }
        .catch {
            println("Caught exception: $it")
        }
        .collect { value ->
            println(value)
        }
}

/*
 which way to use? Imperative versus declarative.

 Now we know how to collect flow, and handle its completion and exceptions in both imperative and declarative ways.
 The natural question here is, which approach is preferred and why? As a library, we do not advocate for any particular
 approach and believe that both options are valid and should be selected according to your own preferences and code style.
 */