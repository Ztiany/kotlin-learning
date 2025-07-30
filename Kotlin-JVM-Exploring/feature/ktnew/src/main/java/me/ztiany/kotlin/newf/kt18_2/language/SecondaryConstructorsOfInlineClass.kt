package me.ztiany.kotlin.newf.kt18_2.language

@JvmInline
private value class Person(private val fullName: String) {
    // Allowed since Kotlin 1.4.30:
    init {
        check(fullName.isNotBlank()) {
            "Full name shouldn't be empty"
        }
    }

    // Preview available since Kotlin 1.8.20:
    constructor(name: String, lastName: String) : this("$name $lastName") {
        check(lastName.isNotBlank()) {
            "Last name shouldn't be empty"
        }
    }
}

/**
 * Kotlin 1.8.20 lifts restrictions on the use of secondary constructors with bodies in inline classes.
 *
 * Inline classes used to allow only a public primary constructor without init blocks or secondary
 * constructors to have clear initialization semantics. As a result, it was impossible to encapsulate
 * underlying values or create an inline class that would represent some constrained values.
 *
 * These issues were fixed when Kotlin 1.4.30 lifted restrictions on init blocks. Now we're taking
 * it a step further and allowing secondary constructors with bodies in preview mode:
 */
fun main() {
    val person1 = Person("Dmitry")
    val person2 = Person("Alice", "Zhan")
    println(person1)
    println(person2)
}