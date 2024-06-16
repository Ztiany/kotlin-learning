package me.ztiany.kotlin.newf.kt17

/**
 * Kotlin 1.7.0 introduces an underscore operator, _, for type arguments. You can use it to automatically infer a type argument when other types are specified:
 */
fun main() {
    // T is inferred as String because SomeImplementation derives from SomeClass<String>
    val s = Runner.run<SomeImplementation, _>()
    assert(s == "Test")

    // T is inferred as Int because OtherImplementation derives from SomeClass<Int>
    val n = Runner.run<OtherImplementation, _>()
    assert(n == 42)
}

private abstract class SomeClass<T> {
    abstract fun execute(): T
}

private class SomeImplementation : SomeClass<String>() {
    override fun execute(): String = "Test"
}

private class OtherImplementation : SomeClass<Int>() {
    override fun execute(): Int = 42
}

private object Runner {

    inline fun <reified S : SomeClass<T>, T> run(): T {
        return S::class.java.getDeclaredConstructor().newInstance().execute()
    }

}
