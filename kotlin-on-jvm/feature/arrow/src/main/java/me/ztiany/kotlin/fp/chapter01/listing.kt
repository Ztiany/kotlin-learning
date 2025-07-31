package me.ztiany.kotlin.fp.chapter01

///////////////////////////////////////////////////////////////////////////
// Imperative Programming with Side Effects
///////////////////////////////////////////////////////////////////////////

// 买咖啡的例子
val listing1 = {

    class CreditCard {
        fun charge(price: Float): Unit = TODO()
    }

    data class Coffee(val price: Float = 2.50F)

    //tag::init1[]
    class Cafe {

        fun buyCoffee(cc: CreditCard): Coffee {
            val cup = Coffee() // <1>
            /*
            This is a side effect. It is not a pure function. It has a dependency on the external world. It is not referentially transparent.
            The buyCoffee shouldn't know about the CreditCard class. It also shouldn't know about the charge method.
            The dependency on CreditCard causes the buyCoffee method not testable and reusable.
             */
            cc.charge(cup.price) // <2>
            return cup // <3>
        }

    }
    //end::init1[]
}

///////////////////////////////////////////////////////////////////////////
// Functional Programming Refactoring: Removing Side Effects
///////////////////////////////////////////////////////////////////////////

val listing2 = {

    data class Coffee(val price: Float = 2.95F)

    class CreditCard

    class Payments {
        fun charge(cc: CreditCard, price: Float): Unit = TODO()
    }

    //tag::init2[]
    class Cafe {

        fun buyCoffee(cc: CreditCard, p: Payments): Coffee {
            val cup = Coffee()
            p.charge(cc, cup.price)
            return cup
        }

    }
    //end::init2[]
}

val listing3 = {

    class CreditCard

    data class Coffee(val price: Float = 2.50F)

    data class Charge(val cc: CreditCard, val amount: Float)

    //tag::init3[]
    class Cafe {

        fun buyCoffee(cc: CreditCard): Pair<Coffee, Charge> {
            val cup = Coffee()
            return Pair(cup, Charge(cc, cup.price))
        }

    }
    //end::init3[]
}

val listing4 = {
    class CreditCard

    //tag::init4[]
    data class Charge(val cc: CreditCard, val amount: Float) { // <1>

        // 合并多笔交易（一次性交易可以节省信用卡的手续费）
        fun combine(other: Charge): Charge = // <2>
            if (cc == other.cc) // <3>
                Charge(cc, amount + other.amount) // <4>
            else throw Exception(
                "Cannot combine charges to different cards"
            )

    }
    //end::init4[]
}

val listing5 = {

    class CreditCard

    data class Coffee(val price: Float = 2.50F)

    data class Charge(val cc: CreditCard, val amount: Float) {
        fun combine(other: Charge): Charge = TODO()
    }

    //tag::init5[]
    class Cafe {

        fun buyCoffee(cc: CreditCard): Pair<Coffee, Charge> = TODO()

        // 一次性购买多杯咖啡，由于 buyCoffee 是纯函数，所可以直接调用多次。
        fun buyCoffees(
            cc: CreditCard,
            n: Int
        ): Pair<List<Coffee>, Charge> {

            val purchases: List<Pair<Coffee, Charge>> = List(n) { buyCoffee(cc) } // <1>

            val (coffees, charges) = purchases.unzip() // <2>

            return Pair(
                coffees,
                charges.reduce { c1, c2 -> c1.combine(c2) }
            ) // <3>
        }
    }
    //end::init5[]
}

/*
 Making Charge into a first-class value（一等值） has other benefits we might not have anticipated: we can more easily assemble business logic for
 working with these charges.

    For instance, Alice may bring her laptop to the coffee shop and work there for a few hours, making occasional purchases.
    It might be nice if the coffee shop could combine Alice’s purchases into a single charge, again saving on credit card processing fees.
    Since Charge is first class, we can now add the following extension method to List<Charge> to coalesce any same-card charges.

 说明：First-class value（一等值）是指在编程语言中，某种数据类型（如整数、字符串、函数）能够像其他数据类型一样作为值被使用和操作。具体来说，如果一个语言中的值可以
 被存储在变量中、作为参数传递给函数、从函数中返回或者作为其他数据结构的元素，那么这个值就是一等值。这种特性也称为值的“头等公民”。

 在支持一等值的编程语言中，可以把函数当作变量来使用，可以将函数作为参数传递给其他函数，也可以从函数中返回一个函数。这种特性可以使代码更加简洁、灵活，也更加易于阅读和维护。
 许多函数式编程语言（如 Haskell、Scala、Clojure 等）都支持函数作为一等值。
 */
val listing6 = {

    class CreditCard

    data class Charge(val cc: CreditCard, val amount: Float) {
        fun combine(other: Charge): Charge = TODO()
    }

    //tag::init6[]
    fun List<Charge>.coalesce(): List<Charge> =
        this.groupBy {
            it.cc
        }.values.map {
            it.reduce { a, b ->
                a.combine(b)
            }
        }
    //end::init6[]
}
