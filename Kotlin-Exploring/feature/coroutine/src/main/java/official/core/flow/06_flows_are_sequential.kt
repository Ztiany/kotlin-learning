package official.core.flow

import common.printAligned
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * Each individual collection of a flow is performed sequentially unless special operators that operate on multiple flows
 * are used. The collection works directly in the coroutine that calls a terminal operator. No new coroutines are launched
 * by default. Each emitted value is processed by all the intermediate operators from upstream to downstream and is then
 * delivered to the terminal operator after.
 */
suspend fun main() {
    flowAreSequential()
}

/**
 * See the following example that filters the even integers and maps them to strings:
 */
private suspend fun flowAreSequential() {
    "filter".printAligned()
    (1..5).asFlow()
        .filter {
            println("Filter $it")
            it % 2 == 0
        }
        .map {
            println("Map $it")
            "string $it"
        }.collect {
            println("Collect $it")
        }
}
