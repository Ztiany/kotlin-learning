package me.ztiany.extenstions.functionalp.fpinkt.chapter03.section2

///////////////////////////////////////////////////////////////////////////
// Pattern matching and how it differs from Kotlin matching（讨论模式匹配以及 Kotlin 中模式匹配的不足）
///////////////////////////////////////////////////////////////////////////

/*
Matching in Kotlin is not perfect and falls short of what other languages offer in this space.
Languages such as Haskell, Scala, and Rust provide a feature called pattern matching.
This is remarkably similar to what we’ve seen in Kotlin’s matching but has better semantics, more abilities, and improved usability versus that offered by Kotlin’s approach.

Let’s compare the matching provided by Kotlin’s when construct and how these other languages handle matching, to highlight these deficiencies.

Pattern matching gives us the ability to not only match on a logic expression but also to extract values from that expression. This extraction, or destructuring, plays a vital
role in FP, mainly when working with algebraic data types. To fully understand how pattern matching works, let’s take a closer look at how we would write this code in Kotlin using when and then how we wish we could write it using some Kotlin pseudocode that applies the pattern-matching technique.
First, let’s revisit the sum function we wrote in the companion object of our List class.
 */

/*
First, let’s revisit the sum function we wrote in the companion object of our List class.

The most noticeable problem is that we are accessing the value xs inside the evaluation of our branch logic by members as xs.head and xs.tail.
Notice that xs is declared a List, which has no head or tail. The fact that List has been smartcast to Cons is never explicitly stated,
which causes confusion about the ambiguous type of xs.
 */
//tag::init1[]
fun <A> append(a1: List<A>, a2: List<A>): List<A> =
    when (a1) {
        is Nil -> a2
        is Cons -> Cons(a1.head, append(a1.tail, a2))
    }
//end::init1[]

/*
If Kotlin supported pattern matching as provided by other languages, it would allow us to express this as in the following Kotlin pseudocode.

fun sum(xs: List): Int = when(xs) {
    case Nil -> 0 //
    case Cons(head, tail) -> head + sum(tail)
}

What is most noticeable is the new case keyword followed by a pattern declaration: in this case, Cons(head, tail). This pattern is first to be matched and then applied. When the code is executed, each branch pattern is applied to the object parameter of when in sequence. When the branch doesn’t match, it is simply passed over. When a match is found, the pattern is applied, extracting any declared fields of that object and
making them available on the right-hand side of that particular branch.


Even though many users have asked for pattern matching to be included in the Kotlin language (for example, see https://discuss.kotlinlang.org/t/destructuring-inwhen/2391),
the creators have taken a strong stance against it, claiming that it would make the language too complex.
We sincerely hope that it will be included in the language at a future date.
 */

