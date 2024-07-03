package official.core.flow

import common.printAligned
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
 * There are lots of ways to compose multiple flows.
 */
suspend fun main() {
    zipOperator()
    combineOperator()
}


/**
 * Just like the Sequence.zip extension function in the Kotlin standard library, flows have a zip operator that combines
 * the corresponding values of two flows:
 */
private suspend fun zipOperator() {
    "zip".printAligned()
    // flows have a zip operator that combines the corresponding values of two flows one by one.
    val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..3 every 300 ms
    val strs = flowOf("one", "two", "three").onEach { delay(400) } // strings every 400 ms
    val startTime = System.currentTimeMillis() // remember the start time
    nums.zip(strs) { a, b -> "$a -> $b" } // compose a single string with "zip"
        .collect { value -> // collect and print
            println("zipOperator: [$value] at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

/**
 * When flow represents the most recent value of a variable or operation (see also the related section on conflation),
 * it might be needed to perform a computation that depends on the most recent values of the corresponding flows and
 * to recompute it whenever any of the upstream flows emit a value. The corresponding family of operators is called combine.
 *
 * we can use the combine operator to combine the latest values of two flows when emission happens.
 */
private suspend fun combineOperator() {
    "combine".printAligned()
    //Combine operator combines the latest values of two flows once emission happens.
    val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..3 every 300 ms
    val strs = flowOf("one", "two", "three").onEach { delay(400) } // strings every 400 ms
    val startTime = System.currentTimeMillis() // remember the start time
    nums.combine(strs) { a, b -> "$a -> $b" } // compose a single string with "combine"
        .collect { value -> // collect and print
            println("combineOperator: $value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}