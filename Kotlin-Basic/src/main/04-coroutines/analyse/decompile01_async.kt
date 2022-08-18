package analyse

import kotlinx.coroutines.*

suspend fun main() {
    log("aaaaa")
    val deferred = GlobalScope.async {
        log("bbbbb")
        delay(100)
        log("ccccc")
        delay(100)
        log("ggggg")
        delay(100)
        log("fffff")
        delay(100)
        log("ooooo")
        123
    }
    log(deferred.javaClass)
    log("ddddd")
    val result = deferred.await()
    log("eeeee")
    log(result)
}