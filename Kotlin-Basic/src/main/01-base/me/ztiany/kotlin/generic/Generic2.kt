package me.ztiany.kotlin.generic

/**
 * 运行时泛型：擦除和实例化参数。
 *
 * 其实泛型类型擦除并不是真的将全部的类型信息都擦除，还是会将类型信息放在对应 class 的常量池中的。Java 将泛型信息存储在哪里？
 * 可以参考以下网页：https://stackoverflow.com/questions/937933/where-are-generic-types-stored-in-java-class-files/937999#937999
 */
fun main() {
    printSum(listOf(1, 2, 3))
    println(isA<String>("abc"))
    println(isA<String>(123))
}

private fun printSum(c: Collection<*>) {
    val intList = c as? List<Int> ?: throw IllegalArgumentException("List is expected")
    println(intList.sum())
}

private fun printSum1(c: Collection<Int>) {
    //这是允许的，因为 Int 已经被确定了：Kotlin 编译器是足够智能的，在编译期它已经知道相应的类型信息时，is 检测是允许的。
    if (c is List<Int>) {
        println(c.sum())
    }
}

private inline fun <reified T> isA(value: Any) = value is T
