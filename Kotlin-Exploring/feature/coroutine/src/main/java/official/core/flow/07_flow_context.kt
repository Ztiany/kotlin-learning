package official.core.flow

import common.logCoroutine
import common.printAligned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

private val singleDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

/**
 * Collection of a flow always happens in the context of the calling coroutine.
 */
suspend fun main() {
    contextPreservation()
    // withContextInFlow()
    flowOnOperator()
}

/**
 * For example, if there is a simple flow, then the following code runs in the context specified by the author of this code,
 * regardless of the implementation details of the simple flow.
 *
 * This property of a flow is called context preservation.
 */
private suspend fun contextPreservation() {
    "preservation".printAligned()
    withContext(singleDispatcher) {
        simpleForPreservation().collect { value ->
            // run in the specified context
            logCoroutine("Collected $value")
        }
    }
}

/**
 * So, by default, code in the flow { ... } builder runs in the context that is provided by a collector of the corresponding flow.
 * For example, consider the implementation of a simple function that prints the thread it is called on and emits three numbers:
 *
 * Since simple().collect is called from the singleDispatcher's thread, the body of simple's flow is also called in the singleDispatcher's thread.
 * This is the perfect default for fast-running or asynchronous code that does not care about the execution context and does not block the caller.
 */
private fun simpleForPreservation(): Flow<Int> = flow {
    logCoroutine("Started simple flow")
    for (i in 1..3) {
        emit(i)
    }
}

private suspend fun withContextInFlow() {
    "context".printAligned()
    /*
    Exception in thread "main" java.lang.IllegalStateException: Flow invariant is violated:
		Flow was collected in EmptyCoroutineContext,
		but emission happened in [DispatchedCoroutine{Active}@64c13468, Dispatchers.Default].
     */
    simpleForWithContextInFlow().collect { value -> println(value) }
}

private fun simpleForWithContextInFlow(): Flow<Int> = flow {
    // The WRONG way to change context for CPU-consuming code in flow builder
    withContext(Dispatchers.Default) {
        for (i in 1..3) {
            Thread.sleep(100) // pretend we are computing it in CPU-consuming way
            emit(i) // emit next value
        }
    }
}

/**
 * The correct way to change the context of a flow is shown in the example below, which also prints the names of the
 * corresponding threads to show how it all works:
 */
private suspend fun flowOnOperator() {
    "flowOn".printAligned()
    // The correct way to change the context of a flow is using flowOn.
    flow {
        for (i in 1..3) {
            Thread.sleep(100) // pretend we are computing it in CPU-consuming way
            logCoroutine("Emitting $i")
            emit(i) // emit next value
        }
    }
        // notice: The flowOn operator will change the default sequential nature of the flow.
        .flowOn(Dispatchers.Default)
        .collect { value ->
            logCoroutine("Collected $value")
        }
}