package analyse

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

suspend fun main() {
    sample1()
    //sample2()
}

private suspend fun sample1() {
    val channel = Channel<String> {
        println("onUndeliveredElement: $it")
    }

    GlobalScope.launch {
        for (i in 1..10) {
            println("send: $i")
            channel.send(i.toString())
            println("sent: $i")
        }
    }

    val job = GlobalScope.launch {
        for (s in channel) {
            delay(100)
            println("receive: $s")
        }
    }

    delay(300)
    channel.close()
    delay(30000)
}

private suspend fun sample2() {
    val channel = Channel<String> {
        println("onUndeliveredElement: $it")
    }

    GlobalScope.launch {
        for (i in 1..10) {
            println("send: $i")
            channel.send(i.toString())
            println("sent: $i")
        }
    }

    val flow1 = channel.consumeAsFlow()

    val subscriberJob1 = GlobalScope.launch {
        flow1
            .onEach {
                delay(100)
            }
            .collect {
                println("1-collect: $it")
            }
    }

    delay(300)
    subscriberJob1.cancel()
    delay(15000)
}