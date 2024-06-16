package core.exception_supervision


import kotlinx.coroutines.*

/*
常规的任务和监督任务之间的另一个重要区别是异常处理。 每一个子任务应该通过异常处理机制处理自身的异常。 这种差异来自于子任务的执行失败不会传播给它的父任务的事实。
*/
fun main() = runBlocking {

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    // 如果是 coroutineScope 则会崩溃。
    supervisorScope {
        val child = launch(handler/*如果没有 handler，不会影响协程的执行，但是会自动打印异常信息到控制台。*/) {
            println("Child throws an exception")
            throw AssertionError()
        }
        delay(1000)
        println("Scope is completing")
    }

    println("Scope is completed")
}