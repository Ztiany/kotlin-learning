package me.ztiany.newf.kt13

/*
With Kotlin 1.3, it is possible to mark members of a companion object of interfaces with annotations @JvmStatic and @JvmField. In the classfile,
such members will be lifted to the corresponding interface and marked as static.
 */
interface Foo {

    companion object {
        @JvmField
        val answer: Int = 42

        @JvmStatic
        fun sayHello() {
            println("Hello, world!")
        }
    }

}