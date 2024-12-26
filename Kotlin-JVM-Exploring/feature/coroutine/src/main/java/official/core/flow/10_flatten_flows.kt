package official.core.flow

import common.printAligned
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.random.Random

/**
 * Flows represent asynchronously received sequences of values, and so it is quite easy to get into a situation where
 * each value triggers a request for another sequence of values.
 */
suspend fun main() {
    requestFlowSample()
    flatMapConcatOperator()
    flatMapMergeOperator()
    flatMapLatestOperator()
}

/**
 * For example, we can have the following function that returns a flow of two strings 500 ms apart:
 */
private suspend fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500) // wait 500 ms
    emit("$i: Second")
}

/** Now if we have a flow of three integers and call requestFlow on each of them like this: */
private suspend fun requestFlowSample() {
    (1..3).asFlow().map {
        requestFlow(it)
    }.collect {
        /*
        Then we will end up with a flow of flows (Flow<Flow<String>>) that needs to be flattened into a single flow for further processing.
        Collections and sequences have flatten and flatMap operators for this. However, due to the asynchronous nature of flows they call
        for different modes of flattening, and hence, a family of flattening operators on flows exists.
         */
        it.collect { value ->
            println("value: [$value]")
        }
    }
}

/**
 * Concatenation of flows of flows is provided by the flatMapConcat and flattenConcat operators. They are the most direct
 * analogues of the corresponding sequence operators. They wait for the inner flow to complete before starting to collect
 * the next one as the following example shows:
 */
private suspend fun flatMapConcatOperator() {
    "flatMapConcat".printAligned()
    val startTime = System.currentTimeMillis() // remember the start time
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
        .flatMapConcat { requestFlow(it) }
        .collect { value -> // collect and print
            println("flatMapConcatOperator $value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

/**
 * Another flattening operation is to concurrently collect all the incoming flows and merge their values into a single flow
 * so that values are emitted as soon as possible. It is implemented by flatMapMerge and flattenMerge operators. They both
 * accept an optional concurrency parameter that limits the number of concurrent flows that are collected at the same time
 * (it is equal to DEFAULT_CONCURRENCY by default).
 */
private suspend fun flatMapMergeOperator() {
    "flatMapMerge".printAligned()
    val startTime = System.currentTimeMillis() // remember the start time
    (1..3).asFlow().onEach { delay(Random.nextLong(1000)) } // a number every 100 ms
        .flatMapMerge { requestFlow(it) }
        .collect { value -> // collect and print
            println("flatMapMergeOperator $value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

/**
 * In a similar way to the collectLatest operator, that was described in the section "Processing the latest value",
 * there is the corresponding "Latest" flattening mode where the collection of the previous flow is cancelled as soon
 * as new flow is emitted. It is implemented by the flatMapLatest operator.
 */
private suspend fun flatMapLatestOperator() {
    "flatMapLatest".printAligned()
    val startTime = System.currentTimeMillis() // remember the start time
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
        .flatMapLatest { requestFlow(it) }
        .collect { value -> // collect and print
            println("flatMapLatestOperator $value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}
