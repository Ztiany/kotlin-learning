package me.ztiany.kotlin.newf.kt13

import kotlin.random.Random

/**
 * Prior to Kotlin 1.3, there was no uniform way to generate random numbers on all platforms — we had to resort to platform-specific solutions like java.util.Random on JVM.
 * This release fixes this issue by introducing the class kotlin.random.Random, which is available on all platforms:
 */
fun main() {
    //Kotlin 有了自己的 Multiplatform  随机数生成器
    val number = Random.nextInt(42)  // number is in range [0, limit)
    println(number)
}