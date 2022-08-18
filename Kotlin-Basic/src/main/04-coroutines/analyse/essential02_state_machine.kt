package analyse

import kotlinx.coroutines.delay
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted
import kotlin.coroutines.resume

suspend fun commonSuspendFun1(): String {
    log("commonSuspendFun1")
    delay(100)
    return "[hello world 1] "
}

suspend fun commonSuspendFun2(): String {
    log("commonSuspendFun2")
    delay(100)
    return "[hello world 2]  "
}

suspend fun commonSuspendFun3(): String {
    log("commonSuspendFun3")
    delay(100)
    return "[hello world 3]"
}

private class TheContinuation : Continuation<String> {
    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<String>) {
        println("Coroutine End: $result")
    }
}

fun main() {

    val theCoroutinesFun: suspend () -> String = {
        //返回一个字符串 hello world1
        val one = commonSuspendFun1()
        //返回一个字符串 hello world2
        val two = commonSuspendFun2()
        //返回一个字符串 hello world3
        val three = commonSuspendFun3()
        one + two + three
    }

    val theContinuation = TheContinuation()
    val coroutine = theCoroutinesFun.createCoroutineUnintercepted(theContinuation)
    coroutine.resume(Unit)
}


