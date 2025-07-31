package me.ztiany.kotlin.fp.chapter03.section4

/*
3.6: Can product, implemented using foldRight, immediately halt the recursion and return 0.0 if it encounters a 0.0?
Why or why not? Consider how any short-circuiting might work if you call foldRight with a large list.
This question has deeper implications that we will return to in chapter 5.

【这是一个关于优化的问题，我的答案是可以】
 */

/*
 3.7: See what happens when you pass Nil and Cons to foldRight, like this (the type annotation Nil as List<Int> is needed here because,
 otherwise, Kotlin infers the B type parameter in foldRight as List<Nothing>):

        foldRight(
            Cons(1, Cons(2, Cons(3, Nil))),
            Nil as List<Int>,
            { x, y ->
                Cons(x, y)
            }
        )

 What do you think this says about the relationship between foldRight and the data constructors of List?

 Simply passing in Nil is not sufficient as we lack the type information of A in this context.
 As a result, we need to express this as Nil as List<Int>. Since this is very verbose, a convenience method to circumvent it can be added to the companion object:

        fun <A> empty(): List<A> = Nil

 This method will be used in all subsequent listings and exercises to represent an empty List.
 */


// 3.8: Compute the length of a list using foldRight.
fun <A> lengthByFoldRight(xs: List<A>): Int {
    return foldRight(xs, 0) { _, acc -> acc + 1 }
}

fun <A> length(xs: List<A>): Int {
    return when (xs) {
        is Nil -> 0
        is Cons -> 1 + length(xs.tail)
    }
}
