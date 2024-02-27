package me.ztiany.kotlin.function

import java.io.File


///////////////////////////////////////////////////////////////////////////
// 惰性求值
///////////////////////////////////////////////////////////////////////////

/*
类似 map 和 filter 这些函数会立即创建临时集合。这意味着每一步的临时结果都被保存在一个临时列表中。序列给了你一个可选的方法来完成这样的计算。这样可以避免创建一次性的临时对象。

集合懒操作的入口是 Sequence 接口。这个接口仅表示：一系列可以逐个枚举的元素。Sequence 只提供了一个方法：`iterator`。你可以用它来从序列中获取元素值。

序列操作分为两类：`中间操作和最终操作`。中间操作返回另一个序列。这个序列知道如何变换原始序列的集合。最终操作返回一个结果。这个结果可以是集合、集合元素、数字或者任意其他通过初始集合变换得到的序列
 */

private class Person(val name: String, val age: Int)

private fun lazyEvaluation() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))

    // 非惰性求值
    people.map(Person::name).filter { it.startsWith("A") }

    // 惰性求值
    people.asSequence()// 1 把初始集合转换为序列
        .map(Person::name)// 2 序列支持跟集合同样的API
        .filter { it.startsWith("A") } //
        .toList()// 3 把结果序列转换为列表
}

///////////////////////////////////////////////////////////////////////////
// 创建序列：对集合调用 `asSequence()` 用于创建一个序列。另一个方式是使用 `generateSequence()` 函数
///////////////////////////////////////////////////////////////////////////

private fun useAsSequence() {
    listOf(1, 2, 3, 4).asSequence()
        .map { print("map($it) "); it * it }
        .filter { print("filter($it) "); it % 2 == 0 }

    listOf(1, 2, 3, 4).asSequence()
        .map { print("map($it) "); it * it }
        .filter { print("filter($it) "); it % 2 == 0 }
        .toList()
}

private fun stringSequence(args: Array<String>) {
    val naturalNumbers = generateSequence(0) { it + 1 }
    val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
    println(numbersTo100.sum())
}

private fun File.isInsideHiddenDirectory() = generateSequence(this) {
    println("generate：$it")
    it.parentFile
}.any {
    println("any：$it")
    it.isHidden
}

fun main(args: Array<String>) {
    val file = File(".").absoluteFile
    println(file.isInsideHiddenDirectory())
}