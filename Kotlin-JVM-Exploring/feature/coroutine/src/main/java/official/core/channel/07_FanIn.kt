package official.core.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel

/*
多个协程可以发送到同一个通道。 比如说，让我们创建一个字符串的通道，和一个在这个通道中以指定的延迟反复发送一个指定字符串的挂起函数：
 */
fun main() = runBlocking {
    //sampleStart
    val channel = Channel<String>()

    launch { sendString(channel, "foo", 200L) }
    launch { sendString(channel, "BAR!", 500L) }

    repeat(6) {
        // receive first six
        println(channel.receive())
    }

    coroutineContext.cancelChildren() // cancel all children to let me.ztiany.tools.main finish
//sampleEnd
}

suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
    while (true) {
        delay(time)
        channel.send(s)
    }
}