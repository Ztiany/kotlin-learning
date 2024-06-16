package analyse

import java.text.SimpleDateFormat
import java.util.*

// 用 ThreadLocal 包装 SimpleDateFormat，保证线程安全。
private val dateFormat = ThreadLocal.withInitial {
    SimpleDateFormat("HH:mm:ss:SSS")
}

private val now = {
    dateFormat.get().format(Date())
}

fun logCoroutine(msg: Any?) = println("${now()} [${Thread.currentThread().name}] $msg")
