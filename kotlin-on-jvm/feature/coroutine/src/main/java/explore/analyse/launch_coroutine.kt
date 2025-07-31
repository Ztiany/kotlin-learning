package explore.analyse

import kotlinx.coroutines.CoroutineStart.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import common.logCoroutine
import kotlin.concurrent.thread


/**
 *
 * @author Ztiany
 */
@ExperimentalCoroutinesApi
suspend fun main() {
    //sample1() //线程
    //sample2()
    //sample3()
    //sample4()
    sample5()
}

@ExperimentalCoroutinesApi
private suspend fun sample5() {
    logCoroutine(1)
    val job = GlobalScope.launch(start = UNDISPATCHED) {
        logCoroutine(2)
        delay(100)
        logCoroutine(3)
    }
    logCoroutine(4)
    job.join()
    logCoroutine(5)
}

@ExperimentalCoroutinesApi
private suspend fun sample4() {
    logCoroutine(1)
    val job = GlobalScope.launch(start = ATOMIC) {
        logCoroutine(2)
    }
    job.cancel()
    logCoroutine(3)
}

private suspend fun sample3() {
    logCoroutine(1)
    val job = GlobalScope.launch(start = LAZY) {
        logCoroutine(2)
    }
    logCoroutine(3)
    job.join()
    logCoroutine(4)
}

private suspend fun sample2() {
    logCoroutine(1)
    val job = GlobalScope.launch {
        logCoroutine(2)
    }
    logCoroutine(3)
    job.join()
    logCoroutine(4)
}

private fun sample1() {
    thread {
        logCoroutine("Hello")
    }
}