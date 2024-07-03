package official.core.flow

import common.printAligned
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Launching flow.
 */
fun main() = runBlocking {
    executeFlow()
    launchFlow(this).join()
    naturallyCancellableFlow()
    cancellableFlow()
}

private fun simpleInt(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i) // emit next value
    }
}

/**
 * It is easy to use flows to represent asynchronous events that are coming from some source.
 * In this case, we need an analogue of the addEventListener function that registers a piece of
 * code with a reaction for incoming events and continues further work. The onEach operator
 * can serve this role. However, onEach is an intermediate operator. We also need a terminal
 * operator to collect the flow. Otherwise, just calling onEach has no effect.
 */
private suspend fun executeFlow() {
    "onEach".printAligned()
    // If we use the collect terminal operator after onEach, then the code after it will wait until the flow is collected:
    simpleInt()
        .onEach { event -> println("Event: $event") }
        .collect() // <--- Collecting the flow waits

    println("Done")
}

/**
 * The launchIn terminal operator comes in handy here. By replacing collect with launchIn we can launch
 * a collection of the flow in a separate coroutine, so that execution of further code immediately continues:
 *
 * The required parameter to launchIn must specify a CoroutineScope in which the coroutine to collect the
 * flow is launched. In the above example this scope comes from the runBlocking coroutine builder, so while
 * the flow is running, this runBlocking scope waits for completion of its child coroutine and keeps the main
 * function from returning and terminating this example.
 *
 * In actual applications a scope will come from an entity with a limited lifetime. As soon as the lifetime of
 * this entity is terminated the corresponding scope is cancelled, cancelling the collection of the corresponding
 * flow. This way the pair of onEach { ... }.launchIn(scope) works like the addEventListener. However, there is no
 * need for the corresponding removeEventListener function, as cancellation and structured concurrency
 * serve this purpose.
 *
 * Note that launchIn also returns a Job, which can be used to cancel the corresponding flow collection
 * coroutine only without cancelling the whole scope or to join it.
 */
private fun launchFlow(scope: CoroutineScope): Job {
    "launchIn".printAligned()
    // By replacing collect with launchIn we can launch a collection of the flow in a separate coroutine, so that execution of further code immediately continues.
    return simpleInt()
        .onEach { event -> println("Event: $event") }
        .launchIn(scope) // <--- Collecting the flow waits
}

private fun sampleForCancellable(): Flow<Int> = flow {
    for (i in 1..5) {
        println("Emitting $i")
        emit(i)
    }
}

/**
 * For convenience, the flow builder performs additional ensureActive checks for cancellation on each emitted value.
 * It means that a busy loop emitting from a flow { ... } is cancellable:
 *
 */
context(CoroutineScope)
private suspend fun naturallyCancellableFlow() {
    "cancellable".printAligned()
    sampleForCancellable()
        .onEach { value ->
            println(value)
            if (value == 3) {
                cancel()
            }
        }
        .collect()
}

/**
 * However, most other flow operators do not do additional cancellation checks on their own for performance reasons. For example,
 * if you use IntRange.asFlow extension to write the same busy loop and don't suspend anywhere, then there are no checks for cancellation.
 *
 * In the case where you have a busy loop with coroutines you must explicitly check for cancellation. You can add .onEach
 * { currentCoroutineContext().ensureActive() }, but there is a ready-to-use cancellable operator provided to do that:
 */
context(CoroutineScope)
private suspend fun cancellableFlow() {
    (1..5).asFlow().cancellable().collect { value ->
        if (value == 3) cancel()
        println(value)
    }
}