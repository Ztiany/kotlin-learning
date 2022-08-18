package book.dukc.ch06_core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS


/**
 * 《深入理解Kotlin协程》chapter 6.2：热数据通道 Channel
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 2020/9/25 10:08
 */
suspend fun main() {
    //Channel：Channel 实际上就是一个并发安全的队列，它可以用来连接协程，实现不同协程的通信。

    //认识 Channel【6.2.1】
    //getStartedWithChannel()

    //Channel 的容量【6.2.2】
    //theCapacity()

    //迭代 Channel【6.2.3】
    //iterateChannel()

    //produce 和 actor【6.2.4】
    //produceAndActor()

    //Channel 的关闭【6.2.5】
    //closeChannel()

    //BroadcastChannel【6.2.6】【obsolete】
    //theBroadcastChannel()

    //Channel 版本的序列生成器【6.2.7】
    //sequenceGeneratorByChannel()

    //Channel 的内部结构【6.2.8】
    theInnerStructureOfChannel()
}

///////////////////////////////////////////////////////////////////////////
// 认识 Channel
///////////////////////////////////////////////////////////////////////////
private suspend fun getStartedWithChannel() {
    //下面代码，构造了两个协程 producer 和 consumer，没有为它们明确指定调度器，所以它们的调度器都是默认的，在 Java 平台上就是基于线程池实现的Default。
    //它们可以运行在不同的线程上，也可以运行在同一个线程上。producer 每隔 1s 向 Channel 发送一个数字，而 consumer 一直在读取 Channel 来获取这个数字并打印，
    //显然发送端比接收端更慢。在没有值可以读到的时候，receive 是挂起的，直到有新元素到达。这么看来，receive 一定是一个挂起函数、那么 send 呢？
    val channel = Channel<Int>()
    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            delay(1000)
            channel.send(i++)
        }
    }
    val consumer = GlobalScope.launch {
        while (true) {
            val element = channel.receive()
            println(element)
        }
    }
    producer.join()
    consumer.join()
}

///////////////////////////////////////////////////////////////////////////
// Channel 的容量
///////////////////////////////////////////////////////////////////////////
private suspend fun theCapacity() {
/*
    send 其实也是挂起函数。发送端为什么会挂起？以我们熟知的 BlockingQueue 为例，当我们往其中添加元素的时候，元素在队列里实际上是占用了空间的。
    如果这个队列空间不足，那么再往其中添加元素的时候就会出现两种情况：

        ·阻塞，等待队列腾出空间。
        ·抛异常，拒绝添加元素。

    send 也会面临同样的问题。Channel 实际上就是一个队列，队列中一定存在缓冲区，那么一旦这个缓冲区满了，并且也一直没有人调用 receive 并取走元素，send 就需要挂起，等待接收者取走数据之后再写入 Channel。

    Channel 缓冲区的定义：
        1. 构造 Channel 的时候调用了一个名为 Channel 的函数，虽然两个“Channel”看起来是一样的，但它却确实不是 Channel 的构造函数。
        2. 在 Kotlin 中我们经常定义一个顶级函数来伪装成同名类型的构造器，这本质上就是工厂函数。
        3. Channel 函数有一个参数叫 capacity，该参数用于指定缓冲区的容量，RENDEZVOUS 默认值为 0。

    capacity 缓冲区有如下选项：
        1. RENDEZVOUS 的本意就是描述“不见不散”的场景，如果不调用 receive，send 就会一直挂起等待。
           换句话说，getStartedWithChannel 实例中，如果 consumer 不调用 receive，producer 里面的第一个 send 就挂起了，
        2. UNLIMITED 比较好理解，其来者不拒，从它给出的实现类 LinkedListChannel 来看，这一点与 LinkedBlockingQueue 有异曲同工之妙。
        3. CONFLATED 的字面意思是合并，那是不是这边发 1、2、3、4、5，那边就会收到一个 [1,2,3,4,5] 的集合呢？实际上这个函数的效果是只保留最后一个元素，
           所以这不是合并而是置换，即这个类型的 Channel 只有一个元素大小的缓冲区，每次有新元素过来，都会用新的替换旧的，也就是说发送端发送了 1、2、3、4、5 之后接收端才接收的话，那么只会收到 5。
        4. 剩下的就是 ArrayChannel了，它接收一个值作为缓冲区容量的大小，效果类似于 ArrayBlockingQueue。
 */
    val channel = Channel<Int>()

    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            delay(1000)
            i++
            //为了方便输出，我们将自增放到前面
            println("before send $i")
            channel.send(i)
            println("after send $i")
        }
    }

    val consumer = GlobalScope.launch {
        while (true) {
            delay(2000) //receive之前延迟2s
            val element = channel.receive()
            println("receive $element")
        }
    }

    producer.join()
    consumer.join()
}


///////////////////////////////////////////////////////////////////////////
// 迭代 Channel
///////////////////////////////////////////////////////////////////////////
private fun iterateChannel() {
/*
    上面，我们在发送和读取 Channel 的时候用了 while(true)，因为我们想要不断地进行读写操作。
    Channel 本身确实有些像序列，可以依次读取，所以我们在读取的时候也可以直接获取一个 Channel 的 iterator。
 */
    val channel = Channel<Int>()
    val consumer1 = GlobalScope.launch {
        val iterator = channel.iterator()
        //iterator.hasNext() 是挂起函数，在判断是否有下一个元素的时候就需要去 Channel 中读取元素了。
        while (iterator.hasNext()) {
            val element = iterator.next()
            println(element)
            delay(2000)
        }
    }

    //也可以写成 for in 的方式
    val consumer2 = GlobalScope.launch {
        for (element in channel) {
            println(element)
            delay(2000)
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// produce 和 actor
///////////////////////////////////////////////////////////////////////////
private fun produceAndActor() {
/*
    produce 和 actor 是 Kotlin 提供的便捷的办法，用于构造生产者和消费者。

        1. ReceiveChannel 和 SendChannel都是 Channel 的父接口，前者定义了 receive，后者定义了 send，Channel 也因此既可以使用 receive 又可以使用 send。
        2. 与 launch 一样，produce 和 actor 也都被称为协程构造器。通过这两个协程构造器启动的协程也与返回的 Channel 自然地绑定到了一起，因此在协程结束时返回的 Channel 也会被立即关闭。
 */

    //以 produce 为例，它构造出了一个 ProducerCoroutine 对象【查看源码】，该对象也是 Job 的实现类之一。
    //注意，在协程完成和取消的方法调用中，对应的 _channel 都会被关闭。
    val receiveChannel: ReceiveChannel<Int> = GlobalScope.produce {
        repeat(100) {
            delay(1000)
            send(it)
        }
    }

    val sendChannel: SendChannel<Int> = GlobalScope.actor<Int> {
        while (true) {
            val element = receive()
            println(element)
        }
    }

/*
    produce 和 actor 这两个构造器看上去都很有用，不过目前前者仍被标记为 Experimental-CoroutinesApi，后者则被标记为 ObsoleteCoroutinesApi，后续仍然可能会有较大的改动。
    actor 的文档中提到的 issue#87（https://github.com/Kotlin/kotlinx.coroutines/issues/87）也说明，
    相比基于 actor 模型的并发框架，Kotlin协程提供的这个 actor API 不过就是一个 SendChannel 的返回值而已，功能相对简单，仍需要进一步设计和完善。
 */
}


///////////////////////////////////////////////////////////////////////////
// Channel的关闭
///////////////////////////////////////////////////////////////////////////
private suspend fun closeChannel() {
/*
    produce 和 actor 返回的 Channel 都会随着对应的协程执行完毕而关闭，可见，Channel 还有一个关闭的概念。也正是这样，Channel 才被称为热数据流。
        1. 对于一个Channel，如果我们调用了它的 close 方法，它会立即停止接收新元素，也就是说这时候它的 isClosedForSend 会立即返回 true。
        2. 而由于 Channel 缓冲区的存在，这时候可能还有一些元素没有被处理完，因此要等所有的元素都被读取之后 isClosedForReceive 才会返回 true。
 */
    val channel = Channel<Int>(3)

    val producer = GlobalScope.launch {
        List(3) {
            channel.send(it)
            println("send $it")
        }
        channel.close()
        println("""close channel. | - ClosedForSend: ${channel.isClosedForSend} | - ClosedForReceive: ${channel.isClosedForReceive}""".trimMargin())
    }

    val consumer = GlobalScope.launch {
        for (element in channel) {
            println("receive $element")
            delay(1000)
        }
        println("""After Consuming. | - ClosedForSend: ${channel.isClosedForSend} | - ClosedForReceive: ${channel.isClosedForReceive}""".trimMargin())
    }

    producer.join()
    consumer.join()

/*
    Channel 关闭的意义：一说到关闭，我们很容易想到 I/O，如果不关闭 I/O 可能会造成资源泄露。那么 Channel 的关闭有什么意义呢？
        前面我们提到过，Channel 内部的资源其实就是个缓冲区，如果我们创建一个 Channel 而不去关闭它，
        虽然并不会造成系统资源的泄露，但却会让接收端一直处于挂起等待的状态，因此一定要在适当的时机关闭 Channel。

    那么 Channel 的关闭究竟应该由谁来处理呢？
        1. 单向的通信过程例如领导讲话，其讲完后会说“我讲完了，散会”，但我们不能在领导还没讲完的时候就说“我听完了，我走了”，这时比较推荐由发送端处理关闭；
        2. 而对于双向通信的情况，就要考虑协商了，双向通信从技术上来说两端是对等的，但业务场景下通常不是，建议由主导的一方实现关闭。
        3. 此外还有一些复杂的情况，前面我们看到的例子都是一对一地进行收发，其实还有一对多、多对多的情况，在这些情况中仍然存在主导的一方，Channel 的生命周期最好由主导方来维护。
 */
}

///////////////////////////////////////////////////////////////////////////
// BroadcastChannel
///////////////////////////////////////////////////////////////////////////
private fun theBroadcastChannel() {
    //发送端和接收端在 Channel 中存在一对多的情形，从数据处理本身来讲，虽然有多个接收端，但是同一个元素只会被一个接收端读到。广播则不然，多个接收端不存在互斥行为。

    // This API is obsolete since 1.5.0. It will be deprecated with warning in 1.6.0 and with error in 1.7.0. It is replaced with SharedFlow.
}

///////////////////////////////////////////////////////////////////////////
// Channel 版本的序列生成器
///////////////////////////////////////////////////////////////////////////
private suspend fun sequenceGeneratorByChannel() {
    //在 4.1.2 节中讲到过序列的生成器，它是基于标准库的协程的 API 实现的，实际上 Channel 本身也可以用来生成序列：
    //produce 创建的协程返回了一个缓冲区大小为 0 的 Channel，为了问题描述起来比较容易，我们传入了一个 Dispatchers.Unconfined 调度器，
    //这意味着协程会立即在当前线程执行到第一个挂起点，所以会立即输出 A 并在 send(1) 处挂起。
    val channel = GlobalScope.produce(Dispatchers.Unconfined) {
        println("A")
        send(1)
        println("B")
        send(2)
        println("Done")
    }
    //后面的 for 循环读到第一个值时，实际上就是调用 channel.iterator.hasNext()，这个 hasNext 函数是一个挂起函数，它会检查是否有下一个元素，
    //在检查的过程中会让前面启动的协程从 send(1) 挂起的位置继续执行，因此会看到B输出，然后再挂起到 send(2) 处，这时候 hasNext 结束挂起，for 循环输出第一个元素，以此类推。
    for (item in channel) {
        println("Got $item")
    }

    //这里 B 居然比 Got 1 先输出，同样，Done 也比 Got 2 先输出，这看上去不太符合直觉，不过挂起恢复的执行顺序确实如此，
    //关键点就是我们前面提到的 hasNext 方法会挂起并触发协程内部从挂起点继续执行的操作。如果你选择了其他调度器，也会有其他合理的输出结果。

    //不管怎么样，我们体验了用 Channel 模拟序列生成器。如果将类似的代码换为标准库的序列生成器，则可得到代码清单如下：
    val sequence = sequence {
        println("A")
        yield(1)
        println("B")
        yield(2)
        println("Done")
    }
    println("before sequence")
    for (item in sequence) {
        println("Got $item")
    }
    //sequence 函数的执行顺序要直观得多，它没有调度器的概念，而且生成的 sequence 对象的 iterator 的 hasNext 和 next 都不是挂起函数，
    //只是在 hasNext 的时候会触发下一个元素的查找，并触发序列生成器内部逻辑的执行。
    //因此，实际上是先触发了 hasNext 才会输出 A，yield 把 1 传出作为序列的第一个元素，这样就会输出 Got 1。

    //标准库的序列生成器本质上就是基于标准库的简单协程实现的，没有官方协程框架提供的复合协程的相关概念。正因为如此，我们可以在 Channel 的例子里面切换不同的调度器来生成元素，但在 sequence 函数中就不行了。

    //下面用 Channel 模拟序列生成器并切换调度器
    val channel2 = GlobalScope.produce(Dispatchers.Unconfined) {
        println("A")
        send(1)
        withContext(Dispatchers.IO) {
            println("B")
            send(2)
        }
        println("Done")
    }
    //当然，实践中我们不会直接将 Channel 当作序列生成器使用，但这个思路非常有意义。Channel 也可以被用来构造 Flow，后者在形式上更加类似于序列生成器。
}

///////////////////////////////////////////////////////////////////////////
// Channel 的内部结构
///////////////////////////////////////////////////////////////////////////
private suspend fun theInnerStructureOfChannel() {
/*
    上面说到的序列生成器无法使用更上层的复合协程的各种能力，除此之外，序列生成器也不是线程安全的，而 Channel 却可以在并发场景下使用。
    而支持 Channel 胜任并发场景的是其内部的数据结构。
 */
}

