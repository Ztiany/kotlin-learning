package analyse

import kotlinx.coroutines.*
import common.logCoroutine

suspend fun main() {
    val scope = CoroutineScope(Job())
    val jobs = mutableListOf<Job>()
    repeat(2) {
        scope.launch { criticalSectionSuspending() }.let(jobs::add)
    }
    jobs.forEach { it.join() }
}

/**Synchronized 并不能和挂起函数配合工作*/
//@Synchronized
suspend fun criticalSectionSuspending() {
    logCoroutine("Starting!")
    delay(10)//此时，criticalSectionSuspending 以及退出了，且释放了同步锁。
    logCoroutine("Ending!")
}