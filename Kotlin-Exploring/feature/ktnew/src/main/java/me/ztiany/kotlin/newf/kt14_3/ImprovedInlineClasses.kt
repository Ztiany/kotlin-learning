package me.ztiany.kotlin.newf.kt14_3

@JvmInline
private value class Negative(val x: Int) {
    //Inline classes can have init blocks. You can add code to be executed right after the class is instantiated:
    init {
        require(x < 0) { }
    }
}