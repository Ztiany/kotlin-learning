package core.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 *Launching flows in a new coroutine.
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 2020/9/30 15:39
 */
fun main() = runBlocking {
    //executeFlow()
    launchFlow(this)
}

private fun simpleInt(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i) // emit next value
    }
}

private suspend fun executeFlow() {
    //If we use the collect terminal operator after onEach, then the code after it will wait until the flow is collected:
    simpleInt()
            .onEach { event -> println("Event: $event") }
            .collect() // <--- Collecting the flow waits

    println("Done")
}

private suspend fun launchFlow(scope: CoroutineScope) {
    //By replacing collect with launchIn we can launch a collection of the flow in a separate coroutine, so that execution of further code immediately continues.
    simpleInt()
            .onEach { event -> println("Event: $event") }
            .launchIn(scope) // <--- Collecting the flow waits

    println("Done")
}
