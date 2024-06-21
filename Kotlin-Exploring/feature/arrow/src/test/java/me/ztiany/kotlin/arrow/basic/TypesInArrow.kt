package me.ztiany.kotlin.arrow.basic

import arrow.core.Either
import arrow.core.Option
import kotlin.test.Test

/**
 * Reference:
 *
 *  - [Unlocking the Full Potential of Kotlin with Arrow.kt](https://kt.academy/article/unlocking-the-full-potential-of-kotlin-with-arrow)
 */
class TypesInArrow {

    /**
     * The Option type is used to represent a value that may or may not be present.
     * It can help you avoid null pointer exceptions by allowing you to safely handle nullable values.
     */
    @Test
    fun testOption() {
        val optionalValue: Option<String> = Option.fromNullable("Alien")
        val result = optionalValue.fold({ "default value" }, { it })
        println(result)
    }

    /**
     * The Either type is used to represent a value that can be one of two possible types. It is often used for error handling,
     * where the left type represents the error and the right type represents the success value.
     */
    @Test
    fun testEither() {
        val result: Either<Exception, String> = Either.Right("default")
        result.fold(
            {
                /* handle error case */
            },
            {
                /* handle success case */
                println(it)
            }
        )
    }

    /**
     * The Validated type is used to represent a value that can be either valid or invalid. It is often used for input validation and error handling.
     *
     * check out [Validation](https://arrow-kt.io/learn/typed-errors/validation/)
     */
    @Test
    fun testValidated() {

    }

    /**
     * The IO Monad is used to represent a computation that has side effects.
     * It can help you manage side effects in a functional way and make your code more testable.
     */
    fun testIOMonad() {

    }

    /**
     * Optics are a way to work with nested data structures in a functional way.
     * They provide a way to focus on a specific part of a data structure and modify it without changing the rest of the structure.
     * To use Optics, you need to define a Lens, which is a way to focus on a specific part of a data structure.
     * You can then use the Lens to modify the focused part of the data structure.
     */
    fun testOptics() {

    }

    /**
     * Monad Comprehensions provide a way to work with multiple monads in a concise and readable way.
     * They allow you to combine monads using a for comprehension, similar to how you would work with a list comprehension.
     */
    fun testMonadComprehensions() {

    }

    /**
     * ValidatedNel is a variant of Validated that collects all errors into a Non-Empty List. It is often used for input validation and error handling.
     */
    fun testValidatedNel() {

    }

}