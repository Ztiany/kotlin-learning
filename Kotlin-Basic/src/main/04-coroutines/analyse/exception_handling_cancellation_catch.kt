package analyse

import kotlinx.coroutines.*

fun main() = runBlocking {

    val parentJob = launch(Dispatchers.Default) {

        launch {
            var i = 0
            while (true) {
                // 1
                // 对于 delay() 函数来说，它可以自动检测当前的协程是否已经被取消，如果已经被取消的话，它会抛出一个 CancellationException，从而终止当前的协程。
                // 下面为了说明 delay 会抛出 Cancellation，对其进行了 try-catch。但是要注意但的是，当我们捕获到 CancellationException 以后，
                // 还要把它重新抛出去。而如果我们删去 throw e 这行代码的话，子协程将同样无法被取消。
                try {
                    delay(500L)
                } catch (e: CancellationException) {
                    println("Catch CancellationException")
                    // 2
                    throw e
                }
                i++
                println("First i = $i")
            }
        }

        launch {
            var i = 0
            while (true) {
                delay(500L)
                i++
                println("Second i = $i")
            }
        }

    }

    delay(2000L)
    parentJob.cancel()
    parentJob.join()
    println("End")
}