package me.ztiany.kotlin.function

/**
 *  Refer to：[In Kotlin, loops are deprecated](https://medium.com/@lucgirardin/in-kotlin-loops-are-deprecated-dae88cd5ae9c)
 */
fun main() {

    val fruits = mutableListOf("Blueberry", "Banana", "Orange", "Apple", "Strawberry", "Cherry")

    // What if the fruits should be sorted by length, but those of the same length should be sorted alphabetically?
    fruits.sortWith(compareBy<String> { it.length }.thenBy { it })

    println(fruits)
}