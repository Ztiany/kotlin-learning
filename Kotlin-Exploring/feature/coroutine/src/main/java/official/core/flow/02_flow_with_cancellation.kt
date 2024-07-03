package official.core.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Flows adhere to the general cooperative cancellation of coroutines. As usual, flow collection can be cancelled when
 * the flow is suspended in a cancellable suspending function (like delay). The following example shows how the flow
 * gets cancelled on a timeout when running in a withTimeoutOrNull block and stops executing its code:
 */
suspend fun main() {
    // withTimeout(3000){
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

    println("Done")
}