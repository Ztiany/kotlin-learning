package official.core.flow

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * 1. **Representing asynchronous multiple values**: Using the List<Int> result type, means we can only return all the values at once.
 * To represent the stream of values that are being computed asynchronously, we can use a Flow<Int> type just like we would use a
 * Sequence<Int> type for synchronously computed values.
 * 2. **Flows are cold by default**: Flows are cold streams similar to sequences — the code inside a flow builder does not run until the
 * flow is collected. This becomes clear in the following example:
 */
suspend fun main() {
    //first time.
    simple(5).map {
        it * it
    }.collect {
        println("first time result: $it")
    }

    //second time
    simple(10).map {
        it * it * it
    }.collect {
        println("second time result: $it")
    }
}

private fun simple(times: Int) = flow {
    for (i in 0..times) {
        emit(i)
    }
}
