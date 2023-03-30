package me.ztiany.newf.kt14


fun main() {

}

//before 1.4
private fun test1(xs: List<Int>) {
    LOOP@ for (x in xs) {
        when (x) {
            2 -> continue@LOOP
            17 -> break@LOOP
            else -> println(x)
        }
    }
}

//1.4 and after
private fun test2(xs: List<Int>) {
    for (x in xs) {
        when (x) {
            2 -> continue
            17 -> break
            else -> println(x)
        }
    }
}