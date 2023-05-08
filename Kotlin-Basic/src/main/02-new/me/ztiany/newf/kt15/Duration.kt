package me.ztiany.newf.kt15

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

inline fun timeCost(block: ()-> Unit): Long {
    val startTime = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - startTime
}

inline fun <T> timeCostReturns(block: () -> T): Pair<T, Long> {
    val startTime = System.currentTimeMillis()
    val result = block()
    return result to (System.currentTimeMillis() - startTime)
}

@OptIn(ExperimentalTime::class)
suspend fun main() {

    val duration = measureTime {
        Thread.sleep(100)
    }
    println(duration)

    val timeCost = timeCost {
        Thread.sleep(100)
    }
    println(timeCost)

    val (value1, duration1) = measureTimedValue {
        Thread.sleep(500)
        1000
    }

    val (value2, duration2) = timeCostReturns {
        Thread.sleep(500)
        1000
    }

    println(value1)
    println(duration1)
    println(value2)
    println(duration2)

    val scope = CoroutineScope(Dispatchers.IO)

    scope.launch {
        val duration = measureTime {
            delay(1000)
        }
        println(duration)

        val mark = TimeSource.Monotonic.markNow()
        delay(1000)
        println(mark.elapsedNow())
    }.join()

}