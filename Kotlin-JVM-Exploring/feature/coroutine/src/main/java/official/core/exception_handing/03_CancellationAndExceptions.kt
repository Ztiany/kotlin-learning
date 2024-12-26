package official.core.exception_handing

import kotlinx.coroutines.*
import okhttp3.internal.wait

/*
 取消与异常紧密相关。协程内部使用 CancellationException 来进行取消，这个异常会被所有的处理者忽略，所以那些可以被 catch 代码块捕获的异常仅仅应该被用来作为
 额外调试信息的资源。

 当一个协程使用 Job.cancel 取消的时候，它会被终止，但是它不会取消它的父协程。
 */
fun main() = runBlocking {
    //sampleStart
    val job = launch {

        val launchChild = launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                println("Launch Child is cancelled")
            }
        }

        val asyncChild = async {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                println("Launch Child is cancelled")
            }
            "asyncChild"
        }

        yield()
        println("Cancelling launchChild")
        launchChild.cancel()

        try {
            launchChild.join()
        } catch (e: CancellationException) {
            // 不会走这里，这是 launch 的特性。
            println("launchChild join error")
        }

        println("Cancelling asyncChild")
        asyncChild.cancel()

        try {
            asyncChild.await()
        } catch (e: CancellationException) {
            // 会走这里，这是 async 的特性。
            println("asyncChild await error")
        }

        yield()
        println("Parent is not cancelled")
    }

    job.join()

//sampleEnd
}