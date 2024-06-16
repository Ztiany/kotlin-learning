package core.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Cancellation.
 */
suspend fun main() {
    //withTimeout(3000){
    withTimeoutOrNull(3000) {
        flow {
            for (i in 0..5) {
                // flow collection can be cancelled when the flow is suspended in a cancellable suspending function (like delay ).
                delay(1000)
                emit(i)
            }
        }.collect {
            println("result $it")
        }
    }

    println("Down")
}