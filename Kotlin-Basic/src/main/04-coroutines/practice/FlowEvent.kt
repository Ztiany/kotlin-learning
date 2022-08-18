package practice

import analyse.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect

suspend fun main() {

    val sharedFlow = MutableSharedFlow<String>(10, 2, BufferOverflow.SUSPEND)

    val job = CoroutineScope(Dispatchers.Default).launch {
        log("collect")
        sharedFlow.collect {
            log(it)
            delay(1000)
        }
    }

    delay(1000)
    for (i in 1..100) {
        sharedFlow.tryEmit(i.toString())
    }
    log("sent")

    job.join()
}