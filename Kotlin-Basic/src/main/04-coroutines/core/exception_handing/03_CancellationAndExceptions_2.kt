package core.exception_handing

import kotlinx.coroutines.*

/*
如果一个协程遇到了 CancellationException 以外的异常，它将使用该异常取消它的父协程。
这个行为无法被覆盖，并且用于为结构化的并发（structured concurrency） 提供稳定的协程层级结构。
CoroutineExceptionHandler 的实现并不适用于子协程。

在本例中，CoroutineExceptionHandler 总是被设置在由 GlobalScope 启动的协程中。
将异常处理者设置在 runBlocking 主作用域内启动的协程中是没有意义的，尽管子协程已经设置了异常处理者， 但是主协程也总是会被取消的。

当父协程的所有子协程都结束后，原始的异常才会被父协程处理。
 */
fun main() = runBlocking {
    //sampleStart
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    val subHandler = CoroutineExceptionHandler { _, exception ->
        println("Sub Caught $exception")
    }

    val job = GlobalScope.launch(handler) {

        val sub1 = launch {
            // the first child
            try {
                delay(Long.MAX_VALUE)
                //delay(2000)
            } finally {
                withContext(NonCancellable) {
                    println("Children are cancelled, but exception is not handled until all children terminate")
                    delay(100)
                    println("The first child finished its non cancellable block")
                }
            }
        }

        val sub2 = launch(/*subHandler not working*/) {
            // the second child
            delay(10)
            println("Second child throws an exception")
            //将会导致父协程被取消，即使加上 subHandler 也不起作用。
            throw ArithmeticException()
            //throw CancellationException() //如果是抛出 CancellationException，那么不会有影响。
        }

        //父协程将得不到执行。
        joinAll(sub1, sub2)
        print("running in the parent")
    }

    job.join()
//sampleEnd
}
