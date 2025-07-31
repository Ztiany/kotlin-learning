package me.ztiany.kotlin.newf.kt14_3


/**
 * Java 14 中引入了 record class，其目的和 Kotlin 中的 data class 类似，都是作为数据的简单存储。
 * Java record 并不遵循 JavaBean 的规范，在 JavaBean 中的 Getter 方法为 getX()和 getY()，而 record class 中则变为了 x()和 y()。
 *
 * 在 Kotlin 中调用 record class 和 JavaBean 类似：同样也可以通过@JvmRecord 注解将 Kotlin 中的 data class 转为 record class 来给 Java 调用，
 * 这样生成的 Getter 方法就变成 x()而不是 getX()。
 */
//@JvmRecord 要求 JDK15
private data class User(val name: String, val age: Int)
