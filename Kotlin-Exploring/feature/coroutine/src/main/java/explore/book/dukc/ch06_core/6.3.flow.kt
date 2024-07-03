package explore.book.dukc.ch06_core

import common.logCoroutine
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.collect
import java.util.concurrent.Executors

/**
 * 《深入理解Kotlin协程》chapter 6.3：flow
 */
suspend fun main() {
    //随着 RxJava 的流行，响应式编程模型逐步深入人心。Flow 就是 Kotlin 协程与响应式编程模型结合的产物。

    //认识 flow【6.3.1】
    //restrictionOfSequence()
    //getStartedOfFlow()

    //flow 的线程切换【6.3.2】
    switchingThread()

    //冷的 flow【6.3.3】
    //coldFlow()

    //flow 的异常处理【6.3.4】
    //flowExceptionHandling()

    //flow 的末端操作符【6.3.5】
    //endOperator()

    //分离 Flow 的消费和触发【6.3.6】
    //separateCreatingAndConsuming()

    //取消 Flow【6.3.7】
    //theCancellationOfFlow()

    //其他 Flow 的创建方式【6.3.8】
    theBuildersOfFlow()

    //Flow 的背压【6.3.9】
    //theBackPressureOfFlow()

    //Flow 的变换【6.3.10】
    //theTransformationOfFlow()
}

///////////////////////////////////////////////////////////////////////////
//认识 flow
///////////////////////////////////////////////////////////////////////////
private fun restrictionOfSequence() {
    //Builds a Sequence lazily yielding values one by one.
    val ints = sequence {
        (1..3).forEach {
            //受 RestrictsSuspension 注解的约束，delay 函数不能在 SequenceScope 的扩展成员中被调用，因而也不能在序列生成器的协程体内调用【参见 3.1.3 和 4.1.2】。
            //delay(1000)
            yield(it)
        }
    }
    println(ints.first())
}

private suspend fun getStartedOfFlow() {
    //Flow 的 API 与序列生成器极为相似，但是没有 Sequence 这么多的限制。
    flow {
        (1..3).forEach {
            emit(it)
            delay(100)
        }
    }
            //Flow 也可以设定它运行时所使用的调度器：
            .flowOn(Dispatchers.IO)
            //最终消费 intFlow 需要调用 collect 函数，这个函数也是一个挂起函数：
            .collect {
                println(it)
            }
}

///////////////////////////////////////////////////////////////////////////
// flow 的线程切换 VS RxJava 的线程切换
///////////////////////////////////////////////////////////////////////////
private suspend fun switchingThread() {
/*
    1. RxJava 也是一个基于响应式编程模型的异步框架，它提供了两个切换调度器的 API，分别是 subscribeOn 和 observeOn。
    2. RxJava 的两个切换调度器 API 中，subscribeOn 指定的调度器影响前面的逻辑，observeOn 影响后面的逻辑。
    3. Flow 的调度器 API 中看似只有 flowOn 与 subscribeOn 对应，其实不然，还有 collect 函数所在协程的调度器与 observeOn 指定的调度器对应。
        flowOn 对应 subscribeOn
        collect 对应 observeOn【因为 collect 是一个挂起函数，有自己的调度器，不再需要类似 observeOn 的 API 来特意指定】
 */
    withContext(Executors.newFixedThreadPool(1).asCoroutineDispatcher()) {
        logCoroutine("1")
        flow {
            emit(1)
            logCoroutine("2")
        }.map {
            logCoroutine("3")
            it
        }.flowOn(Dispatchers.IO).map {
            logCoroutine("4")
            it
        }.map {
            logCoroutine("5")
            it
        }.collect {
            logCoroutine("6")
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// 冷的 flow：所谓冷数据流，就是只有消费时才会生产的数据流，这一点与 Channel 正好相反，Channel 的发送端并不依赖于接收端。
///////////////////////////////////////////////////////////////////////////
private suspend fun coldFlow() {
    //和 RxJava 一样，默认的 flow 也是冷的，即在一个 Flow 创建出来之后，不消费则不生产，多次消费则多次生产，生产和消费总是一一相对应的。
    //Flow 的 API 与序列生成器极为相似，但是没有 Sequence 这么多的限制。
    val intFlow = flow {
        (1..3).forEach {
            println("creating $it")
            emit(it)
            delay(100)
        }
    }

    intFlow.flowOn(Dispatchers.IO)
            .collect {
                println("consuming $it")
            }

    intFlow.flowOn(Dispatchers.IO)
            .collect {
                println("consuming $it")
            }
}

///////////////////////////////////////////////////////////////////////////
// flow 的异常处理
///////////////////////////////////////////////////////////////////////////
private suspend fun flowExceptionHandling() {
    //正确的异常处理写法
    flow {
        emit(1)
        throw ArithmeticException("Div 0")
    }
            //Flow 的异常处理也比较直接，直接调用 catch 函数即可
            .catch { t: Throwable ->
                //在 RxJava 中还有与 onErrorReturn 这样的操作，在 Flow 当中也可以模拟，直接在 catch 中继续 emit 即可。
                println("caught error: $t")
                emit(10)
            }
            //onCompletion用起来类似于try...catch...finally中的finally，无论前面是否存在异常，它都会被调用，参数t则是前面未捕获的异常。
            .onCompletion {
                println("onCompletion")
            }
            .collect {
                println(it)
            }

    //不推荐的方式：在 Flow 操作内部使用 try...catch...finally，这样的写法后续可能会被禁用。
    flow {
        try {
            emit(1)
            throw ArithmeticException("Div 0")
        } catch (t: Throwable) {
            println("tcf: caught error: $t")
        } finally {
            println("tcf: onCompletion")
        }
    }.collect {
        println("tcf: $it")
    }
}

///////////////////////////////////////////////////////////////////////////
// flow 的末端操作符
///////////////////////////////////////////////////////////////////////////
private suspend fun endOperator() {
/*
    collect 是最基本的末端操作符，功能与 RxJava 的 subscribe 类似。除了 collect 之外，还有其他常见的末端操作符，它们大体分为两类：
        1. 集合类型转换操作符，包括 toList、toSet 等。
        2. 聚合操作符，包括将 Flow 规约到单值的 reduce、fold 等操作；还有获得单个元素的操作符，包括 single、singleOrNull、first 等。

   识别是否为末端操作符，还有一个简单方法：由于 Flow 的消费端一定需要运行在协程中，因此末端操作符都是挂起函数。
 */
    val intFlow = flow {
        (1..3).forEach {
            println("creating $it")
            emit(it)
            delay(100)
        }
    }

    intFlow.toList().run {
        println(this)
    }
}

///////////////////////////////////////////////////////////////////////////
// Flow 的消费和触发
///////////////////////////////////////////////////////////////////////////
private suspend fun separateCreatingAndConsuming() {
    //可以在 collect 处消费 Flow 的元素以外，还可以通过 onEach 来做到这一点。这样消费的具体操作就不需要与末端操作符放到一起，collect 函数可以放到其他任意位置调用。
    fun createFlow() = flow {
        (1..3).forEach {
            emit(it)
            delay(100)
        }
    }.onEach { println(it) }

    createFlow().collect()
}

///////////////////////////////////////////////////////////////////////////
// Flow 的取消
///////////////////////////////////////////////////////////////////////////
private suspend fun theCancellationOfFlow() {
/*
    Flow 没有提供取消操作，因为并不需要。具体原因是：
        Flow 的消费依赖于 collect 这样的末端操作符，而它们又必须在协程中调用，因此 Flow 的取消主要依赖于末端操作符所在的协程的状态。
 */
    val job = GlobalScope.launch {
        val intFlow = flow {
            (1..3).forEach {
                delay(1000)
                emit(it)
            }
        }
        intFlow.collect { println(it) }
    }

    delay(2500)

    job.cancelAndJoin()
}

///////////////////////////////////////////////////////////////////////////
// 其他 Flow 的创建方式
///////////////////////////////////////////////////////////////////////////
private suspend fun theBuildersOfFlow() {
    //上面讲到 flow{...} 这种形式的创建方式，不过在这当中无法随意切换调度器，这是因为 emit 函数不是线程安全的。
    flow {
        emit(1)
        /*
            错误的做法：
                Flow was collected in EmptyCoroutineContext,
		        but emission happened in [DispatchedCoroutine{Active}@77ef055f, Dispatchers.IO].
		        Please refer to 'flow' documentation or use 'flowOn' instead
         */
        /*withContext(Dispatchers.IO) {
            emit(2)
        }*/
    }.collect {
        println(it)
    }

    //想要在生成元素时切换调度器，就必须使用 channelFlow 函数来创建 Flow：
    channelFlow {
        send(1)
        launch {
            send(2)
        }
    }.collect {
        println(it)
    }
}

///////////////////////////////////////////////////////////////////////////
// Flow 的背压：只要是响应式编程，就一定会有背压问题。
///////////////////////////////////////////////////////////////////////////
private suspend fun theBackPressureOfFlow() {

    //背压问题在生产者的生产速率高于消费者的处理速率的情况下出现。为了保证数据不丢失，我们也会考虑添加缓冲来缓解背压问题。
    flow { List(100) { emit(it) } }.buffer()
    //我们可以为 buffer 指定一个容量。不过，如果只是单纯地添加缓冲，而不是从根本上解决问题，就会造成数据积压。
    flow { List(100) { emit(it) } }.buffer(100)

    //出现背压问题的根本原因是生产和消费速率不匹配，此时除可直接优化消费者的性能以外，还可以采用一些取舍的手段。

    //第一种是 conflate。与 Channel 的 Conflate 模式一致，新数据会覆盖老数据。
    flow {
        List(100) {
            emit(it)
        }
    }.conflate().collect { value ->
        println("Collecting $value")
        delay(100)
        println("$value collected")
    }

    // 第二种是 collectLatest。顾名思义，其只处理最新的数据。这看上去似乎与 conflate 没有区别，其实区别很大：
    // collectLatest 并不会直接用新数据覆盖老数据，而是每一个数据都会被处理，只不过如果前一个还没被处理完后一个就来了的话，处理前一个数据的逻辑就会被取消。
    flow {
        List(100) {
            emit(it)
        }
    }.collectLatest { value ->
        println("Collecting $value")
        delay(100)
        println("$value collected")
    }

    //除 collectLatest 之外，还有 mapLatest、flatMapLatest 等，使用方法与 collectLatest 类似。
}

///////////////////////////////////////////////////////////////////////////
// Flow 的变换
///////////////////////////////////////////////////////////////////////////
private suspend fun theTransformationOfFlow() {
    //Flow 看上去与集合框架极其类似，这一点与 RxJava 的 Observable 的表现基本一致。
    flow {
        List(5) { emit(it) }
    }.map {
        it * 2
    }.collect {
        println("Collecting $it")
    }

    flow { List(5) { emit(it) } }.map { flow { List(it) { emit(it) } } }.collect {
        it.collect {
            println("Collecting-$it")
        }
    }

    //实际上，上面代码得到的是一个数据类型为 Flow 的 Flow，如果希望将它们拼接起来，除了手动收集外，还可以使用 flattenConcat。
    //flattenConcat 是按顺序拼接的，结果的顺序仍然是生产时的顺序。此外，我们还可以使用 flattenMerge 进行会并发拼接，但得到的结果不会保证顺序与生产是一致。
    flow { List(5) { emit(it) } }.map { flow { List(it) { emit(it) } } }.flattenConcat().collect { println(it) }
}
