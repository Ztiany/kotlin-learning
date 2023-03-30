package core.flow

import kotlinx.coroutines.flow.*

/**
 * handle completion.
 */
suspend fun main() {
    //imperatively()//会中断协程【因为没有使用协程内置的错误处理机制】
    //declarative()//会中断协程【因为没有处理错误】

    declarativeWithCatch()//不会中断协程
}

private fun simpleInt(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i) // emit next value
    }
}.onEach {
    check(it <= 1) { "Collected $it" }
}

private suspend fun imperatively() {
    try {
        simpleInt().collect {
            println(it)
        }
    } finally {
        println("imperatively: Done")
    }
}

private suspend fun declarative() {
    simpleInt()
            .onCompletion {
                println("declarative: Done")
            }
            .collect {
                println(it)
            }
}

private suspend fun declarativeWithCatch() {
    simpleInt()
            .onCompletion { cause ->
                println("declarativeWithCatch: Flow completed exceptionally with $cause")
            }
            .catch {
                println("Caught exception")
            }
            .collect { value ->
                println(value)
            }
}
