package explore.analyse

import kotlinx.coroutines.*


suspend fun main() {
    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {

        val job1 = async { networkTask1() }
        val job2 = async { networkTask2() }

        try {
            val result1 = job1.await()
            val result2 = job2.await()
            println("result1 = $result1, result2 = $result2")
        } catch (e: Exception) {
            // 这里的 try-catch 可以捕获到 await() 抛出的异常。
            println("catch $e")
        }

    }.join()// 是这里的 join 导致异常被打印出来，如果没有这个 join，异常将不会被打印出来。

    println("Before End")
    delay(2000)
    println("Real End")
}

private fun networkTask1(): String {
    Thread.sleep(1000)
    println("networkTask1")
    if (true) {
        throw RuntimeException("networkTask1")
    }
    return "networkTask1"
}

private fun networkTask2(): String {
    Thread.sleep(2000)
    println("networkTask2")
    return "networkTask2"
}