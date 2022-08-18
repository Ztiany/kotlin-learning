package me.ztiany.kotlin.advanced

/**
 * 可空类型与非空类型：Kotlin 的类型系统旨在消除来自代码空引用的危险，也称为《十亿美元的错误》。
 *
 * 许多编程语言（包括 Java）中最常见的陷阱之一是访问空引用的成员，导致空引用异常。在 Java 中，
 * 这等同于 NullPointerException 或简称 NPE。
 *
 * Kotlin 的类型系统旨在从我们的代码中消除 NullPointerException。NPE 的唯一可能的原因可能是：
 *
 * 1. 显式调用 throw NullPointerException()
 * 2. 使用了 !! 操作符
 * 3. 外部 Java 代码导致的
 * 4. 对于初始化，有一些数据不一致（如一个未初始化的 this 用于构造函数的某个地方）
 *
 * 虽然 Java 在避免 NPE 上也做出了努力，比如推出了 Optional 类，但是如果对性能有着严苛的要求，则 Optional 是一个优化点，因为多次测试发现，
 * Optional 的耗时大约是普通判空的数十倍。这主要是因为 Optional<T> 是一个包含类型 T引用的泛型类，在使用的时候多创建了一次对象，当
 * 数据量非常大的时候，频繁地实例化对象会造成性能损失。如果未来 Java 支持了值类（value class），这些开销将会不复存在。——《Kotlin 核心编程》
 *
 * Kotlin 的可空类型不会有类似的开销，Kotlin 可空类型优于 Java Optional 的地方体现在：
 *
 * 1. Kotlin 可空类型兼容性更好；
 * 2. Kotlin 可空类型性能更好、开销更低；
 * 3. Kotlin 可空类型语法简洁。
 *
 * 注意：由于 null 只能被存储在 Java 的引用类型的变量中，所以在 Kotlin 中基本数据的可空版本都会使用该类型的包装形式。
 * 同样，如果你用基本数据类型作为泛型类的类型参数，Kotlin 同样会使用该类型的包装形式。
 */
fun main() {
    //在 Kotlin 中，类型系统区分一个引用可以容纳 null （可空引用）还是不能容纳（非空引用）。
    // 例如，String 类型的常规变量不能容纳 null：

    val a: String = "abc"
    println(a.length)

    var b: String? = "abc"
    b = null // 可空类型必须加上?

    //调用可空类型的方法
    //1 判断
    if (b != null) {
        println(b.length)
    }
    //2 ?符号
    println(b?.length)
    //3 Elvis 操作符
    println(b?.length ?: -1)


    //安全的类型转换
    val aInt: Int? = a as? Int
    println(aInt)

    //可空类型的集合：如果你有一个可空类型元素的集合，并且想要过滤非空元素，你可以使用 filterNotNull 来实现。
    val nullableList: List<Int?> = listOf(1, 2, null, 4)
    val intList: List<Int> = nullableList.filterNotNull()
    println(intList)

    // !! 操作符
    val bb = aInt!!
}

//用 Either 代替可空类型也是不错的选择：Either 只有两个子类型：Left、Right，如果 Either[A，B] 对象包含的是 A 的实例，那它就是 Left 实例，否则就是 Right 实例。
//通常来说，Left 代表出错的情况，Right 代表成功的情况。Kotlin 虽然没有 Either 类，但是我们可以通过密封类便捷地创造出 Either 类：
sealed class Either<A, B> {
    class Left<A, B>(val value: A) : Either<A, B>()
    class Right<A, B>(val value: B) : Either<A, B>()
}

data class Seat(val student: Student?)
data class Student(val glasses: Glasses?)
data class Glasses(val degreeOfMyopia: Double)

fun getDegreeOfMyopiaKt(seat: Seat?): Either<Error, Double> {
    return seat?.student?.glasses?.let {
        Either.Right(it.degreeOfMyopia)
    } ?: Either.Left(Error("-1"))
}

//这样写起来代码不是变多了吗？是这样，没错。但是《Kotlin 核心编程》第四章中所说，我们需要用 ADT 良好地组织业务。在获取数据时，我们往往是经过多个方法逐步获取，
//最后整合在一起。定义一个 Error 类，将所有步骤中的错误都抽象为不同的子类型，便于最终的处理以及后期排查错误，何乐而不为。
//如果我们不这么做，只是隐藏了潜在的异常，调用者通常会忽略可能发生的错误，这是很危险的设计。
