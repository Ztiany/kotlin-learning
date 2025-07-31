package me.ztiany.kapt.main

import me.ztiany.kcp.annotations.debuglog.DebugLog

fun main(args: Array<String>) {
    test1()
    test2()
    test2("Ztiany")
}

@DebugLog
private fun test1() {
    println("Hello, world!")
}

@DebugLog
private fun test2(name: String = "World") {
    println("Hello, $name!")
}