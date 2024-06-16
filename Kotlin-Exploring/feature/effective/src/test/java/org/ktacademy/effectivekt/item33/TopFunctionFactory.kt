package org.ktacademy.effectivekt.item33

//item33：2 使用【顶层工厂方法】

///////////////////////////////////////////////////////////////////////////
// example01：在Kotlin中，更多的是定义顶层函数而不是CFM(伴生对象工厂方法)。比如一些常见的例子listOf，setOf和mapOf
///////////////////////////////////////////////////////////////////////////
interface UserDao {

}

fun createUserDao(): UserDao {
    return object : UserDao {

    }
}