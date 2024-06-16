package analyse

import kotlinx.coroutines.*
import common.logCoroutine

suspend fun main() {
    logCoroutine("aaaaa")
    val deferred = GlobalScope.async {
        logCoroutine("bbbbb")
        delay(100)
        logCoroutine("ccccc")
        delay(100)
        logCoroutine("ggggg")
        delay(100)
        logCoroutine("fffff")
        delay(100)
        logCoroutine("ooooo")
        123
    }
    logCoroutine(deferred.javaClass)
    logCoroutine("ddddd")
    val result = deferred.await()
    logCoroutine("eeeee")
    logCoroutine(result)
}