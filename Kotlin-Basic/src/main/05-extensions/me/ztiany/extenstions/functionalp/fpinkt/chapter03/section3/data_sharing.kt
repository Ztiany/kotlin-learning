package me.ztiany.extenstions.functionalp.fpinkt.chapter03.section3

import me.ztiany.extenstions.functionalp.fpinkt.chapter03.section2.Cons
import me.ztiany.extenstions.functionalp.fpinkt.chapter03.section2.List
import me.ztiany.extenstions.functionalp.fpinkt.chapter03.section2.Nil


///////////////////////////////////////////////////////////////////////////
// Data sharing in functional data structures
///////////////////////////////////////////////////////////////////////////

/*
When data is immutable, how do we write functions that, for example, add elements to or remove them from a list? The answer is simple.

When we add an element 1 to the front of an existing list—say, xs—we return a new list, in this case Cons(1,xs). Since lists are immutable,
we don’t need to actually copy xs; we can just reuse it. This is called data sharing.
 */
private fun <A> append(a1: List<A>, a2: List<A>): List<A> =
    when (a1) {
        is Nil -> a2
        is Cons -> Cons(a1.head, append(a1.tail, a2))
    }

// In the same way, to remove an element from the front of a list, mylist = Cons(x,xs), we simply return its tail, xs.
private fun <A> tail(xs: List<A>): List<A> =
    when (xs) {
        is Nil -> throw IllegalStateException("tail called on empty list")
        is Cons -> xs.tail
    }