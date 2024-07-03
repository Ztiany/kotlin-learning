package explore.analyse

import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted

fun main() {
    val theContinuation = object : Continuation<String> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<String>) {
            println("Coroutine End: $result")
        }
    }

    val theCoroutinesFun: suspend () -> String = {
        "HelloWorld"
    }

    val coroutine = theCoroutinesFun.createCoroutineUnintercepted(theContinuation)
    println(theCoroutinesFun)
    println(coroutine)
    println(theCoroutinesFun.javaClass)
    println(coroutine.javaClass)
    coroutine.resume(Unit)
}

