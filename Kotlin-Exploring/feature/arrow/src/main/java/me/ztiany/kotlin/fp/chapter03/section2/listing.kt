package me.ztiany.kotlin.fp.chapter03.section2

import kotlin.random.Random

///////////////////////////////////////////////////////////////////////////
// Working with functional data structures: how to define functional data structure and how to use it.
///////////////////////////////////////////////////////////////////////////

//tag::init1[]
sealed class List<out A> { // <1>

    companion object { // <2>

        // 构建器：根据数组生成链表
        fun <A> of(vararg aa: A): me.ztiany.kotlin.fp.chapter03.section2.List<A> { // <3>
            val tail = aa.sliceArray(1 until aa.size)
            return if (aa.isEmpty()) me.ztiany.kotlin.fp.chapter03.section2.Nil else me.ztiany.kotlin.fp.chapter03.section2.Cons(
                aa[0],
                me.ztiany.kotlin.fp.chapter03.section2.List.Companion.of(*tail)
            )
        }

        // the sum function states that the sum of an empty list is 0, and the sum of a nonempty list is the first element plus the sum of the remaining elements.
        //tag::sum[]
        fun sum(ints: me.ztiany.kotlin.fp.chapter03.section2.List<Int>): Int =
            when (ints) {
                is me.ztiany.kotlin.fp.chapter03.section2.Nil -> 0
                is me.ztiany.kotlin.fp.chapter03.section2.Cons -> ints.head + me.ztiany.kotlin.fp.chapter03.section2.List.Companion.sum(
                    ints.tail
                )
            }
        //end::sum[]

        // the product function states that the product of an empty list is 1.0, the product of any list starting with 0.0 is 0.0, and the product of any other nonempty list
        // is the first element multiplied by the product of the remaining elements.
        //tag::product[]
        fun product(doubles: me.ztiany.kotlin.fp.chapter03.section2.List<Double>): Double =
            when (doubles) {
                is me.ztiany.kotlin.fp.chapter03.section2.Nil -> 1.0
                is me.ztiany.kotlin.fp.chapter03.section2.Cons ->
                    if (doubles.head == 0.0) 0.0
                    else doubles.head * me.ztiany.kotlin.fp.chapter03.section2.List.Companion.product(doubles.tail)
            }

        //end::product[]
    }
}
//end::init1[]

object Nil : me.ztiany.kotlin.fp.chapter03.section2.List<Nothing>() {
    override fun toString(): String = "Nil"
}

//tag::init7[]
data class Cons<out A>(val head: A, val tail: me.ztiany.kotlin.fp.chapter03.section2.List<A>) : me.ztiany.kotlin.fp.chapter03.section2.List<A>()
//end::init7[]

//tag::init2[]
fun <A> of(vararg aa: A): me.ztiany.kotlin.fp.chapter03.section2.List<A> {
    val tail = aa.sliceArray(1 until aa.size)
    return if (aa.isEmpty()) me.ztiany.kotlin.fp.chapter03.section2.Nil else me.ztiany.kotlin.fp.chapter03.section2.Cons(
        aa[0],
        me.ztiany.kotlin.fp.chapter03.section2.List.Companion.of(*tail)
    )
}
//end::init2[]

//tag::init3[]
val ints = me.ztiany.kotlin.fp.chapter03.section2.List.Companion.of(1, 2, 3, 4) // <1>

fun sum(xs: me.ztiany.kotlin.fp.chapter03.section2.List<Int>): Int =
    when (xs) {
        is me.ztiany.kotlin.fp.chapter03.section2.Nil -> 0 // <2>
        is me.ztiany.kotlin.fp.chapter03.section2.Cons -> xs.head + me.ztiany.kotlin.fp.chapter03.section2.sum(xs.tail) // <3>
    }

fun main() = me.ztiany.kotlin.fp.chapter03.section2.sum(me.ztiany.kotlin.fp.chapter03.section2.ints) // <4>
//end::init3[]

val listing35 = {
    //tag::init4[]
    val x = Random.nextInt(-10, 10)
    val y: String =
        if (x == 0) { // <1>
            "x is zero"
        } else if (x < 0) { // <2>
            "is negative"
        } else { // <3>
            "x is positive"
        }
    //end::init4[]
}

val listing36 = {
    //tag::init5[]
    val x = Random.nextInt(-10, 10)
    val y: String =
        when { // <1>
            x == 0 -> // <2>
                "x is zero"
            x < 0 -> // <2>
                "x is negative"
            else -> // <3>
                "x is positive"
        }
    //end::init5[]
}

val listing37 = {
    //tag::init6[]
    fun sum(xs: me.ztiany.kotlin.fp.chapter03.section2.List<Int>): Int =
        when (xs) {
            is me.ztiany.kotlin.fp.chapter03.section2.Nil -> 0
            is me.ztiany.kotlin.fp.chapter03.section2.Cons -> xs.head + sum(xs.tail) // <1>
        }
    //end::init6[]
}
