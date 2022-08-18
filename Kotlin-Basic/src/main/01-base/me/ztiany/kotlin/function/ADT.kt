package me.ztiany.kotlin.function

/**
 * 在计算机编程，特别是函数式编程与类型理论中，ADT 是一种组合类型（composite type）。例如，一个类型由其他类型组合而成。
 * 两个常见的代数类型是：
 *
 * - “和”（sum）类型；
 * - “积”（product）类型。
 *
 * 简单来说，ADT就是像代数一样的数据类型。代数是什么，最简单的理解就是能代表数字的符号。
 *
 * - x + 5 = 6
 * - y * 3 = 21
 *
 * 上面表达式中的 x、y 就是代数。通过解上面的方程，我们知道了代数 x 代表的是数字 1，而代数 y 代表的是数字 7。再仔细看一下上面的方程，
 * 我们还发现了两个操作符：“+”和“*”。那么通过代数和这些操作符能做什么呢？再看看两个表达式：
 *
 * - x * 1 = z
 * - a + 2 = c
 *
 *可以看出，第 1 个表达式中代数 x 与 1 通过乘法操作得到了一个新的代数 z，第 2 个表达式中代数 a 与 2 通过加法操作得到了一个新的代数 c。
 * 到这里，我们其实就可以思考一下了，如果把上面表达式中的代数与数字换成编程语言中的类型或者值，那它们之间通过某种操作是不是就
 * 可以得到某种新的类型呢？
 *
 * 而当我们将这些代数或者数字换成类型，那么这种被我们用代数或者数字换成的类型，以及通过这些类型所产生的新类型就叫作**代数数据类型（ADT）**。
 *
 * **那么 ADT 究竟有什么用呢**？其实 ADT 的应用很广，就拿我们所熟知的业务逻辑来说吧，我们可以将一些比较简单的类型通过某种“操作符”而抽象
 * 成比较复杂而且功能强大的类型。另外在编程语言中，某些常见的类型其实就是代数类型，比如枚举类。并且，ADT 是类型安全的，使用它可以为我们
 * 免去许多麻烦。
 *
 * ADT 中常见的两种类型：
 *
 * - 和类型（sum）：比如密封类，和类型是一种“OR”的关系。
 * - 积类型（product）：积类型可以拥有好几种类型，是一种“AND”关系。
 *
 * 和类型一次只能是其中的某种类型，要么是 A，要么是 B，不能同时拥有这两种类型，所以它代表的是“OR”的含义。
 *
 * 具体参考《Kotlin 核心编程》第四章。
 */
fun main() {

}

///////////////////////////////////////////////////////////////////////////
//1. 使用ADT最大的好处就是可以很放心地去使用 when 表达式。
///////////////////////////////////////////////////////////////////////////
sealed class Shape {
    class Circle(val radius: Double) : Shape()
    class Rectangle(val width: Double, val height: Double) : Shape()
    class Triangle(val base: Double, val height: Double) : Shape()
}

fun getArea(shape: Shape): Double = when (shape) {
    is Shape.Circle -> Math.PI * shape.radius * shape.radius
    is Shape.Rectangle -> shape.width * shape.height
    is Shape.Triangle -> shape.base * shape.height / 2.0
    //不再需要 else
}

///////////////////////////////////////////////////////////////////////////
// 2. ADT在函数式编程中一个非常大的用处，就是结合模式匹配。虽然Kotlin并没有在极大程度上支持模式匹配，
// 然而 when 表达式依旧是非常强大的一个语言特性。
///////////////////////////////////////////////////////////////////////////
/**
 * 维基百科：在计算机科学中，模式匹配是检查给定标记序列中是否存在某种模式的组成部分的行为。与模式识别相反，匹配通常必须是精确的。
 * 模式通常具有序列或树结构的形式。模式匹配的用途包括输出一个模式在一个标记序列中的位置（如果有的话），输出匹配模式的一些组成部分，
 * 以及用一些其他的标记序列（即搜索和替换）替换匹配模式。更多可以参考
 *
 * - [为什么诸多编程语言都将模式匹配作为重要构成？](https://www.zhihu.com/question/30354775)
 *- [话说模式匹配(1): 什么是模式？](https://hongjiang.info/scala-pattern-matching-1/)
 *
 * 关于 when：
 *
 * 1. when 表达式不是完整的模式匹配。真正的模式匹配要比 when 表达式更加强大。然而，Kotlin 是一门倡导实用主义的语言，
 * when 表达式结合 Kotlin 的其他语言特性，比如解构、Smart Casts，已经能够满足大部分工程中的实际需求。
 * 2. 要想了解 Kotlin 首席设计师关于模式匹配的思考，可以参考他的这封邮件的内容：
 * https://www.mail-archive.com/amber-spec-experts@openjdk.java.net/msg00006.html
 *
 * 模式匹配（Pattern Matching）：其实模式匹配与 Java 的正则匹配非常相似（Pattern 类），只是模式匹配中匹配的不仅有正则表达式，
 * 还可以有其他表达式。这里的“表达式”就是下面将要介绍的模式。
 *
 * 1. 表达式：一个数字、一个对象的实例，或者说，凡是能够求出特定值的组合我们都能称其为表达式。
 * 2. 当我们在构造模式时就是在构造表达式。你可以将模式构造成简单的数字、逻辑表达式，也可以将其构造成复杂的类或者其他嵌套的结构。
 */
private fun patternMatching() {

}

//2.1 常量模式
private fun constantPattern(x: Int) = when (x) {
    1 -> "a"
    2 -> "b"
    else -> "z"
}

//2.2 类型模式：就是上面的 getArea

//2.3 逻辑表达式模式
private fun logicPattern(a: Int) = when {
    a in 2..11 -> ("$a is smaller than 10 and bigger than 1")
    else -> "Maybe" + a + "is bigger than 10, or smaller than 1"
}

fun logicPattern(a: String) = when {
    a.contains("Yison") -> "Something is about Yison"
    else -> "It`s none of Yison`s business"
}

// 以上 3 种就是在 Kotlin 中比较常见的模式。看了上面 3 种模式的匹配方式后，你可能会想，这就是模式匹配吗？和 if-else 有什么区别？的确，
// 上面的 3 种模式在进行匹配的时候，我们其实都可以用 if-else 或者 switch-case 来实现，就像我们在 Java 中经常做的那样。那么模式匹配的
// 威力到底体现在哪里呢？

//看到这里，需要先查看 《Kotlin 核心编程》第四章中的 Scale 的模式匹配示例代码。

// 总结一下，模式匹配中的模式就是表达式，模式匹配的要匹配的就是表达式。模式匹配的核心其实就是解构，
// 也就是反向构造表达式——给定一个复杂的数据结构，然后将之前用来构成该复杂结构的参数抽取出来。

/*
2.4 处理嵌套表达式

上面几种模式匹配都能用 if-else 或者 switch-case 语句来实现，那么有没有哪种模式是用 if-else 等语句实现起来有困难，
但是用模式匹配却会很简洁的呢？答案是肯定的，那就是嵌套表达式。
 */

//首先定义如下的结构
//      Num 类表示某个整数的值
//      Operate 类则是一个树形结构，它被用来表示一些复杂的表达式，其中 opName 属性表示常见的操作符，比如 “+”“-”“*”“/”。
sealed class Expr {
    data class Num(val value: Int) : Expr()
    data class Operate(val opName: String, val left: Expr, val right: Expr) : Expr()
}

// 下面我们就利用上述结构来实现一些需求。在进行整数表达式计算的时候，你往往会遇到许多可以简化的表达式，比如“1+0”就可 以
// 简化为1，“1*0”就可以简化为0。

// 我们现在实现一个比较简单的需求：将“0+x”或者“x+0”化简为x，其他情况返回表达式本身。
fun simplifyExpr(expr: Expr): Expr = if (expr is Expr.Num) {
    expr
} else if (expr is Expr.Operate && expr.opName == "+" && expr.left is Expr.Num && expr.left.value == 0) {
    expr.right
} else if (expr is Expr.Operate && expr.opName == "+" && expr.right is Expr.Num && expr.right.value == 0) {
    expr.left
} else expr

// 用 when 代替 if else
fun simplifyExpr1(expr: Expr): Expr = when {
    (expr is Expr.Operate) && (expr.opName == "+") && (expr.left is Expr.Num) && (expr.left.value == 0) -> expr.right
    (expr is Expr.Operate) && (expr.opName == "+") && (expr.right is Expr.Num) && (expr.right.value == 0) -> expr.left
    else -> expr
}

//使用 when 的特性：与前面实现的方法相比，的确简洁了许多，并且也能够很清楚地推断出将要匹配的表达式的结构是什么样的。
//但是上面的实现还是需要我们去写判断类型的语句，而且当嵌套的层数变多的时候，单纯使用 when 表达式还是显得无力。
fun simplifyExpr2(expr: Expr): Expr = when (expr) {
    is Expr.Num -> expr
    is Expr.Operate -> when (expr) {
        Expr.Operate("+", Expr.Num(0), expr.right) -> expr.right
        Expr.Operate("+", expr.left, Expr.Num(0)) -> expr.left
        else -> expr
    }
}

//通过使用递归的方式实现更复杂的匹配
fun simplifyExpr3(expr: Expr): Expr = when (expr) {
    is Expr.Num -> expr
    is Expr.Operate -> when (expr) {
        Expr.Operate("+", Expr.Num(0), expr.right) -> simplifyExpr(expr.right)
        Expr.Operate("+", expr.left, Expr.Num(0)) -> expr.left
        else -> expr
    }
}

//但是实际业务中的数据结构可能并不像上面那样对称，有可能在某些情况下，我们必须访问两层结构，并且用递归又实现不了。
//对于上面的例子，如果不用递归，采用 Kotlin 的 when 表达式我们只能这样去写：
fun simplifyExpr4(expr: Expr): Expr = when (expr) {
    is Expr.Num -> expr
    is Expr.Operate -> when {
        (expr.left is Expr.Num && expr.left.value == 0) && (expr.right is Expr.Operate) ->
            when (expr.right) {
                Expr.Operate("+", expr.right.left, Expr.Num(0)) -> expr.right.left
                else -> expr.right
            }
        else -> expr
    }
}

//2.5 增强的模式匹配
//更多参考资料：
// https://infoscience.epfl.ch/record/98468/files/MatchingObjectsWithPatterns-TR.pdf
// https://kotlin.link/articles/Improved-Pattern-Matching-in-Kotlin.html

object expr1 {

    sealed class Expr {

        abstract fun isZero(): Boolean
        abstract fun isAddZero(): Boolean
        data class Num(val value: Int) : Expr() {
            override fun isZero(): Boolean = this.value == 0
            override fun isAddZero(): Boolean = false
        }

        data class Operate(val opName: String, val left: Expr, val right: Expr) : Expr() {
            override fun isZero(): Boolean = false
            override fun isAddZero(): Boolean = this.opName == "+" && (this.left.isZero() || this.right.isZero())
        }
    }
}

object expr2 {
    sealed class Expr {
        abstract fun isZero(): Boolean
        abstract fun isAddZero(): Boolean
        abstract fun left(): Expr
        abstract fun right(): Expr
        data class Num(val value: Int) : Expr() {
            override fun isZero(): Boolean = this.value == 0
            override fun isAddZero(): Boolean = false
            override fun left(): Expr = throw Throwable("no element")
            override fun right(): Expr = throw Throwable("no element")
        }

        data class Operate(val opName: String, val left: Expr, val right: Expr) : Expr() {
            override fun isZero(): Boolean = false
            override fun isAddZero(): Boolean = this.opName == "+" && (this.left.isZero() || this.right.isZero())
            override fun left(): Expr = this.left
            override fun right(): Expr = this.right
        }
    }

    fun simplifyExpr(expr: Expr): Expr = when {
        expr.isAddZero() && expr.right().isAddZero() && expr.right().left().isZero() -> expr.right().right()
        else -> expr
    }
}

object expr3 {
    sealed class Expr {
        abstract fun accept(v: Visitor): Boolean
        class Num(val value: Int) : Expr() {
            override fun accept(v: Visitor): Boolean = v.visit(this)
        }

        class Operate(val opName: String, val left: Expr, val right: Expr) : Expr() {
            override fun accept(v: Visitor): Boolean = v.visit(this)
        }
    }

    class Visitor {
        fun visit(expr: Expr.Num): Boolean = false
        fun visit(expr: Expr.Operate): Boolean = when (expr) {
            Expr.Operate("+", Expr.Num(0), expr.right) -> true
            Expr.Operate("+", expr.left, Expr.Num(0)) -> true
            else -> false
        }
    }
}

object expr4 {
    sealed class Expr {
        abstract fun isZero(v: Visitor): Boolean
        abstract fun isAddZero(v: Visitor): Boolean
        abstract fun simplifyExpr(v: Visitor): Expr

        class Num(val value: Int) : Expr() {
            override fun isZero(v: Visitor): Boolean = v.matchZero(this)
            override fun isAddZero(v: Visitor): Boolean = v.matchAddZero(this)
            override fun simplifyExpr(v: Visitor): Expr =
                v.doSimplifyExpr(this)
        }

        class Operate(val opName: String, val left: Expr, val right: Expr) : Expr() {
            override fun isZero(v: Visitor): Boolean = v.matchZero(this)

            override fun isAddZero(v: Visitor): Boolean = v.matchAddZero(this)
            override fun simplifyExpr(v: Visitor): Expr = this
        }
    }

    class Visitor {
        fun matchAddZero(expr: Expr.Num): Boolean = false
        fun matchAddZero(expr: Expr.Operate): Boolean = when (expr) {
            Expr.Operate("+", Expr.Num(0), expr.right) -> true
            Expr.Operate("+", expr.left, Expr.Num(0)) -> true
            else -> false
        }

        fun matchZero(expr: Expr.Num): Boolean = expr.value == 0
        fun matchZero(expr: Expr.Operate): Boolean = false
        fun doSimplifyExpr(expr: Expr.Num): Expr = expr

        fun doSimplifyExpr(expr: Expr.Operate, v: Visitor): Expr = when {
            (expr.right is Expr.Num && v.matchAddZero(expr)
                    && v.matchAddZero(expr.right))
                    && (expr.right is Expr.Operate && expr.right.left is Expr.Num)
                    && v.matchZero(expr.right.left) -> expr.right.left
            else -> expr
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// 模式匹配实战
///////////////////////////////////////////////////////////////////////////
/*
需求：
    · 优惠券有多种类型，如现金券、礼品券及折扣券；
    · 现金券能够实现“满多少金额减多少金额”，礼品券能够通过券上标明的礼品来兑换相应礼物，折扣券表示用户能够享受多少折扣；
    · 用户可以领取优惠券，领取之后也可以使用优惠；
    · 优惠券的使用时间是可以指定在某一个特定时间段内；
    · 如果优惠券在特定的时间内没有使用的话就会过期。
 */

//糟糕的设计：如果我们定义的优惠券很复杂，比如折扣券中还包含一些它特有的属性，代金券也是如此，那么冗余的属性就会非常多，这将使我们的代码看上去非常地臃肿。
//而且如果我们后期想增加一种优惠券类型，那么我们就需要修改整个Coupon的结构，这将使我们的开发成本变得很大。
// 并且代码也将变得不是很安全，因为Coupon类有可能在很多个地方被实例化，那就意味着，这些地方都需要被修改。
data class BadCoupon(
    //通用属性
    val id: String,
    val type: Int,
    //满减时使用
    val leastCost: Long?,
    val reduceCost: Long?,
    //折扣券时使用
    val discount: Int?,
    //礼品券时使用
    val gift: String?
)

//利用ADT的思想去重新抽象一下Coupon
sealed class Coupon {

    companion object {
        const val CashType = "CASH"
        const val DiscountType = "DISCOUNT"
        const val GiftType = "GIFT"
    }

    class CashCoupon(val id: Long, val type: String, val leastCost: Long, val reduceCost: Long) : Coupon()
    class DiscountCoupon(val id: Long, val type: String, val discount: Int) : Coupon()
    class GiftCoupon(val id: Long, val type: String, val gift: String) : Coupon()
}

data class User(val name: String)

sealed class CouponStatus {
    data class StatusNotFetched(val coupon: Coupon) : CouponStatus()
    data class StatusFetched(val coupon: Coupon, val user: User) : CouponStatus()
    data class StatusUsed(val coupon: Coupon, val user: User) : CouponStatus()
    data class StatusExpired(val coupon: Coupon) : CouponStatus()
    data class StatusUnAvailable(val coupon: Coupon) : CouponStatus()
}