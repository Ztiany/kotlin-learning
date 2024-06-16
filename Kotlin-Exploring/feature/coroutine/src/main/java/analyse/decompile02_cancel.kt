package analyse

import kotlinx.coroutines.*
import common.logCoroutine

suspend fun main() {
    //sample01()
    //sample02()
    sample03()
    //sample04()
}

private suspend fun sample04() {
    val scope1 = CoroutineScope(SupervisorJob())
    val scope2 = CoroutineScope(SupervisorJob())

    val job = scope1.launch {
        logCoroutine("scope1 running")

        try {
            val result = sample4_loadResult(scope2)
            logCoroutine("result = $result")
        } catch (e: Exception) {
            logCoroutine("exception = $e")//会正常打印异常
        }

        logCoroutine("scope1 end")
    }

    job.join()
    logCoroutine("finished")
}

private suspend fun sample4_loadResult(scope2: CoroutineScope): String {
    return scope2.async {
        logCoroutine("scope2 running")
        delay(2000)
        throw ArithmeticException("fake")
        logCoroutine("scope2 end")
        "Result"
    }.await()
}

private suspend fun sample03() {
    val scope1 = CoroutineScope(SupervisorJob())
    val scope2 = CoroutineScope(SupervisorJob())

    val job = scope1.launch {
        logCoroutine("scope1 running")

        try {
            sample3_execute(scope2)
            logCoroutine("sample3_execute ok")//会正常打印 sample3_execute ok
        } catch (e: Exception) {
            logCoroutine("exception = $e")
        }

        delay(3000)
        logCoroutine("scope1 end")
    }

    job.join()
    logCoroutine("finished")
}

private suspend fun sample3_execute(scope2: CoroutineScope) {
    scope2.launch {
        logCoroutine("scope2 running")
        delay(2000)
        throw ArithmeticException("fake")
        logCoroutine("scope2 end")
        "Result"
    }.join()
}

private suspend fun sample02() {
    val scope1 = CoroutineScope(SupervisorJob())
    val scope2 = CoroutineScope(SupervisorJob())

    scope1.launch {
        logCoroutine("scope1 running")
        scope2.launch {
            logCoroutine("scope2 running")
            delay(4000)
            logCoroutine("scope2 end")
        }.join()
        delay(3000)
        logCoroutine("scope1 end")
    }

    delay(2000)
    scope1.cancel()
    delay(5000)
    logCoroutine("scope1 cancel")
}

private suspend fun sample01() {
    val scope = CoroutineScope(SupervisorJob())
    val context = Job()
    println("provided scope = $scope")
    println("provided job = $context")

    scope.launch(/*context*/) {
        println("parent scope = $scope")//== provided scope
        println("CancelJob job parent: job = ${coroutineContext[Job]}")

        launch {
            println("CancelJob job1: job = ${coroutineContext[Job]}")
            delay(2000L)
            println("CancelJob job1 finished")
            scope.cancel()
        }

        launch {
            println("CancelJob job2: job = ${coroutineContext[Job]}")
            delay(3000L)
            println("CancelJob job2 finished")
        }
    }.join()
}
