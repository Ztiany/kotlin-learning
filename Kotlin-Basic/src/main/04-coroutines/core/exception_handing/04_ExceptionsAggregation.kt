package core.exception_handing

import kotlinx.coroutines.*
import java.io.IOException

/*
当协程的多个子协程因异常而失败时， 一般规则是“取第一个异常”，因此将处理第一个异常。 在第一个异常之后发生的所有其他异常都作为被抑制的异常绑定至第一个
异常（exception.suppressed）。

1. 下面代码只在 JDK7 以上支持 suppressed 异常的环境中才能正确工作。
2. 这个机制当前只能在 Java 1.7 以上的版本中使用。 在 JS 和原生环境下暂时会受到限制，但将来会取消。
 */
fun main() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception with suppressed ${exception.suppressed?.contentToString()}")
    }

    val job = GlobalScope.launch(handler) {
        launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                throw ArithmeticException()
            }
        }

        launch {
            delay(100)
            throw IOException()
        }

        delay(Long.MAX_VALUE)
    }

    job.join()
}