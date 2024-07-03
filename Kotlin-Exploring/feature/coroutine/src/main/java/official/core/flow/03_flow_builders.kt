package official.core.flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * Flow builders.
 */
suspend fun main() {
    //flowExample()
    //flowOfExample()
    asFlowExample()
}

/**
 * Various collections and sequences can be converted to flows using the .asFlow() extension function.
 */
private suspend fun asFlowExample() {
    (1..10).asFlow().collect {
        println(it)
    }
}

/**
 * The flowOf builder defines a flow that emits a fixed set of values.
 */
private suspend fun flowOfExample() {
    flowOf(1, 2, 3, 4).collect {
            println(it)
    }
}

private suspend fun flowExample() {
    flow {
        for (i in 0..10) {
            emit(i)
        }
    }.collect {
        println(it)
    }
}
