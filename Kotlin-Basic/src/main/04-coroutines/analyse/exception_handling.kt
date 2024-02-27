package analyse

import kotlinx.coroutines.*
import java.lang.AssertionError
import kotlin.coroutines.EmptyCoroutineContext

fun main() = runBlocking {
//    demo1()
//    demo2()
//    demo3()
//    demo4()
//    demo5()
//    demo6()
//    demo7()
//    demo8()
//    demo9_1(this)
    demo9_2()
}

private suspend fun demo9_1(scope: CoroutineScope) {
    scope.launch {

        try {
            val usersDeferred = async {
                delay(2000)
                throw Exception("test------------demo9_1")
            }
            val moreUsersDeferred = async {
                listOf("1", "2", "3")
            }
            val users = usersDeferred.await()
            val moreUsers = moreUsersDeferred.await()
        } catch (exception: Exception) {
            // handle exception
            logCoroutine(exception)
        }

    }.join()

    // 不会被打印，上面 async 中的异常终止了整个协程。
    println("demo9_1 end")
}

private suspend fun demo9_2() {
    CoroutineScope(Dispatchers.Default)/*新的 Scope*/.launch {

        try {
            val usersDeferred = async {
                delay(2000)
                throw Exception("test------------demo9_2")
            }
            val moreUsersDeferred = async {
                listOf("1", "2", "3")
            }
            val users = usersDeferred.await()
            val moreUsers = moreUsersDeferred.await()
        } catch (exception: Exception) {
            // handle exception
            logCoroutine(exception)
        }

    }.join()

    // 会被打印，上面 async 中的异常终止的是新的 Scope，不会影响外部协程。
    println("demo9_2 end")
}


private suspend fun demo8() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        logCoroutine("${coroutineContext[CoroutineName]} $throwable")
    }

    CoroutineScope(Job() /*指定了 Job，不影响外部协程了*/ + exceptionHandler).launch {
        /*async {
            throw Exception("test")
        }*/

        try {
            async {
                throw Exception("test")
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.join()

    println("end")
}

//https://juejin.cn/post/6844903854245429255
private suspend fun demo7() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        logCoroutine("${coroutineContext[CoroutineName]} $throwable")
    }

    logCoroutine(1)
    try {
        supervisorScope { //①
//        coroutineScope { //①
            logCoroutine(2)

            launch(exceptionHandler + CoroutineName("②")) { // ②
                logCoroutine(3)

                //对于 supervisorScope 的子协程 （例如 ②）的子协程（例如 ③），如果没有明确指出，它是遵循默认的作用于规则的，也就是 coroutineScope 的规则了，
                // 出现未捕获的异常会尝试传递给父协程并尝试取消父协程。所以这里的异常，由 ② 处设置的 exceptionHandler 处理。
                launch(exceptionHandler + CoroutineName("③")) { // ③
                    logCoroutine(4)
                    delay(100)
                    throw ArithmeticException("Hey!!")
                }

                logCoroutine(5)
            }

            logCoroutine(6)

            val job = launch { // ④
                logCoroutine(7)
                delay(1000)
            }

            try {
                logCoroutine(8)
                job.join()
                logCoroutine("9")
            } catch (e: Exception) {
                logCoroutine("10. $e")
            }

        }//supervisorScope end

        logCoroutine(11)
    } catch (e: Exception) {
        //这个是我们对 coroutineScope 整体的一个捕获，如果 coroutineScope 内部以为异常而结束，那么我们是可以对它直接 try ... catch ... 来捕获这个异常的，
        // 这再一次表明协程把异步的异常处理到同步代码逻辑当中。
        logCoroutine("12. $e")
    }

    logCoroutine(13)
}

private suspend fun demo6() {
    GlobalScope.launch {

        val job = launch {
            println(1)
            delay(1000)
            println(2)
            //throw CancellationException() // CancellationException 异常不会有任何影响，也捕获不到。
            throw AssertionError() // 其他异常会影响协程的执行，可以捕获到。
        }

        launch {
            println(3)
            try {
                job.join()
            } catch (e: Exception) {
                println(e)
            }
            println(4)
        }

        println(5)
        delay(2000)
        println(6)

    }.join()
}

private suspend fun demo5() {
    coroutineScope { //①

        logCoroutine(2)

        launch { // ②
            logCoroutine(3)
            launch { // ③
                logCoroutine(4)
                delay(100)
                throw ArithmeticException("Hey!!")
            }
            logCoroutine(5)
        }

        logCoroutine(6)

        val job = launch { // ④
            logCoroutine(7)
            delay(1000)
        }

        try {
            logCoroutine(8)
            job.join()
            logCoroutine("9")
        } catch (e: Exception) {
            logCoroutine("10. $e")
        }
    }
}

//https://stackoverflow.com/questions/53577907/when-to-use-coroutinescope-vs-supervisorscope
private suspend fun demo4() {
    CoroutineScope(EmptyCoroutineContext).launch {
        supervisorScope {
            val color = async { delay(2000); "purple" }
            val height = async<Double> { delay(100); throw Exception() }
            try {
                println("A %s box %.1f inches tall".format(color.await(), height.await()))
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }.join()

    println("end")
}

private suspend fun demo3() {
    CoroutineScope(EmptyCoroutineContext).launch {
        val deferred = async {
            throw Exception("test")
        }
        try {
            deferred.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.join()

    println("end")
}

private suspend fun demo2() {
    /*coroutineScope 启动的协程，如果协程内发生异常，会自动将异常打印到控制台。*/
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
    }
    CoroutineScope(EmptyCoroutineContext + coroutineExceptionHandler).launch {//①

        //coroutineScope 如果以异常结束，外部协程 ① 就被取消，这是常规的异常处理逻辑。
        coroutineScope {//②

            val deferred = async {
                println("point")
                throw Exception("test")//已经触发了异常
            }

            //在 coroutineScope 内，异常传播无法被停止，即使捕获了，这个异常还是会取消整个协程②，最终 coroutineScope 以异常结束。end-1 不会被打印。
            try {
                //deferred.join()//有影响
                deferred.await()
            } catch (e: Exception) {
                println("Exception cough.")
                e.printStackTrace()
            }
        }//coroutineScope end

        println("end-1")

    }.join()

    println("end-2")
}

//https://xuyisheng.top/coroutine_exception
private suspend fun demo1() {
    CoroutineScope(EmptyCoroutineContext).launch {//①

        //supervisorScope 如果以异常结束，外部协程 ① 就被取消，这是常规的异常处理逻辑。
        supervisorScope {//②
            val deferred = async {
                println("point")
                throw Exception("test")//触发了异常
            }

            //不用 try-catch，supervisorScope② 就会以异常结束。end-1 不会被打印。
            //deferred.await()

            // 用 try-catch，内部异常就处理了，supervisorScope② 就会正常结束。end-1 会被打印。
            // 在 supervisorScope 中，async 的异常是可以捕获到的，即捕获后，这个异常传播就结束了，不会影响其他协程。【这符合 supervisorScope 的异常传播规则】
            try {
                //deferred.join()//无影响，join 处理关心协程有没有完成。在 supervisorScope 中，父协程不关心不管子协程是以异常完成的还是正常完成的，完成了就可以。所以无影响。
                deferred.await()
            } catch (e: Exception) {
                println("Exception cough.")
                e.printStackTrace()
            }
        }

        println("end-1")

    }.join()

    println("end-2")
}