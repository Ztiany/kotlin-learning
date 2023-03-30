package me.ztiany.kotlin.network

import java.net.URL


fun main(args: Array<String>) {
    doGet()
}

private fun doGet() {
    //执行网络请求非常方便
    val text = URL("https://www.baidu.com/").readText()
    println(text)
}