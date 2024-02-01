package book.dukc.ch03_foundation

import analyse.logCoroutine
import kotlinx.coroutines.delay
import kotlin.coroutines.*

/**
 * 《深入理解 Kotlin 协程》chapter 3.1：协程的创建
 */
fun main() {
    /*
        与协程的创建和启动相关的 API 一共有两组：

            1. fun <T> (suspend () -> T).createCoroutine(completion: Continuation<T>): Continuation<Unit>
            2. fun <R, T> (suspend R.() -> T).createCoroutine( receiver: R, completion: Continuation<T> ): Continuation<Unit>

        这两组 API 的差异点仅仅在于协程体自身的类型，第二组 API 的协程体多了一个 Receiver 类型 R。这个 R 可以为协程体提供一个作用域，在协程体内我们可以直接使用作用域内提供的函数或者状态等。
     */

    //第一组 API：协程体不带 Receiver【3.1.1-3.1.2】
    createCoroutine()
    //startCoroutine()

    //第二组 API：协程体带 Receiver【3.1.3】
    //callLaunchCoroutine()

    //main 也支持挂起函数【3.1.3】
    //suspendMan()
}

///////////////////////////////////////////////////////////////////////////
// 协程体不带 Receiver
///////////////////////////////////////////////////////////////////////////

private fun createCoroutine() {
    /*
        createCoroutine 函数：
            参数 completion 会在协程执行完成后调用，实际上就是协程的完成回调。
            返回值是一个 Continuation 对象，由于现在协程仅仅被创建出来，因此需要通过这个值在之后触发协程的启动。
     */
    val continuation = suspend {
        logCoroutine("In coroutine")
        4
    }.createCoroutine(object : Continuation<Int> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext /*+ Dispatchers.Default*/

        override fun resumeWith(result: Result<Int>) {
            logCoroutine("Coroutine End: $result")
        }
    })

    //调用 resume 以启动协程。
    continuation.resume(Unit)
    /*
        为什么调用 continuation 的 resume 就会触发协程体的执行呢？
            1. 通过阅读 createCoroutine 的源码或者直接打断点调试，可以得知 continuation 是 SafeContinuation 的实例，
            2. SafeContinuation 其实只是一个“马甲”。SafeContinuation 一个名为 delegate 的属性，这个属性才是 Continuation 的本体。
            3. delegate 的类名类似 <FileName>Kt$<FunctionName>$continuation$1 这样的形式，与 Java 类似，这其实指代了某一个匿名内部类。
            4. 匿名内部类就是我们的协程体，那个用以创建协程的 suspend lambda 表达式。
            4. Kotlin 编译器在编译器会对我们的 suspend Lambda 做一些处理，生成了一个匿名内部类，这个类继承自 SuspendLambda 类，而这个类又是 Continuation 接口的实现类。
            5. suspend lambda 表达式是如何编译的？一个函数如何对应一个类呢？
                    SuspendLambda 有一个抽象函数 invokeSuspend（这个函数在它的父类 BaseContinuationImpl 中声明），编译生成的匿名内部类中这个函数的实现就是我们的协程体。
     */
}

private fun startCoroutine() {
    //startCoroutine 仅仅是在 createCoroutine 基础上帮我们调用了 Resume 方法。
    suspend {
        println("In coroutine")
        4
    }.startCoroutine(object : Continuation<Int> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            println("Coroutine End: $result")
        }
    })
}

///////////////////////////////////////////////////////////////////////////
// 协程体带 Receiver
///////////////////////////////////////////////////////////////////////////

/*
    1. Kotlin 没有提供直接声明带有 Receiver 的 Lambda 表达式的语法，为了方便使用带有 Receiver 的协程 API，我们封装一个用以启动协程的函数 launchCoroutine。
    2. 多了一个 Receiver 类型 R，可以为协程体提供一个作用域，在协程体内我们可以直接使用作用域内提供的函数或者状态等。
 */
private fun <R, T> launchCoroutine(receiver: R, black: suspend R.() -> T) {
    black.startCoroutine(receiver, object : Continuation<T> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<T>) {
            println("Coroutine End: $result")
        }
    })
}

/*使用时首先创建一个作用域，ProducerScope 用来模拟一个生成器协程的作用域，再使用它来创建协程即可*/
private class ProducerScope<T> {
    suspend fun produce(value: T) {

    }
}

/*
 1. 作用域可以用来提供函数支持，自然也就可以用来增加限制。如果我们为Receiver对应的类型增加一个RestrictsSuspension注解，那么在它的作用下，协程体内就无法调用外部的挂起函数了。
 2. 这里在 RestrictProducerScope 的作用下，协程体内部无法调用外部的挂起函数 delay，这个特性对于不少在特定场景下创建的协程体有非常大的帮助，可以避免无效甚至危险的挂起函数的调用。标准库中的序列生成器（Sequence Builder）就使用了这个注解。
    参考下面在 callLaunchCoroutine 中的调用。
*/
@RestrictsSuspension
private class RestrictsProducerScope<T> {
    suspend fun produce(value: T) {

    }
}

private fun callLaunchCoroutine() {
    launchCoroutine(ProducerScope<Int>()) {
        println("In coroutine.")
        produce(1024)
        delay(1000)
        produce(2048)
    }

    launchCoroutine(RestrictsProducerScope<Int>()) {
        println("In coroutine.")
        produce(1024)
        //因为有 @RestrictsSuspension 标注 RestrictsProducerScope，在这个协程体里只有调用 RestrictsProducerScope 提供给的挂起函数。
        //delay(1000)
        produce(2048)
    }

}

///////////////////////////////////////////////////////////////////////////
//可挂起的 main 函数
///////////////////////////////////////////////////////////////////////////

/*
Kotlin 从 1.3 版本开始添加了一个非常有趣的特性：main 可以直接被声明为挂起函数，只需要在 main 函数的声明前加 suspend 关键字即可。
    1. 这意味着 Kotlin 程序从程序入口处就可以获得一个协程，而我们所有的程序都将在这个协程体里面运行。
    2. 首先可以确定的是这个可挂起的 main 函数并不会是真正的程序入口，JVM 根本不知道什么是 Kotlin 协程，这里一定有“魔法”。
    3. 尝试对可挂起的 main 函数进行反编译，得知其实 Kotlin 编译器无非是帮我们生成了一个真正的 main 函数，里面调用了一个叫作 runSuspend 的函数来执行所谓的可挂起的 main 函数。
 */
private fun suspendMan() {
    // no op
}
