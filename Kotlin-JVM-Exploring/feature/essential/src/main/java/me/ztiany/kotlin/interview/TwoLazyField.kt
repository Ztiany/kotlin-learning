package me.ztiany.kotlin.interview

private class TwoLazyField {

    val first: String by lazy {
        println("first")
        "first$second"
    }

    val second: String by lazy {
        println("second")
        "second$first"
    }

}

fun main() {
    val twoLazyField = TwoLazyField()
    println(twoLazyField.first)
    println(twoLazyField.second)
}