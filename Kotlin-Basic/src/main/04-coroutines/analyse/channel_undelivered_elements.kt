package analyse

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

private val scope = CoroutineScope(SupervisorJob())

suspend fun main() {
    //sample1()
    //sample2()
    sample3()
}

private suspend fun sample1() {
    val channel = Channel<String> {
        println("There is a undelivered Element: $it")
    }

    scope.launch {
        for (i in 1..10) {
            println("send: $i")
            channel.send(i.toString())
            println("sent: $i")
        }
    }

    scope.launch {
        for (s in channel) {
            delay(100)
            println("receive: $s")
        }
    }

    delay(300)
    channel.close()
    delay(30000)
}

// 会有元素丢失
private suspend fun sample2() {
    val channel = Channel<String> {
        println("Find a undeliveredElement: $it")
    }

    scope.launch {
        for (i in 1..10) {
            println("send: $i")
            channel.send(i.toString())
            println("sent: $i")
        }
    }

    val flow = channel.receiveAsFlow()

    val subscriberJob1 = scope.launch {
        flow.onEach {
                delay(200)
            }
            .collect {
                println("1-collect: $it")
            }
    }

    delay(500)
    subscriberJob1.cancel()

    delay(1000)
    val subscriberJob2 = scope.launch {
        flow.onEach {
            delay(200)
        }
            .collect {
                println("2-collect: $it")
            }
    }
    delay(500)
    subscriberJob2.cancel()

    delay(15000)
}

// 会触发 undeliveredElement
private suspend fun sample3() {
    val channel = Channel<String> {
        println("There is  a undeliveredElement: $it")
    }

    scope.launch {
        for (i in 1..10) {
            println("send: $i")
            channel.send(i.toString())
            println("sent: $i")
        }
    }

    val flow = channel.consumeAsFlow()

    val subscriberJob = scope.launch {
        flow.onEach {
            delay(200)
        }
            .collect {
                println("1-collect: $it")
            }
    }

    delay(500)
    subscriberJob.cancel()

    delay(15000)
}