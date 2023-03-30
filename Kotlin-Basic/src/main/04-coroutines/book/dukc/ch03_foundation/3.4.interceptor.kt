package book.dukc.ch03_foundation

import kotlin.concurrent.thread
import kotlin.coroutines.*

/**
 * 《深入理解 Kotlin 协程》chapter 3.4：拦截器
 */
suspend fun main() {
/*
    前面以及学习了：
        1. Kotlin 协程可以通过调用挂起函数实现挂起；
        2. 可以通过 Continuation 的恢复调用实现恢复；
        3. 协程能够通过绑定一个上下文来设置一些数据来丰富协程的能力。

    那么我们最关心的问题来了：协程如何处理线程的调度？在 Continuation 和协程上下文的基础上，标准库又提供了一个叫作拦截器（Interceptor）的组件，
    它允许我们拦截协程异步回调时的恢复调用。既然可以拦截恢复调用，那么想要操纵协程的线程调度应该不是什么难事。
 */

    //拦截的位置【3.4.1】
    //thePositionOfInterception()

    //拦截器的使用【3.4.2】
    theUsageOfInterceptor()

    //拦截器的执行细节【3.4.3】
    theDetailsOfInterceptor()
}

///////////////////////////////////////////////////////////////////////////
// 拦截的位置
///////////////////////////////////////////////////////////////////////////
private suspend fun suspendFunc02(a: String, b: String) = suspendCoroutine<Int> { continuation ->
    thread {
        continuation.resumeWith(Result.success(5))

        suspend fun notSuspend() = suspendCoroutine<Int> { continuation ->
            continuation.resume(100)
        }

    }
}

private suspend fun thePositionOfInterception() {
    //先来回顾一下 Continuation 的恢复调用在协程中的调用情况
    suspend {
        suspendFunc02("Hello", "Kotlin")
        suspendFunc02("Hello", "Coroutine")
    }.startCoroutine(object : Continuation<Int> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            println("resumeWith: $result")
        }
    })
    //我们启动了一个协程，并在其中调用了两次挂起函数 suspendFunc02，这个挂起函数每次执行都会异步挂起，那么这个过程中发生了几次恢复调用呢？
    //·协程启动时调用一次，通过恢复调用来开始执行协程体从开始到下一次挂起之间的逻辑。
    //·挂起点处如果异步挂起，则在恢复时会调用一次。由于这个过程中有两次挂起，因此会调用两次。

    //由此可知，恢复调用的次数为 1+n 次，其中 n 是协程体内真正挂起执行异步逻辑的挂起点的个数。
}

///////////////////////////////////////////////////////////////////////////
// 拦截器的使用
///////////////////////////////////////////////////////////////////////////

//挂起点恢复执行的位置都可以在需要的时候添加拦截器来实现一些 AOP 操作。拦截器也是协程上下文的一类实现，定义拦截器只需要实现拦截器的接口，并添加到对应的协程的上下文中即可。
private class LogInterceptor : ContinuationInterceptor {
    override val key = ContinuationInterceptor

    //拦截器的关键拦截函数是 interceptContinuation，可以根据需要返回一个新的 Continuation 实例。
    //我们在 LogContinuation 的 resumeWith 中打印日志，接下来把它设置到上下文中，程序运行时就会有相应的日志输出。
    override fun <T> interceptContinuation(continuation: Continuation<T>) = LogContinuation(continuation)
}

private class LogContinuation<T>(private val continuation: Continuation<T>) : Continuation<T> by continuation {
    override fun resumeWith(result: Result<T>) {
        println("before resumeWith: $result")
        continuation.resumeWith(result)
        println("after resumeWith.")
    }
}

private suspend fun theUsageOfInterceptor() {
    suspend {
        suspendFunc02("Hello", "Kotlin")
        suspendFunc02("Hello", "Coroutine")
    }.startCoroutine(object : Continuation<Int> {
        override val context: CoroutineContext
            get() = LogInterceptor()

        override fun resumeWith(result: Result<Int>) {
            println("resumeWith: $result")
        }
    })
}

///////////////////////////////////////////////////////////////////////////
// 拦截器的执行细节
///////////////////////////////////////////////////////////////////////////
private fun theDetailsOfInterceptor() {
/*
    我们曾经提到过一个“马甲”SafeContinuation，其内部有个叫作 delegate 的成员，我们之前称之为协程体，之所以可以这么讲，主要是因为之前没有在协程中添加拦截器。
    而添加了拦截器之后，delegate 其实就是拦截器拦截之后的 Continuation 实例了。例如在 theUsageOfInterceptor 中，delegate 其实就是拦截之后的 LogContinuation 的实例。

    协程体在挂起点处先被拦截器拦截，再被 SafeContinuation 保护了起来。想要让协程体真正恢复执行，先要经过这两个过程，这也为协程支持更加复杂的调度逻辑提供了基础。
    除了打印日志，拦截器的作用还有很多，最常见的就是控制线程的切换。
 */
}