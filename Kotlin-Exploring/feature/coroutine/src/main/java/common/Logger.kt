package common

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

fun String.printAligned(targetLength: Int = 50, paddingChar: Char = '=') {
    val paddingLength = targetLength - this.length
    if (paddingLength <= 0) {
        println(this)
    } else {
        val padding = paddingChar.toString().repeat(paddingLength)
        println("$this $padding")
    }
}