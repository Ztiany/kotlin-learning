package org.ktacademy.effectivekt.item33

import org.junit.Test

//item33：1 使用【伴生工厂方法】
class Usage1 {

    @Test
    fun test01() {
        usage01()
    }

    @Test
    fun test02() {
        usage02()
    }

    @Test
    fun test03() {
        usage03()
    }

}

///////////////////////////////////////////////////////////////////////////
//example01
///////////////////////////////////////////////////////////////////////////
private class MyList {
    //在Kotlin中不允许有static关键字修饰的方法，类似于Java中的静态工厂方法通常被称为伴生工厂方法，它是一个放在伴生对象中的工厂方法。
    companion object {
        fun of(vararg args: Int): MyList {
            /*...*/
            return MyList()
        }
    }
}

private fun usage01() {
    MyList.of(1)
}

///////////////////////////////////////////////////////////////////////////
// example02
///////////////////////////////////////////////////////////////////////////

/*
在底层实现上，伴生对象实际上就是个单例类，它有个很大的优点就是Companion对象可以继承其他的类。
这样我们就可以实现多个通用工厂方法并为它们提供不同的类。使用的常见例子是Provider类，我用它作为DI的替代品。
 */
abstract class Provider<T> {
    var original: T? = null
    var mocked: T? = null
    abstract fun create(): T

    fun get(): T = mocked ?: original ?: create().apply { original = this }

    fun lazyGet(): Lazy<T> = lazy { get() }
}

data class User(val name: String)

interface UserRepository {
    fun getUser(): User

    companion object : Provider<UserRepository>() {
        override fun create() = UserRepositoryImpl()
    }
}

class UserRepositoryImpl : UserRepository {
    override fun getUser(): User {
        return User("Alien")
    }
}

class UserRepositoryImplForTest : UserRepository {
    override fun getUser(): User {
        return User("Tester")
    }
}

@Test
private fun usage02() {
    UserRepository.mocked = UserRepositoryImplForTest()
    val userRepository = UserRepository.get()
    println(userRepository.getUser().name)
}

///////////////////////////////////////////////////////////////////////////
// example03
///////////////////////////////////////////////////////////////////////////

interface Dependency<T> {
    var mocked: T?
    fun get(): T
    fun lazyGet(): Lazy<T> = lazy { get() }
}

abstract class ProviderV2<T>(val init: () -> T) : Dependency<T> {
    var original: T? = null
    override var mocked: T? = null
    override fun get(): T = mocked ?: original ?: init().apply { original = this }
}

class UserRepositoryImplV2 : UserRepositoryV2 {
    override fun getUser(): User {
        return User("AlienV2")
    }
}

interface UserRepositoryV2 {
    fun getUser(): User

    companion object :
        Dependency<UserRepositoryV2> by object : ProviderV2<UserRepositoryV2>({ UserRepositoryImplV2() }) {

        }
}

private fun usage03() {
    val repository by UserRepositoryV2.lazyGet()
    println(repository.getUser())
}

///////////////////////////////////////////////////////////////////////////
// example04：伴生对象扩展工厂方法
///////////////////////////////////////////////////////////////////////////
interface Tool {
    companion object {

    }
}

class BigTool : Tool

fun Tool.Companion.createBigTool(name: String): Tool {
    return BigTool()
}