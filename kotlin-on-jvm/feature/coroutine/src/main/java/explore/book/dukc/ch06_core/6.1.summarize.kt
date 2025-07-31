package explore.book.dukc.ch06_core

import kotlinx.coroutines.*
import java.io.InputStream
import java.io.OutputStream
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * 《深入理解Kotlin协程》chapter 6.1：协程框架概述
 */
suspend fun main() {
    //协程框架的构成【6.1.1】
    theConsistent()

    //协程的启动模式【6.1.2】
    launchMode()

    //协程的调度器【6.1.3】

    //协程的全局异常处理器【6.1.4】
    exceptionHandling()

    //协程的取消检查【6.1.5】
    cancellationAndCheck()

    //协程的超时取消【6.1.6】
    cancellationAndTimeout()

    //禁止取消【6.1.7】
    disableCancellation()
}

private fun theConsistent() {
/*
    Kotlin 协程的官方框架 kotlin.coroutines 是一套独立于标准库之外的以生产为目的的框架，框架本身提供了丰富的 API 来支撑生产环境中异步程序的设计和实现。
    它主要由以下几部分构成：

        ·core：框架的核心逻辑，包括第5章讨论的所有功能以及 Channel、Flow 等特性。
        ·ui：包括 android、javafx、swing 三个库，用于提供各平台的UI调度器和一些特有的逻辑（例如 Android 平台上的全局异常处理器设置）。
        ·reactive 相关：提供对各种响应式编程框架的协程支持，包括如下几项。
            ·reactive：提供对 Reative Streams（https://www.reactivestreams.org/）的协程支持。
            ·reactor：提供对 Reactor（https://projoctreactor.io/）的协程支持，Spring 的 WebFlux 就是基于 Reactor 实现的。
            ·rx2：提供对 RxJava 2.x（https://github.com/RecictiveX/RxJava）版本的协程支持。
        ·integration：提供与其他框架的异步回调的集成，包括如下几项。
            ·jdk8：提供对 CompletableFuture 的协程 API 的支持。
            ·guava：提供对 ListenableFuture 的协程 API 的支持。
            ·slf4j：提供 MDCContext 作为协程上下文的元素，以使协程中使用 slf4j 打印日志时能够读取对应的 MDC 中的键值对。
            ·play-services：提供对 Google Play 服务中的 Task 的协程 API 的支持。
 */
}

///////////////////////////////////////////////////////////////////////////
// 协程的启动模式
///////////////////////////////////////////////////////////////////////////
fun launchMode() {
/*
启动模式总共有4种。
    ·DEFAULT：协程创建后，立即开始调度，在调度前如果协程被取消，其将直接进入取消响应的状态。
    ·ATOMIC：协程创建后，立即开始调度，协程执行到第一个挂起点之前不响应取消。
    ·LAZY：只有协程被需要时，包括主动调用协程的 start、join 或者 await 等函数时才会开始调度，如果调度前就被取消，那么该协程将直接进入异常结束状态。
    ·UNDISPATCHED：协程创建后立即在当前函数调用栈中执行，直到遇到第一个真正挂起的点。

要理清这四种模式的区别，需要先搞明白立即调度和立即执行的区别。
    1. 立即调度表示协程的调度器会立即接收调度指令，但具体执行的时机以及在哪个线程上执行，还需要根据调度器的具体情况而定;
    2. 也就是说立即调度到立即执行之间通常会有一段时间。

因此，我们得出以下结论：
    ·DEFAULT 虽然是立即调度，但也有可能在执行前被取消。
    ·UNDISPATCHED 是立即执行，因此协程一定会执行。
    ·ATOMIC 虽然是立即调度，但其将调度和执行两个步骤合二为一了，就像它的名字一样，其保证调度和执行是原子操作，因此协程也一定会执行。
    ·UNDISPATCHED 和 ATOMIC 虽然都会保证协程一定执行，但在第一个挂起点之前，前者运行在协程创建时所在的线程，后者则会调度到指定的调度器所在的线程上执行。

一般，业务开发实践中通常使用 DEFAULT 和 LAZY 这两个启动模式就足够了。
 */
    GlobalScope.launch(start = CoroutineStart.ATOMIC) {

    }
}

///////////////////////////////////////////////////////////////////////////
// 协程调度器
///////////////////////////////////////////////////////////////////////////
private fun dispatchers() {
/*
内置的四种调度器：
    ·Default：默认调度器，适合处理后台计算，其是一个 CPU 密集型任务调度器。
    ·IO：IO 调度器，适合执行 IO 相关操作，其是一个 IO 密集型任务调度器。
    ·Main：UI 调度器，根据平台不同会被初始化为对应的 UI 线程的调度器，例如在 Android 平台上它会将协程调度到 UI 事件循环中执行，即通常在主线程上执行。
    ·Unconfined：“无所谓”调度器，不要求协程执行在特定线程上。协程的调度器如果是 Unconfined，那么它在挂起点恢复执行时会在恢复所在的线程上直接执行，当然，如果嵌套创建以它为调度器的协程，那么这些协程会在启动时被调度到协程框架内部的事件循环上，以避免出现 StackOverflow。
使用场景：
    1. 为确保对UI读写的并发安全性，我们需要确保相关协程在UI线程上执行，因此需要指定调度器为 Main。
    2. 如果是只包含单纯的计算任务的协程，则通常其存续时间较短，比较适合使用 Default 调度器；
    3. 如果是包含 IO 操作的协程，则通常其存续时间较长，且无须连续占据 CPU 资源，因此适合使用 IO 作为其调度器。

Default 和 IO：
    1. 对于 Default 和 IO 这两个调度器的实现，它们背后实际上是同一个线程池。那么，为什么二者在使用上会存在差异呢？
    2. 由于 IO 任务通常会阻塞实际执行任务的线程，在阻塞过程中线程虽然不占用 CPU，但却占用了大量内存，这段时间内被 IO 任务占据线程实际上是资源使用不合理的表现；
    3. 因此 IO 调度器对于 IO 任务的并发量做了限制，避免过多的 IO 任务并发占用过多的系统资源，同时在调度时为任务打上 PROBABLY_BLOCKING 的标签，以方便线程池在执行任务调度时对阻塞任务和非阻塞任务区别对待。

自定义调度器:
    1. 如果内置的调度器无法满足需求，我们也可以自行定义调度器，只需要实现 CoroutineDispatcher 接口。
    2. 使用扩展函数 asCoroutineDispatcher 可以将 Executor 转为调度器，不过这个调度器需要在使用完毕后主动关闭，以免造成线程泄露。

使用 withContext 切换调度器：
    1. withContext 会将参数中的 Lambda 表达式调度到对应的调度器上，它自己本身就是一个挂起函数，返回值为 Lambda 表达式的值，由此可见它的作用几乎等价于 async{...}.await()。
    2. 与 async{...}.await() 相比，withContext 的内存开销更低，因此对于使用 async 之后立即调用 await 的情况，应当优先使用 withContext。
 */

    GlobalScope.launch(context = Dispatchers.Default) {

    }

}

///////////////////////////////////////////////////////////////////////////
// 异常处理
///////////////////////////////////////////////////////////////////////////

//定义全局异常处理，在 classpath 下面创建 META-INF/services 目录，并在其中创建一个名为 kotlinx.coroutines.CoroutineExceptionHandler 的文件，文件的内容就是全局异常处理器的全类名。
private class CustomCoroutineExceptionHandler : CoroutineExceptionHandler {

    override val key = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        println("Global Coroutine exception: $exception")
    }
}

private suspend fun exceptionHandling() {
    //1. 官方协程框架支持全局的异常处理器。在根协程未设置异常处理器时，未捕获异常会优先传递给全局异常处理器处理，之后再交给所在线程的 UncaughtExceptionHandler。
    //2. 全局异常处理器可以获取到所有协程未处理的未捕获异常，不过它并不能对异常进行捕获。虽然不能阻止程序崩溃，全局异常处理器在程序调试和异常上报等场景中仍然有非常大的用处。
    //3. 全局异常处理器不适用于 JavaScript 和 Native 平台。
    GlobalScope.launch { throw ArithmeticException("Hey!") }.join()
}

///////////////////////////////////////////////////////////////////////////
// 协程的取消检查
///////////////////////////////////////////////////////////////////////////
private fun cancellationAndCheck() {
/*
    挂起函数可以通过 suspendCancellableCoroutine 来响应所在协程的取消状态，我们在设计异步任务时，异步任务的取消响应点可能就在这些挂起点处。

    但如果没有挂起点的执行逻辑，就需要额外处理了，方法有两个：
        1. 对所在协程添加 isActive 判断。【copyTo 和 copyToSuspend 实现】
        2. 使用 yield 函数：yield 函数的作用主要是检查所在协程的状态，如果已经取消，则抛出取消异常予以响应。此外，它还会尝试出让线程的执行权，给其他协程提供执行机会。【copyToYield 实现】
 */
}

/*
将这段程序直接放入协程中之后，就会发现协程的取消状态对它没有丝毫影响。想要解决这个问题，首先可以想到的是在 while 环处添加一个对所在协程的 isActive 的判断。
这个思路没有问题，我们可以通过全局属性 coroutineContext 获取所在协程的 Job 实例来做到这一点。【参考 copyToSuspend】
 */
private fun InputStream.copyTo(out: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE): Long {
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)
    while (bytes >= 0) {
        out.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
    }
    return bytesCopied
}

/*
1. 如果 job 为空，那么说明所在的协程是一个简单协程，这种情况不存在取消逻辑；
2. 当 job 不为空时，如果 isActive 也不为 true，则说明当前协程被取消了，抛出它对应的取消异常即可。
 */
@OptIn(InternalCoroutinesApi::class)//getCancellationException 被标记为内部 API，因此我们需要添加注解 @OptIn(InternalCoroutinesApi::class) 才可编译通过。
suspend fun InputStream.copyToSuspend(out: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE): Long {
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)
    val job = coroutineContext[Job]
    while (bytes >= 0) {
        job?.let { it.takeIf { it.isActive } ?: throw job.getCancellationException() }
        out.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
    }
    return bytesCopied
}

suspend fun InputStream.copyToYield(out: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE): Long {
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)
    while (bytes >= 0) {
        yield()
        out.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
    }
    return bytesCopied
}


///////////////////////////////////////////////////////////////////////////
// 协程的超时取消
///////////////////////////////////////////////////////////////////////////
private suspend fun cancellationAndTimeout() {
/*
    发送网络请求，通常会设置一个超时来应对网络不佳的情况，所有的网络框架（如OkHttp）都会提供这样的参数。如果有一个特定的请求，用户等不了太久，
    比如要求 5s 以内没有响应就要取消，这种情况下就要单独修改网络库的超时配置，但这样做不太方便。为了解决这个问题，我们可以这样做：
 */

    //启动两个子协程，其中一个协程用于请求数据，另一个协程用于设置超时，二者中任何一个成功执行都会取消另一个，最终只有一个可以正常结束。
    GlobalScope.launch {
        val userDeferred = async { getUserSuspend() }
        val timeoutJob = launch {
            delay(5000)
            userDeferred.cancel()
        }
        val user = userDeferred.await()
        timeoutJob.cancel()
        println(user)
    }

    /*
        上面这看上去没什么问题，只是不够简洁，甚至还有些令人迷惑。幸运的是，官方框架提供了一个可以设定超时的 API，我们可以用这个 API 来优化上面的代码，
         1. withTimeout 这个 API 可以设定一个超时，如果它的第二个参数 block 运行超时，那么就会被取消，取消后 withTimeout 直接抛出取消异常。
         2. 如果不希望在超时的情况下抛出取消异常，也可以使用 withTimeoutOrNull，它的效果是在超时的情况下返回 null。
     */
    GlobalScope.launch {
        val user = withTimeout(5000) {
            getUserSuspend()
        }
        println(user)
    }.join()
}

private suspend fun getUserSuspend(): String {
    delay(2000)
    return "Alien"
}

///////////////////////////////////////////////////////////////////////////
// 禁止取消
///////////////////////////////////////////////////////////////////////////
private suspend fun disableCancellation() {
    GlobalScope.launch {
        val job = launch {
            listOf(1, 2, 3, 4).forEach {
                //这段代码本意是希望研究 yield 函数的作用（是否能响应取消），然而在运行过程中，响应协程取消的不一定是 yield 函数，因为 delay 函数自身也可以响应取消，
                //甚至由于它执行时挂起的时间跨度更大，反而非常容易干扰试验结果。
                yield()
                //官方框架为我们提供了一个名为 NonCancellable 的上下文实现，它的作用就是禁止作用范围内的协程被取消。为了确保 delay 函数不响应取消，为其指定 NonCancellable 上下文。
                withContext(NonCancellable) {
                    delay(it * 100L)
                }
            }
        }
        delay(200)
        job.cancelAndJoin()
    }.join()
}
