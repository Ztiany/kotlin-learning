package me.ztiany.kotlin.newf.kt14

/** 支持更多的回调引用。*/
fun main() {
    println(apply(::foo1))

    foo2 { returnsInt() } // this was the only way to do it  before 1.4
    foo2(::returnsInt) // starting from 1.4, this also works
}

//References to functions with default argument values
private fun foo1(i: Int = 0): String = "$i!"

private fun apply(func: () -> String): String = func()

//Function references in Unit-returning functions
private fun foo2(f: () -> Unit) {}
private fun returnsInt(): Int = 42

//References that adapt based on the number of arguments in a function
private fun foo3(x: Int, vararg y: String) {}

private fun use0(f: (Int) -> Unit) {}
private fun use1(f: (Int, String) -> Unit) {}
private fun use2(f: (Int, String, String) -> Unit) {}

private fun test3() {
    use0(::foo3)
    use1(::foo3)
    use2(::foo3)
}

//Suspend conversion on callable references
private fun call() {}
private fun takeSuspend(f: suspend () -> Unit) {

}

private fun test4() {
    takeSuspend { call() } // OK before 1.4
    takeSuspend(::call) // In Kotlin 1.4, it also works
}