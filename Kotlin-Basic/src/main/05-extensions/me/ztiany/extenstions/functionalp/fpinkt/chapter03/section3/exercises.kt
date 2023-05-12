package me.ztiany.extenstions.functionalp.fpinkt.chapter03.section3


import me.ztiany.extenstions.functionalp.fpinkt.chapter03.section2.Cons
import me.ztiany.extenstions.functionalp.fpinkt.chapter03.section2.List
import me.ztiany.extenstions.functionalp.fpinkt.chapter03.section2.Nil

/*
 3.1: Implement the function tail for removing the first element of a List.

 Note that the function takes constant time. What different choices can you make in your implementation if the List is Nil?

 We’ll return to this question in the next chapter.
 */
private fun <A> tail(xs: List<A>): List<A> =
    when (xs) {
        is Nil -> throw IllegalStateException("tail called on empty list")
        is Cons -> xs.tail
    }

/*
 3.2: Using the same idea as in the previous exercise, implement the function setHead for replacing the first element of a List with a different value.
 */
private fun <A> setHead(xs: List<A>, x: A): List<A> =
    when (xs) {
        is Nil -> throw IllegalStateException("setHead called on empty list")
        is Cons -> Cons(x, xs.tail)
    }

/*
 3.3: Generalize tail to the function drop, which removes the first n elements from a list. Note that this function takes time proportional only to the number of elements being
 dropped—you don’t need to make a copy of the entire List.
 */
private fun <A> drop(xs: List<A>, n: Int): List<A> =
    when (xs) {
        is Nil -> throw IllegalStateException("drop called on empty list")
        is Cons ->
            if (n == 0) xs
            else drop(xs.tail, n - 1)
    }

// 3.4: Implement dropWhile, which removes elements from the List prefix as long as they match a predicate.
fun <A> dropWhile(l: List<A>, f: (A) -> Boolean): List<A> = when (l) {
    is Nil -> l
    is Cons ->
        if (f(l.head)) dropWhile(l.tail, f)
        else l
}

/*
 3.5: Both drop and dropWhile employed data sharing to achieve their purposes. A more surprising example of data sharing is the following function,
 which adds all the elements of one list to the end of another.
*/
fun <A> appendList(a1: List<A>, a2: List<A>): List<A> = when (a1) {
    is Nil -> a2
    // 其实还是创建了新的对象，只是新的对象的 tail 指向了 a2。还是有点浪费的。
    is Cons -> Cons(a1.head, appendList(a1.tail, a2))
}

fun main() {
    val list = List.of(1, 2, 3, 4, 5, 6, 7)
    val droppedList = drop(list, 5)
    println(droppedList)
}