package core.flow

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * 1. Representing multiple values.
 * 2. Flows are cold by default.
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

fun simple(times: Int) = flow<Int> {
    for (i in 0..times) {
        emit(i)
    }
}