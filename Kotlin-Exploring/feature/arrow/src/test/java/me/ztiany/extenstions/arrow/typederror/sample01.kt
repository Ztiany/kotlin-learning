package me.ztiany.extenstions.arrow.typederror

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.*
import arrow.core.right
import io.kotest.matchers.shouldBe
import kotlin.test.DefaultAsserter.fail

data class UserNotFound(val message: String)

data class User(val id: Long)

///////////////////////////////////////////////////////////////////////////
// Defining the success / happy path
///////////////////////////////////////////////////////////////////////////

val user: Either<UserNotFound, User> = User(1).right()

fun Raise<UserNotFound>.user(): User = User(1)

///////////////////////////////////////////////////////////////////////////
// Raising an error
///////////////////////////////////////////////////////////////////////////

val error: Either<UserNotFound, User> = UserNotFound("").left()

fun Raise<UserNotFound>.error(): User = raise(UserNotFound(""))

///////////////////////////////////////////////////////////////////////////
// Usage
///////////////////////////////////////////////////////////////////////////

fun User.isValid(): Either<UserNotFound, Unit> = either {
    ensure(id > 0) { UserNotFound("User without a valid id: $id") }
}

fun Raise<UserNotFound>.isValid(user: User): User {
    ensure(user.id > 0) { UserNotFound("User without a valid id: ${user.id}") }
    return user
}

fun example1() {
    User(-1).isValid() shouldBe UserNotFound("User without a valid id: -1").left()

    fold(
        { isValid(User(1)) },
        { _: UserNotFound -> fail("No logical failure occurred!") },
        { user: User -> user.id shouldBe 1 }
    )
}

context(Raise<UserNotFound>)
fun User.isValidA(): Unit = ensure(id > 0) { UserNotFound("User without a valid id: $id") }

fun process(user: User?): Either<UserNotFound, Long> = either {
    ensureNotNull(user) { UserNotFound("Cannot process null user") }
    user.id // smart-casted to non-null
}

fun Raise<UserNotFound>.process(user: User?): Long {
    ensureNotNull(user) { UserNotFound("Cannot process null user") }
    return user.id // smart-casted to non-null
}

fun example2() {
    process(null) shouldBe UserNotFound("Cannot process null user").left()

    fold(
        { process(User(1)) },
        { _: UserNotFound -> fail("No logical failure occurred!") },
        { i: Long -> i shouldBe 1L }
    )
}

///////////////////////////////////////////////////////////////////////////
// Running and inspecting results
///////////////////////////////////////////////////////////////////////////
fun example3() {
    when (error) {
        is Either.Left -> error.value shouldBe UserNotFound("")
        is Either.Right -> fail("A logical failure occurred!")
    }

    fold(
        block = {
            error()
        },
        recover = { e: UserNotFound -> e shouldBe UserNotFound("") },
        transform = { _: User -> fail("A logical failure occurred!") }
    )
}