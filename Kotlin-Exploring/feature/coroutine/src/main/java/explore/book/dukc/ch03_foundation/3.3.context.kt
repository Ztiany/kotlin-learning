package explore.book.dukc.ch03_foundation

import kotlin.coroutines.*

/**
 * 《深入理解 Kotlin 协程》chapter 3.2：协程上下文
 */
fun main() {
    //Continuation除了可以通过恢复调用来控制执行流程的异步返回以外，还有一个重要的属性 context，即协程的上下文。

    //协程上下文的集合特征【3.3.1】
    //collectionFeatureOfContext()

    //协程上下文元素的实现【3.3.2】
    //theImplementationOfCollectionFeature()

    //协程上下文的使用【3.3.3】
    theUsageOfContext()
}

///////////////////////////////////////////////////////////////////////////
// 协程上下文的集合特征
///////////////////////////////////////////////////////////////////////////

/*
    1. 上下文很容易理解，也很常见，例如 Android 中的 Context，Spring 中的 ApplicationContext，它们在各自的场景下主要承载了资源获取、配置管理等工作，是执行环境相关的通用数据资源的统一提供者。
    2. 协程的上下文也是如此，它的数据结构的特征甚至更加显著，其实现与 List、Map 这些我们熟悉的集合非常类似。
    3. List 没有元素的时候是个空 List，同样对应的也有 EmptyCoroutineContext，EmptyCoroutineContext 是一个标准库已经定义好的 object，表示一个空的协程上下文，里面没有数据。
    4. 使用 List 时会定义 List 中的元素类型，协程上下文作为一个集合，它的元素类型是 Element。
    5. Element 的定义：interface Element : CoroutineContext { public val key: Key<*> ... // 省略部分逻辑 }
        5.1 Element 本身也实现了 CoroutineContext 接口，这看上去就好像 Int 实现了 List<Int> 接口一样，这很奇怪，为什么元素本身也是集合了呢？其实这主要是为了 API 设计方便，Element 中是不会存放除了它自己以外的其他数据的。
        5.2 Element接口中有一个属性 key，这个属性很关键。List 中的元素都有自己的索引，类似的，这里协程上下文元素的 key 就是协程上下文这个集合中元素的索引。
    6. Element 的不同之处是：这个索引“长”在了数据里面【定义在 Element 中】，这意味着协程上下文的数据在“出生”时就找到了自己的位置。

    上面为什么用 List 类比 Context 呢？
        1. List 的 Key 类型是固定的 Int，Map 的 Key 类型可以有多种【而 Context 的 Key 也是固定类型的】
        2. 协程上下文的内部实现实际上是一个单链表，这也正反映出它与 List 之间的关系。
 */
private fun collectionFeatureOfContext() {
    val emptyList = listOf<Int>()
    val context = EmptyCoroutineContext
}

///////////////////////////////////////////////////////////////////////////
// 协程上下文元素的实现
///////////////////////////////////////////////////////////////////////////

/*
    1. 上面说到，协程具有集合特征，因此可以给协程上下文添加一些数据。
    2. AbstractCoroutineContextElement 实现了 CoroutineContext，能让我们在实现协程上下文的元素时更加方便。
 */
fun theImplementationOfCollectionFeature() {

}

/*自定义上下文：允许我们在启动协程时安装一个统一的异常处理器*/
private class CoroutineExceptionHandler(val onErrorAction: (Throwable) -> Unit) : AbstractCoroutineContextElement(Key) {

    companion object Key : CoroutineContext.Key<CoroutineExceptionHandler>

    fun onError(error: Throwable) {
        error.printStackTrace()
        onErrorAction(error)
    }
}

/*自定义上下文：用于给协程加上名字*/
private class CoroutineName(val name: String) : AbstractCoroutineContextElement(Key) {
    companion object Key : CoroutineContext.Key<CoroutineName>
}

///////////////////////////////////////////////////////////////////////////
// 协程上下文的使用
///////////////////////////////////////////////////////////////////////////

/*
    从上面协程上下文的定义可以看出，不同的上下文可以有不同的功能，协程能够通过绑定一个上下文来设置一些数据来丰富协程的能力。

    因此，协程上下文的使用体系在两方面：
        1. 在启动协程时，如何组合多个协程上下文。
        2. 在协程运行时，如何获取特定的上下文。
 */
private fun theUsageOfContext() {
    var coroutineContext: CoroutineContext = EmptyCoroutineContext

    //使用 + 和 += 操作符就可以组合多个协程上下文
    coroutineContext += CoroutineName("co-01")
    coroutineContext += CoroutineExceptionHandler {
        println(it)
    }

    coroutineContext += CoroutineName("co-01") + CoroutineExceptionHandler {
        println(it)
    }

    //在协程的运行时，使用 [] 操纵符就可以获取特定的上下文。
    suspend {
        println("In Coroutine [${coroutineContext[CoroutineName]}].")
        throw ArithmeticException()
        100
    }.startCoroutine(object : Continuation<Int> {

        override val context = coroutineContext

        override fun resumeWith(result: Result<Int>) {
            result.onFailure {
                context[CoroutineExceptionHandler]?.onError(it)
            }.onSuccess {
                println("Result $it")
            }
        }
    })
}
