package practice

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

private var manualOperationFlow = MutableSharedFlow<Boolean>(
    replay = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)

@OptIn(DelicateCoroutinesApi::class, FlowPreview::class)
suspend fun main() {
    val job1 = GlobalScope.launch {
        manualOperationFlow
            .distinctUntilChanged()//distinctUntilChanged must be before debounce.
            .debounce(3000)
            .onEach { pause ->
                println("manualOperationFlow: $pause")
            }.collect()
    }

    val job2 = GlobalScope.launch {
        var count = 0
        while (true) {
            if (count > 40) {
                manualOperationFlow.tryEmit(true)
            } else {
                manualOperationFlow.tryEmit(false)
            }
            count++
            delay(100)
        }
    }

    joinAll(job1, job2)
}

