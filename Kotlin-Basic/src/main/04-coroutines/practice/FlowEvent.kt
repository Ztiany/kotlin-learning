package practice

import analyse.logCoroutine
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

suspend fun main() {

    val sharedFlow = MutableSharedFlow<String>(10, 2, BufferOverflow.SUSPEND)

    val job = CoroutineScope(Dispatchers.Default).launch {
        logCoroutine("collect")
        sharedFlow.collect {
            logCoroutine(it)
            delay(1000)
        }
    }

    delay(1000)
    for (i in 1..100) {
        sharedFlow.tryEmit(i.toString())
    }
    logCoroutine("sent")

    job.join()
}