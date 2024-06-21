package me.ztiany.kotlin.arrow.monad

import arrow.core.Option
import arrow.core.Some
import kotlin.test.Test

/**
 * References:
 *
 *  - [Kotlin Arrow KT Monad: A Beginner’s Guide 🚀](https://adventures92.medium.com/kotlin-arrow-kt-monads-beginners-guide-6787770717d8)
 */
class WhatIsMonad {

    @Test
    fun testOption() {
        val maybeNumber: Option<Int> = Some(10)
        val maybeCalculation: Option<Int> = maybeNumber.map { it * 2 }
        println(maybeCalculation) // Some(20)
    }

}
