package me.ztiany.kotlin.fp.chapter02.section2

///////////////////////////////////////////////////////////////////////////
// Polymorphic functions: Abstracting over types
///////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////
// monomorphic function
///////////////////////////////////////////////////////////////////////////

// For example, the following monomorphic function, findFirst, returns the first index in an array where the key occurs, or -1 if it’s not found.
//tag::init1[]
fun findFirst(ss: Array<String>, key: String): Int {

    tailrec fun loop(n: Int): Int =
        when {
            n >= ss.size -> -1 // <1>
            ss[n] == key -> n // <2>
            else -> loop(n + 1) // <3>
        }

    return loop(0) // <4>
}
//end::init1[]


///////////////////////////////////////////////////////////////////////////
// polymorphic function
///////////////////////////////////////////////////////////////////////////

/*
 相比上面的 monomorphic function，它们的逻辑是相同的，但是这里的函数可以处理任意类型的数组。

 this piece of code shows how we can write findFirst more generally for any type A by accepting a function to test a particular A value.
 请注意区分面向对象中的多态和函数式编程中的多态。函数的多态是通过参数化类型实现的，而不是通过继承。
 */
//tag::init2[]
fun <A> findFirst(xs: Array<A>, p: (A) -> Boolean): Int { // <1>

    tailrec fun loop(n: Int): Int =
        when {
            n >= xs.size -> -1
            p(xs[n]) -> n // <2>
            else -> loop(n + 1)
        }

    return loop(0)
}
//end::init2[]
