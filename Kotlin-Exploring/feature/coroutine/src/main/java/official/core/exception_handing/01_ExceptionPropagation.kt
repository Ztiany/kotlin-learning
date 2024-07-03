package official.core.exception_handing

import kotlinx.coroutines.*

/*
协程构建器有两种风格：自动的传播异常（launch 以及 actor） 或者将它们暴露给用户（async 以及 produce）。
    前者这类构建器将异常视为未捕获异常，类似于 Java 的 Thread.uncaughtExceptionHandler。【这种行为，默认只会将异常信息打印到控制台】
    后者依赖用户来最终消耗异常，比如说，通过 await 或 receive 。
 */
fun main() = runBlocking {

    CoroutineScope(Dispatchers.Default).launch {
        val a = withContext(Dispatchers.IO) {
            1 / 0
        }
    }

    //通过 GlobeScope 启动的协程单独启动一个协程作用域，内部的子协程遵从默认的作用域规则。通过 GlobeScope 启动的协程“自成一派”。
    val job = GlobalScope.launch {
        delay(2000)
        println("Throwing exception from launch")
        //Kotlin 将自动在控制台打印抛出的异常
        throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
    }

    try {
        //launch 的异常不会被处理
        job.join()
        println(message = "after joining")
    } catch (e: Exception) {
        //不会走这里，不会有打印。
        println(message = "catch $e of launch")
        e.printStackTrace()
    }

    println("Joined failed job")

    val deferred = GlobalScope.async {
        delay(1000)
        println("Throwing exception from async")
        //没有打印任何东西，依赖用户去调用等待
        throw ArithmeticException() // Nothing is printed, relying on user to call await
    }

    try {
        deferred.await()
        println(message = "Unreached")
    } catch (e: ArithmeticException) {
        println("Caught ArithmeticException")
    }
}