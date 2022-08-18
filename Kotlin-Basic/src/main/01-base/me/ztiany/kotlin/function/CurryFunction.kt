package me.ztiany.kotlin.function

import java.io.OutputStream
import java.nio.charset.Charset

/**
 *- **柯里化函数**：多元函数变成单元函数调用链的过程，具体来说指的是把接收多个参数的函数变换成一系列仅接收单一参数函数的过程，
 * 在返回最终结果值之前，前面的函数依次接收单个参数，然后返回下一个新的函数。
 *- **偏函数**：指定多参数函数的某些参数而得到新函数，实现方式是函数扩展。
 *
 * **为什么会有柯里化**：Lambda 演算是函数式语言的理论基础。在严格遵守这套理论的设计中，所有的函数都只能接收最多一个参数。为了解决多参数的问题，
 * Haskell Curry 引入了柯里化这种方法。值得一提的是，这种技术也是根据他的名字来命名的——Currying，后续其他语言也以此来称呼它。
 *
 * **柯里化有用么**？柯里化是为了简化 Lambda 演算理论中函数接收多参数而出现的，它简化了理论，将多元函数变成了一元。
 * 然而，在实际工程中，Kotlin 等语言并不存在这种问题，因为它们的函数都可以接收多个参数进行计算。那么，这是否意味着柯里化对我们而言，
 * 仅仅只有理论上的研究价值呢？【确实是这样的，具体参考《kotlin 核心编程》第二章】
 */
fun main() {
    //柯里化函数
    log("benny", System.out, "HelloWorld")
    logA("benny")(System.out)("HelloWorld Again.")

    val curriedLog = ::log.curried()("benny")(System.out)("HelloWorld Again.")
    println(curriedLog)
    sum(1)(3)(5)
    sum(2)(4)(6)
    println()

    val consoleLogWithTag = (::log.curried())("benny")(System.out)
    consoleLogWithTag("HelloAgain Again.")
    consoleLogWithTag("HelloAgain Again.")
    consoleLogWithTag("HelloAgain Again.")
    consoleLogWithTag("HelloAgain Again.")

    //偏函数
    val bytes = "我是中国人".toByteArray(charset("GBK"))
    val stringFromGBK = makeStringFromGbkBytes(bytes)
}

///////////////////////////////////////////////////////////////////////////
// 柯里化
///////////////////////////////////////////////////////////////////////////
//三个参数的函数
private fun log(tag: String, target: OutputStream, message: Any?) {
    target.write("[$tag] $message\n".toByteArray())
}

//柯里化函数
private fun logA(tag: String) = fun(target: OutputStream) = fun(message: Any?) = target.write("[$tag] $message\n".toByteArray())

private fun logADetailed(tag: String): (target: OutputStream) -> (message: Any?) -> Unit {
    return fun(target: OutputStream): (message: Any?) -> Unit {
        return fun(message: Any?) {
            target.write("[$tag] $message\n".toByteArray())
        }
    }
}

//三个参数的函数
private fun sum(x: Int, y: Int, z: Int): Int {
    return x + y + z;
}

//柯里化函数
private fun sum(x: Int) = { y: Int ->
    { z: Int ->
        x + y + z
    }
}

//Function3表示任意该类型的函数
private fun <P1, P2, P3, R> Function3<P1, P2, P3, R>.curried() = fun(p1: P1) = fun(p2: P2) = fun(p3: P3) = this(p1, p2, p3)

///////////////////////////////////////////////////////////////////////////
// 偏函数
///////////////////////////////////////////////////////////////////////////
//多参数函数
private val makeString = fun(byteArray: ByteArray, charset: Charset): String {
    return String(byteArray, charset)
}

//偏函数
private val makeStringFromGbkBytes = makeString.partial2(charset("GBK"))

private fun <P1, P2, R> Function2<P1, P2, R>.partial2(p2: P2) = fun(p1: P1) = this(p1, p2)
private fun <P1, P2, R> Function2<P1, P2, R>.partial1(p1: P1) = fun(p2: P2) = this(p1, p2)