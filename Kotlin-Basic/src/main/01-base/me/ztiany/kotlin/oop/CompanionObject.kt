package me.ztiany.kotlin.oop


/**
 * **伴生对象**：类内部的对象声明可以用 companion 关键字标记
 *
 * 1，伴生对象的成员可通过只使用类名作为限定符来调用。
 * 2，可以省略伴生对象的名称，在这种情况下将使用名称 Companion。
 * 3，即使伴生对象的成员看起来像其他语言的静态成员，在运行时他们仍然是真实对象的实例成员，而且还可以实现接口。
 * 4，在 JVM 平台，如果使用 @JvmStatic 注解，你可以将伴生对象的成员生成为真正的 静态方法和字段。
 * 5，每个类，只能有一个伴生对象。
 *
 *  **为什么舍弃 static，而设计伴生对象**？
 *
 * 1. static 是很常见的 Java 代码，也许你已经习惯了。但是如果仔细思考，会发现这种语法其实并不是非常好。因为在一个类中既有静态变量、静态方法，也有普通变量、普通方法的声明。
 * 然而，静态变量和静态方法是属于一个类的，普通变量、普通方法是属于一个具体对象的。虽然有 static 作为区分，然而在代码结构上职能并不是区分得很清晰。
 * 2. “伴生”是相较于一个类而言的，意为伴随某个类的对象，它属于这个类所有，因此伴生对象跟 Java 中 static 修饰效果性质一样，全局只有一个单例。它需要声明在类的内部，在类被装载时会被初始化。
 * 3. companion object 用花括号包裹了所有静态属性和方法，使得它可以与 Prize 类的普通方法和属性清晰地区分开来。
 * 4. 伴生对象的另一个作用是可以实现工厂方法模式。
 */
private class CompanionClass1 {
    companion object {
        val x: Int = 100
    }
}

private class CompanionClass2 {
    companion object Factory {
        fun create(): CompanionClass2 = CompanionClass2()
    }
}

private fun testCompanion() {
    CompanionClass1.x
    CompanionClass2.create()
}
