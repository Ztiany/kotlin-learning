package me.ztiany.kotlin.stdlib

/**
 * **kotlin/util/Standard.kt API学习**：
 *
 *  - let：调用某对象的 let 函数，则该对象为函数的参数。在函数块内可以通过 it。 指代该对象。返回值为函数块的最后一行或指定 return 表达式。
 *  - run：调用 run 函数块，返回值为函数块最后一行，或者指定 return 表达式。
 *  - apply：调用某对象的 apply 函数，在函数块内可以通过 this 指代该对象。返回值为该对象自己。
 *  - also：调用某对象的 also 函数，则该对象为函数的参数。在函数块内可以通过 it 指代该对象，返回值为该对象自己。
 *  - with：with 函数和前面的几个函数使用方式略有不同，因为它不是以扩展的形式存在的。它是将某对象作为函数的参数，在函数块内可以通过 this 指代该对象。返回值为函数块的最后一行或指定 return 表达式。
 */
fun main(args: Array<String>) {
    letSample()
    applySample()
    runSample()
    alsoSample()
    withSample()
}

fun withSample() {
    println("withSample--------------")
    val result = with(100) {
        val a = toString()
        a
    }
    println(result)
}

fun alsoSample() {
    println("alsoSample--------------")
    val abc = "ABC".also {
        println(it)
    }
    println(abc)
}

fun runSample() {
    println("runSample--------------")
    println(100.run {
        println(this)
        "100 return"
    })
}

fun applySample() {
    println("applySample--------------")
    val result = 4.apply {
        println(this)
    }
    println(result)

    val list = mutableListOf<String>()

    list.apply {
        add("A")
        add("B")
    }
}

private fun letSample() {
    println("letSample--------------")
    val abc = 1.let {
        println(it)
        "ABC"
    }
    println(abc)
}

