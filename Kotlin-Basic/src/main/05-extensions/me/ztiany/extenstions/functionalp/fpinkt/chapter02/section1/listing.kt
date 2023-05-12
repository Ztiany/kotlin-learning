package me.ztiany.extenstions.functionalp.fpinkt.chapter02.section1

///////////////////////////////////////////////////////////////////////////
// A short detour: Writing loops functionally
///////////////////////////////////////////////////////////////////////////

/*
To adapt our existing program to demonstrate HOFs, we need to introduce some new behavior.

We will do so by adding a new function that calculates the nth factorial.

To write this simple function, we will first take a short detour by showing how loops are written in a purely functional way. We do this by introducing recursion.
 */

val listing1 = {

    //tag::init1[]
    fun factorial(i: Int): Int {

        // It is common to write functions that are local to the body of another function. In functional programming, we shouldn’t consider this any stranger
        // than a local integer or string.
        fun go(n: Int, acc: Int): Int = // <1>
            if (n <= 0) acc
            else go(n - 1, n * acc)

        return go(i, 1) // <2>
    }
    //end::init1[]
}

val listing2 = {

    //tag::init2[]
    fun factorial(i: Int): Int {

        tailrec fun go(n: Int, acc: Int): Int = // <1>
            if (n <= 0) acc
            else go(n - 1, n * acc) // <2>

        return go(i, 1)
    }
    //end::init2[]
}

//tag::init3[]
object Example {

    private fun abs(n: Int): Int =
        if (n < 0) -n
        else n

    private fun factorial(i: Int): Int { //<1>

        fun go(n: Int, acc: Int): Int =
            if (n <= 0) acc
            else go(n - 1, n * acc)

        return go(i, 1)
    }

    fun formatAbs(x: Int): String {
        val msg = "The absolute value of %d is %d"
        return msg.format(x, abs(x))
    }

    fun formatFactorial(x: Int): String { //<2>
        val msg = "The factorial of %d is %d"
        return msg.format(x, factorial(x))
    }
}

fun main() {
    println(Example.formatAbs(-42))
    println(Example.formatFactorial(7)) //<3>
}
//end::init3[]

///////////////////////////////////////////////////////////////////////////
// Writing our first higher-order function.
///////////////////////////////////////////////////////////////////////////

val listing4 = {
    fun factorial(i: Int): Int = TODO()

    fun abs(n: Int): Int = TODO()


    // Variable-naming conventions（参数命名规范）: It’s a standard convention to use names like f, g, and h for parameters to a HOF. In functional programming, we tend to use
    // terse variable names, even one-letter names. This is because HOFs are so general that they have no opinion on what the argument should actually do in
    // the limited scope of the function body. All they know about the argument is its type. Many functional programmers feel that short names make code easier
    // to read since they make the code structure easier to see at a glance.
    // formatResult 是一个高阶函数，它接受一个函数作为参数。
    //tag::init4[]
    fun formatResult(name: String, n: Int, f: (Int) -> Int): String {
        val msg = "The %s of %d is %d."
        return msg.format(name, n, f(n))
    }
    //end::init4[]

    //tag::init5[]
    fun main() {
        println(formatResult("factorial", 7, ::factorial))
        println(formatResult("absolute value", -42, ::abs))
    }
    //end::init5[]
}
