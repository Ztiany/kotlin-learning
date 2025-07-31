package explore.book.dukc.ch04_practice

/**
 * 《深入理解Kotlin协程》chapter 4.1：序列生成器
 */
fun main() {
    //序列生成器实际上包含了“序列”和“生成器”两部分。对于使用者而言，作为结果的“序列”更重要，而对于 API 的设计者而言，作为过程的“生成器”的实现才是关键。

    //标准库的序列生成器介绍【4.1.2】
    kotlinStandardSequenceGenerator()
}

private fun kotlinStandardSequenceGenerator() {
    //Kotlin 标准库中提供了类似的生成器实现，通常我们也称它为“懒”序列生成器。序列生成器的使用方法与我们前面实现的 Generator 颇为相似
    val fibonacci = sequence {
        yield(1)
        var current = 1
        var next = 1
        while (true) {
            yield(next)
            next += current
            current = next - current
        }
    }

    for (i in fibonacci.take(10)) {
        println(i)
    }
}