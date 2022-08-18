package core.flow

import io.reactivex.internal.subscriptions.SubscriptionHelper.cancel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

/**
 *Make flows cancellable.
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 2020/9/30 15:39
 */
fun main() = runBlocking {
    //naturalCancellable(this)
    //naturalNotCancellable(this)
    makeCancellable(this)
}

private suspend fun naturalCancellable(scope: CoroutineScope) {
    //the flow builder performs additional ensureActive checks for cancellation on each emitted value. It means that a busy loop emitting from a flow { ... } is cancellable.
    flow {
        for (i in 1..5) {
            println("Emitting $i")
            emit(i)
        }
    }.collect { value ->
        if (value == 3) {
            scope.cancel()
        }
        println("naturalCancellable: $value")
    }
}

private suspend fun naturalNotCancellable(coroutineScope: CoroutineScope) {
    //However, most other flow operators do not do additional cancellation checks on their own for performance reasons.
    (1..5).asFlow().collect { value ->
        if (value == 3) coroutineScope.cancel()
        println("naturalNotCancellable: $value")
    }
}

private suspend fun makeCancellable(coroutineScope: CoroutineScope) {
    //In the case where you have a busy loop with coroutines you must explicitly check for cancellation. You can add .onEach { currentCoroutineContext().ensureActive() },
    // but there is a ready-to-use cancellable operator provided to do that
    (1..5).asFlow()
            .cancellable()
            .collect { value ->
                if (value == 3) coroutineScope.cancel()
                println("naturalNotCancellable: $value")
            }
}
