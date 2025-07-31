package explore.analyse

import kotlinx.coroutines.*

fun main() {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val job = coroutineScope.launch {
        launch {
            try {
                repeat(5) { i ->
                    println("Child job is working: $i")
                    delay(500)
                }
            } finally {
                println("Child job finally block executed")
            }
        }
    }

    coroutineScope.launch {
        try {
            /*
             * If the Job of the invoking coroutine is cancelled or completed when this
             * suspending function is invoked or while it is suspended, this function throws CancellationException.
             */
            job.join()
        } catch (e: CancellationException) {
            // 走这里是因为发起 join 的协程已经被取消了，必须通过异常来告知调用者。
            println("!!! Job cancelled")
        }
    }

    coroutineScope.cancel()
    Thread.sleep(5000)
    println("Main ends.")
}