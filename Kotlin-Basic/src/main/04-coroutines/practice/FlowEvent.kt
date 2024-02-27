package practice

import analyse.logCoroutine
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

suspend fun main() {

    val sharedFlow = MutableSharedFlow<String>(2, 2, BufferOverflow.SUSPEND)

    val job1 = CoroutineScope(Dispatchers.Default).launch {
        logCoroutine("collect-1")
        sharedFlow.collect {
            logCoroutine("collect-1: $it")
            delay(1000)
        }
    }

    delay(1000)

    for (i in 1..100) {
        sharedFlow.tryEmit(i.toString())
    }

    val job2 = CoroutineScope(Dispatchers.Default).launch {
        logCoroutine("collect-2")
        sharedFlow.collect {
            logCoroutine("collect-2: $it")
            delay(1000)
        }
    }

    logCoroutine("sent")

    job1.join()
}