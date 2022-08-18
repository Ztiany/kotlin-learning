package me.ztiany.kotlin.function

/**
 * **如何理解 Unit**？其实它与 int 一样，都是一种类型，然而它不代表任何信息，用面向对象的术语来描述就是一个单例，它的实例只有一个，可写为 ()。
 * 那么，Kotlin 为什么要引入 Unit 呢？一个很大的原因是函数式编程侧重于组合，尤其是很多高阶函数，在源码实现的时候都是采用泛型来实现的。
 * 然而 void 在涉及泛型的情况下会存在问题【参考 [ReturnVoid]】。
 *
 * ——具体参考《Kotlin 核心编程》第二章
 */
fun main() {

    val strLen = object : FunctionKT<String, Int> {
        override fun apply(arg: String): Int {
            return arg.length
        }
    }

    /*相比之下，比 Java 版本的简介多了。*/
    val printStr = object : FunctionKT<String, Unit> {
        override fun apply(arg: String) {
            println(arg)
        }
    }

}

private interface FunctionKT<Arg, Return> {
    fun apply(arg: Arg): Return
}
