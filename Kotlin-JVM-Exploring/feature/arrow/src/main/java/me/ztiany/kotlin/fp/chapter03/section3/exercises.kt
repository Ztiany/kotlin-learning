package me.ztiany.kotlin.fp.chapter03.section3


import me.ztiany.kotlin.fp.chapter03.section2.Cons
import me.ztiany.kotlin.fp.chapter03.section2.List
import me.ztiany.kotlin.fp.chapter03.section2.Nil

/*
 3.1: Implement the function tail for removing the first element of a List.

 Note that the function takes constant time. What different choices can you make in your implementation if the List is Nil?

 We’ll return to this question in the next chapter.
 */
private fun <A> tail(xs: me.ztiany.kotlin.fp.chapter03.section2.List<A>): me.ztiany.kotlin.fp.chapter03.section2.List<A> =
    when (xs) {
        is me.ztiany.kotlin.fp.chapter03.section2.Nil -> throw IllegalStateException("tail called on empty list")
        is me.ztiany.kotlin.fp.chapter03.section2.Cons -> xs.tail
    }

/*
 3.2: Using the same idea as in the previous exercise, implement the function setHead for replacing the first element of a List with a different value.
 */
private fun <A> setHead(xs: me.ztiany.kotlin.fp.chapter03.section2.List<A>, x: A): me.ztiany.kotlin.fp.chapter03.section2.List<A> =
    when (xs) {
        is me.ztiany.kotlin.fp.chapter03.section2.Nil -> throw IllegalStateException("setHead called on empty list")
        is me.ztiany.kotlin.fp.chapter03.section2.Cons -> me.ztiany.kotlin.fp.chapter03.section2.Cons(x, xs.tail)
    }

/*
 3.3: Generalize tail to the function drop, which removes the first n elements from a list. Note that this function takes time proportional only to the number of elements being
 dropped—you don’t need to make a copy of the entire List.
 */
private fun <A> drop(xs: me.ztiany.kotlin.fp.chapter03.section2.List<A>, n: Int): me.ztiany.kotlin.fp.chapter03.section2.List<A> =
    when (xs) {
        is me.ztiany.kotlin.fp.chapter03.section2.Nil -> throw IllegalStateException("drop called on empty list")
        is me.ztiany.kotlin.fp.chapter03.section2.Cons ->
            if (n == 0) xs
            else drop(xs.tail, n - 1)
    }

// 3.4: Implement dropWhile, which removes elements from the List prefix as long as they match a predicate.
fun <A> dropWhile(l: me.ztiany.kotlin.fp.chapter03.section2.List<A>, f: (A) -> Boolean): me.ztiany.kotlin.fp.chapter03.section2.List<A> = when (l) {
    is me.ztiany.kotlin.fp.chapter03.section2.Nil -> l
    is me.ztiany.kotlin.fp.chapter03.section2.Cons ->
        if (f(l.head)) dropWhile(l.tail, f)
        else l
}

/*
 3.5: Not everything works out so nicely as when we append two lists to each other. Implement a function, init, that returns a List consisting of all but the last
 element of a List. So, given List(1, 2, 3, 4), init should return List(1, 2, 3). Why can’t this function be implemented in constant time like tail?

 Due to the structure of a singly linked list, any time we want to replace the tail of a Cons, even if it’s the last Cons in the list,
 we must copy all the previous Cons objects. Writing purely functional data structures that support different operations efficiently is all about finding clever ways
 to exploit data sharing. We’re not going to cover these data structures here; for now, we’re content to use the functional data structures others have written.
 */
fun <A> init(xs: me.ztiany.kotlin.fp.chapter03.section2.List<A>): me.ztiany.kotlin.fp.chapter03.section2.List<A> = when (xs) {
    is me.ztiany.kotlin.fp.chapter03.section2.Nil -> throw IllegalStateException("init called on empty list")
    is me.ztiany.kotlin.fp.chapter03.section2.Cons ->
        if (xs.tail == me.ztiany.kotlin.fp.chapter03.section2.Nil) me.ztiany.kotlin.fp.chapter03.section2.Nil
        else me.ztiany.kotlin.fp.chapter03.section2.Cons(xs.head, init(xs.tail))
}


///////////////////////////////////////////////////////////////////////////
// Appending all elements of one list to another
///////////////////////////////////////////////////////////////////////////
/*
 Both drop and dropWhile employed data sharing to achieve their purposes. A more surprising example of data sharing is the following function,
 which adds all the elements of one list to the end of another.

 Note that this definition only copies values until the first list is exhausted, so its run time and memory usage are determined only by the length of a1.
 The remaining list then just points to a2. If we were to implement this same function for two arrays, we’d be forced to copy all the elements in both arrays
 into the result. In this case, the immutable linked list is much more efficient than an array!
*/
fun <A> appendList(a1: me.ztiany.kotlin.fp.chapter03.section2.List<A>, a2: me.ztiany.kotlin.fp.chapter03.section2.List<A>): me.ztiany.kotlin.fp.chapter03.section2.List<A> = when (a1) {
    is me.ztiany.kotlin.fp.chapter03.section2.Nil -> a2
    // 其实还是创建了新的对象，只是新的对象的 tail 指向了 a2。还是有点浪费的。
    is me.ztiany.kotlin.fp.chapter03.section2.Cons -> me.ztiany.kotlin.fp.chapter03.section2.Cons(
        a1.head,
        appendList(a1.tail, a2)
    )
}

fun main() {
    val list = me.ztiany.kotlin.fp.chapter03.section2.List.of(1, 2, 3, 4, 5, 6, 7)
    val droppedList = drop(list, 5)
    println(droppedList)
}