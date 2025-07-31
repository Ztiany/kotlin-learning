package explore.book.dukc.ch03_foundation

import kotlinx.coroutines.delay
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 《深入理解 Kotlin 协程》chapter 3.2：协程的挂起
 */
suspend fun main() {
    //Kotlin 协程的挂起和恢复能力本质上就是挂起函数的挂起和恢复。

    //理解函数挂起的本质【3.2.1】
    suspendFunction()

    //理解挂起点【3.2.2】
    suspendPoint()

    //理解什么是 CPS 变换【【3.2.3】
    cpsTransformation()
}

///////////////////////////////////////////////////////////////////////////
// 挂起函数
///////////////////////////////////////////////////////////////////////////
private suspend fun suspendFunction() {
    //根据是否可挂起，整个 Kotlin 语言体系内的函数就分为两派：普通函数和挂起函数。其中挂起函数可以调用任何函数，普通函数只能调用普通函数。

    // 挂起函数既可以像普通函数一样同步返回（如suspendFunc01）；
    //suspendFunc01(1)

    // 也可以处理异步逻辑（如suspendFunc02）
    suspendFunc02("a", "v")
}

suspend fun suspendFunc01(a: Int) {
    delay(1000)
    return
}

/*
    1. 在 suspendFunc02 的定义中，用到了 suspendCoroutine<T> 获取当前所在协程体的 Continuation<T> 的实例作为参数将挂起函数当成异步函数来处理。
       【suspendCoroutine 用于连接协程与异步函数】
    2. 因此协程调用 suspendFunc02 无法同步执行，会进入挂起状态，直到结果返回。
    3. 所谓协程的挂起其实就是程序执行流程发生异步调用时，当前调用流程的执行状态进入等待状态。【但是注意：挂起函数不一定真的会挂起，只是提供了挂起的条件。】
 */
private suspend fun suspendFunc02(a: String, b: String) = suspendCoroutine<Int> { continuation ->
    thread {
        continuation.resumeWith(Result.success(5))

        suspend fun notSuspend() = suspendCoroutine<Int> { continuation ->
            continuation.resume(100)
        }

    }
}

///////////////////////////////////////////////////////////////////////////
// 挂起点
///////////////////////////////////////////////////////////////////////////
/*
    1. 一个函数想要让自己挂起，所需要的无非就是一个 Continuation 实例，我们也确实可以通过 suspendCoroutine 函数获取到它，但是这个 Continuation 是从哪儿来的呢？
    2. 回想【3.1】中关于协程的创建和运行过程，协程体本身就是一个 Continuation 实例，正因如此挂起函数才能在协程体内运行。
    3. 在协程内部挂起函数的调用处被称为挂起点，挂起点如果出现异步调用，那么当前协程就被挂起，直到对应的 Continuation 的 resume 函数被调用才会恢复执行。
    4. 通过调试可知，通过 suspendCoroutine 函数获得的 Continuation 是一个 SafeContinuation 的实例，与创建协程时得到的用来启动协程的 Continuation 实例没有本质上的差别。
    5. SafeContinuation 类的作用也非常简单，它可以确保只有发生异步调用时才会挂起，如下面 suspendPoint 所示的情况虽然也有 resume 函数的调用，但协程并不会真正挂起。
    6. 异步调用是否发生，取决于resume函数与对应的挂起函数的调用是否在相同的调用栈上。
        6.1 切换函数调用栈的方法可以是切换到其他线程上执行；
        6.2 也可以是不切换线程但在当前函数返回之后的某一个时刻再执行。其实就是先将 Continuation 的实例保存下来，在后续合适的时机再调用【比如 Android 中的 Handler】
 */
private suspend fun suspendPoint() = suspendCoroutine<Int> { continuation ->
    continuation.resume(100)
}

///////////////////////////////////////////////////////////////////////////
// CPS 变换：CPS 变换（Continuation-Passing-Style Transformation），是通过传递 Continuation 来控制异步调用流程。
///////////////////////////////////////////////////////////////////////////

suspend fun notSuspend() = suspendCoroutine<Int> { continuation ->
    continuation.resume(100)
}

private suspend fun cpsTransformation() {
/*
    程序被挂起时，最关键的是要做什么？是保存挂起点。
        对于线程：它被中断时，中断点就是被保存在调用栈中的。
        对于协程：将挂起点的信息保存到了 Continuation 对象中。

    1. Continuation 携带了协程继续执行所需要的上下文，恢复执行的时候只需要执行它的恢复调用并且把需要的参数或者异常传入即可。
    2. 作为一个普通的对象，Continuation占用内存非常小，这也是无栈协程能够流行的一个重要原因。
    3. 挂起函数如果需要挂起，则需要通过 suspendCoroutine 来获取 Continuation 实例。我们已经知道它是协程体，但是这个实例是怎么传进来的呢？
        3.1 直接看 Kotlin 代码看不出来。
        3.2 想要刨根问底有两种方法：一种就是看字节码或者使用 Java 代码直接调用它，另一种就是使用 Kotlin 反射。
    4. 参考 JavaInvokeSuspend 中的代码，发现上面 notSuspend 在 Java 语言看来实际上是 (Continuation<Integer>)->Object 类型，
       这正好与我们经常写的异步回调的方法类似，传一个回调进去等待结果返回就好了。
    5. 问题在于，(Continuation<Integer>)->Object 类型是有返回值的，为什么要有返回值 Object？因为通常我们写的回调方法是不会有返回值的，其实这里的返回值 Object 有两种情况。
            5.1 情况1：挂起函数同步返回。作为参数传入的 Continuation 的 resumeWith 不会被调用，函数的实际返回值就是它作为挂起函数的返回值。
                notSuspend 尽管看起来似乎调用了 resumeWith，不过调用对象是 SafeContinuation，这一点在前面已经多次提到，因此它的实现属于同步返回。
            5.2 情况2：挂起函数挂起，执行异步逻辑。此时函数的实际返回值是一个挂起标志，通过这个标志外部协程就可以知道该函数需要挂起等到异步逻辑执行。
                在 Kotlin 中这个标志是个常量为 COROUTINE_SUSPENDED，定义在 Intrinsics.kt 当中。
 */

    //使用  Kotlin 反射找到挂起函数的原理。
    val ref = ::notSuspend
    val result = ref.call(object : Continuation<Int> {
        override val context = EmptyCoroutineContext
        override fun resumeWith(result: Result<Int>) {
            println("resumeWith: ${result.getOrNull()}")
        }
    })
}