package book.dukc.ch06_core

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.atomic.AtomicInteger

/**
 * 《深入理解Kotlin协程》chapter 6.5：并发安全
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 2020/9/25 10:08
 */
suspend fun main() {
    //我们使用线程在解决并发问题的时候总是会遇到线程安全的问题，而 Java 平台上的 Kotlin 协程实现免不了存在并发调度的情况，因此线程安全同样值得留意。

    //不安全的并发访问【6.5.1】
    //unsafeAccess()

    //协程的并发工具【6.5.2】
    concurrentToolForCoroutines()

    //避免访问外部可变状态【6.5.3】
    avoidAccessingOuterVariable()
}

///////////////////////////////////////////////////////////////////////////
// 不安全的并发访问
///////////////////////////////////////////////////////////////////////////
private suspend fun unsafeAccess() {
    //运行在 Java 平台上，默认启动的协程会被调度到 Default 这个基于线程池的调度器上，因此 count++ 是不安全的
    var count = 0
    List(1000) {
        GlobalScope.launch {
            count++
        }
    }.joinAll()
    println(count)

    //安全方案有很多，比如将 count 声明为原子类型，确保自增操作为原子操作。
    val safeCount = AtomicInteger(0)
    List(1000) {
        GlobalScope.launch { safeCount.incrementAndGet() }
    }.joinAll()
    println(safeCount.get())

    //另外，Kotlin 官方提供了一套原子操作的封装 kotlinx.atomicfu（https://github.com/kotlin/kotlinx.atomicfu），它的 Java 平台版本是基于 AtomicXXXFieldUpdater 来实现的。
    //atomicfu 其实就是 atomic field updater 的缩写。使用 AtomicXXXFieldUpdater 比直接使用 AtomicReference 在内存上的表现更好。值得一提的是，官方的协程框架内部的状态维护就是基于这个框架实现的。
}

///////////////////////////////////////////////////////////////////////////
// 协程的并发工具
///////////////////////////////////////////////////////////////////////////
private suspend fun concurrentToolForCoroutines() {
/*
    除了我们在线程中常用的解决并发问题的手段之外，协程框架也提供了一些并发安全的工具，包括：
        ·Channel：并发安全的消息通道，我们已经非常熟悉。
        ·Mutex：轻量级锁，它的 lock 和 unlock 从语义上与线程锁比较类似，之所以轻量是因为它在获取不到锁时不会阻塞线程而只是挂起等待锁的释放。
        ·Semaphore：轻量级信号量，信号量可以有多个，协程在获取到信号量后即可执行并发操作。
 */
    var countMutex = 0
    val mutext = Mutex()
    List(1000) {
        GlobalScope.launch {
            mutext.withLock { countMutex++ }
        }
    }.joinAll()
    println(countMutex)

    var countSemaphore = 0
    val semaphore = Semaphore(1)
    List(1000) { GlobalScope.launch { semaphore.withPermit { countSemaphore++ } } }.joinAll()
    println(countSemaphore)
}


///////////////////////////////////////////////////////////////////////////
// 避免访问外部可变状态
///////////////////////////////////////////////////////////////////////////
private suspend fun avoidAccessingOuterVariable() {
/*
    我们前面一直在探讨如何正面解决线程安全的问题，实际上多数时候我们并不需要这么做。我们完全可以想办法规避因可变状态的共享而引发的安全问题，
    上述计数程序出现问题的根源是启动了多个协程且访问一个公共的变量 count，如果我们能避免在协程中访问可变的外部状态，就基本上不用担心并发安全的问题。

    如果我们编写函数时要求它不得访问外部状态，只能基于参数做运算，通过返回值提供运算结果，这样的函数不论何时何地调用，只要传入的参数相同，
    结果就保持不变，因此它就是可靠的，这样的函数也被称为纯函数。我们在设计基于协程的逻辑时，应当尽可能编写纯函数，以降低程序出错的风险。
 */

    //比如下面程序：其中，var count被改为val count，直接在协程内部访问外部count实现自增被改为返回增量结果。
    //你可能会觉得这个例子过于简单，然而实际情况也莫过于此。总而言之，如非必须，则避免访问外部可变状态；如无必要，则避免使用可变状态。
    val count = 0
    val result = count + List(1000) {
        GlobalScope.async { 1 }
    }.map {
        it.await()
    }.sum()
    println(result)
}
