package official.core.flow

import common.printAligned
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

/**
 * Running different parts of a flow in different coroutines can be helpful from the standpoint of the overall time
 * it takes to collect the flow, especially when long-running asynchronous operations are involved.
 */
suspend fun main() {
    timeSpentOverall()
    bufferOperator()
    conflationOperator()
    processLatestOperator()
}

/**
 * For example, consider a case when the emission by a simple flow is slow, taking 100 ms to produce an element; and
 * collector is also slow, taking 300 ms to process an element. Let's see how long it takes to collect such a flow
 * with three numbers:
 */
private suspend fun timeSpentOverall() {
    "time spending".printAligned()
    val time = measureTimeMillis {
        simpleForTimeSpending().collect { value ->
            delay(300) // pretend we are processing it for 300 ms
            println(value)
        }
    }
    // It produces something like this, with the whole collection taking around 1200 ms (three numbers, 400 ms for each):
    println("Collected in $time ms")
}

private fun simpleForTimeSpending(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100) // pretend we are asynchronously waiting 100 ms
        emit(i) // emit next value
    }
}

/**
 * We can use a buffer operator on a flow to run emitting code of the simple flow concurrently with collecting code, as
 * opposed to running them sequentially:
 */
private suspend fun bufferOperator() {
    "buffer".printAligned()
    measureTimeMillis {
        simpleForTimeSpending()
            .buffer()
            // It produces the same numbers just faster, as we have effectively created a processing pipeline, having to
            // only wait 100 ms for the first number and then spending only 300 ms to process each number. This way it
            // takes around 1000 ms to run:
            .collect { value ->
                //time = 100 + (300 * 3) ≈ 1000
                delay(300) // pretend we are processing it for 300 ms
                println(value)
            }
    }.let {
        println("bufferOperator: time = $it")
    }
}

/**
 * When a flow represents partial results of the operation or operation status updates, it may not be necessary to process
 * each value, but instead, only most recent ones. In this case, the conflate operator can be used to skip intermediate
 * values when a collector is too slow to process them.
 */
private suspend fun conflationOperator() {
    "conflate".printAligned()
    // the conflate operator can be used to skip intermediate values when a collector is too slow to process them.
    measureTimeMillis {
        simpleForTimeSpending()
            // Conflates flow emissions via conflated channel and runs collector in a separate coroutine. The effect of
            // this is that emitter is never suspended due to a slow collector, but collector always gets the most recent value emitted.
            .conflate()
            .collect { value ->
                delay(300) // pretend we are processing it for 300 ms
                println(value)
            }
    }.let {
        println("conflationOperator: time = $it")
    }
}

/**
 * Conflation is one way to speed up processing when both the emitter and collector are slow. It does it by dropping emitted values.
 * The other way is to cancel a slow collector and restart it every time a new value is emitted. There is a family of xxxLatest operators
 * that perform the same essential logic of a xxx operator, but cancel the code in their block on a new value. Let's try changing conflate
 * to collectLatest in the previous example:
 */
private suspend fun processLatestOperator() {
    "collectLatest".printAligned()
    // cancel a slow collector and restart it every time a new value is emitted.
    measureTimeMillis {
        simpleForTimeSpending()
            .collectLatest { value ->
                println("Collecting $value")
                delay(300) // pretend we are processing it for 300 ms
                println("Done $value")
            }
    }.let {
        println("processLatestOperator: time = $it")
    }
}
