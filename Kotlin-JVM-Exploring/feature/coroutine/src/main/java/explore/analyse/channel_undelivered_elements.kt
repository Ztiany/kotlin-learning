package explore.analyse

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

private val scope = CoroutineScope(SupervisorJob())

suspend fun main() {
    //  sample1()
     sample2()
    //sample3()
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
    val channel = Channel<String>(3) {
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
    println("1----------------------------------------------------------------------------")
    delay(5000)

    val subscriberJob1 = scope.launch {
        flow.onEach {
            delay(200)
        }.collect {
            println("1-collect: $it")
        }
    }

    delay(500)
    subscriberJob1.cancel()
    println("2----------------------------------------------------------------------------")

    delay(5000)
    val subscriberJob2 = scope.launch {
        flow.onEach {
            delay(200)
        }.collect {
            println("2-collect: $it")
        }
    }

    println("3----------------------------------------------------------------------------")
    delay(500)
    subscriberJob2.cancel()

    /*channel.consumeEach {
        delay(200)
        println("consume: $it")
    }
    channel.close()*/

    println("4----------------------------------------------------------------------------")
    delay(15000)
    println("5----------------------------------------------------------------------------")
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
        }.collect {
            println("1-collect: $it")
        }
    }

    delay(500)
    subscriberJob.cancel()

    // Channel is closed, so the consumeEach call will cause Exception.
    /*channel.consumeEach {
        delay(200)
        println("consume: $it")
    }*/

    delay(15000)
}