package core.exception_handing

import kotlinx.coroutines.*

/*
但是如果不想将所有的异常打印在控制台中呢？将未捕获异常打印到控制台的默认行为是可自定义的。

    1. 根协程中的 CoroutineExceptionHandler 上下文元素可以被用于这个根协程通用的 catch 块，及其所有可能自定义了异常处理的子协程。 它类似于 Thread.uncaughtExceptionHandler 。
    2. 无法从 CoroutineExceptionHandler 的异常中恢复。当调用处理者的时候，协程已经完成并带有相应的异常。通常，该处理者用于记录异常，显示某种错误消息，终止和（或）重新启动应用程序。

在 JVM 中可以重定义一个全局的异常处理者来将所有的协程通过 ServiceLoader 注册到 CoroutineExceptionHandler。

CoroutineExceptionHandler 仅在未捕获的异常上调用——即没有以其他任何方式处理的异常才会交给 CoroutineExceptionHandler 处理。

        1. 对于所有子协程委托，它们的父协程处理它们的异常，然后它们也委托给其父协程，以此类推直到根协程， 因此永远不会使用在其上下文中设置的 CoroutineExceptionHandler。
        2. async 构建器始终会捕获所有异常并将其表示在结果 Deferred 对象中， 因此它的 CoroutineExceptionHandler 也无效。
 */
fun main() = runBlocking {
    //sampleStart
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    val job = GlobalScope.launch(handler) {
        println("launch")
        throw AssertionError()
    }

    val deferred = GlobalScope.async(handler/*对于 async，handler 无效*/) {
        println("async")
        // Nothing will be printed, relying on user to call deferred.await()
        throw ArithmeticException()
    }

    //deferred.await()，调用 deferred 的 await 会崩溃，而调用其 join，会按照 launch 的逻辑处理。
    joinAll(job, deferred)
    //sampleEnd
}