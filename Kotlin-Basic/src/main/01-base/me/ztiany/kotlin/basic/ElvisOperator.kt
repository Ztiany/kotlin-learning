package me.ztiany.kotlin.basic

import java.io.File

/**
 *Elvis 操作符
 */
fun main() {
    val files = File("Test").listFiles()

    println(files?.size ?: "empty") // if files is null, this prints "empty"

    // To calculate the fallback value in a code block, use `run`
    val filesSize = files?.size ?: run {
        return
    }
}

private class User {
    fun save() {}
}

/*
* 对空值的处理
* */
private fun testElvis(input: String?, user: User?) {
    val a: Int?

    if (input == null) {
        a = -1
    } else {
        a = input?.length
    }

    if (user == null) {
        val newOne = User()
        newOne.save()
    } else {
        user.save()
    }
}

/**
 * Elvis操作符 ?: 简化对空值的处理
 */
private fun testElvis2(input: String?, user: User?) {
    val b = input?.length ?: -1
    //?:符号会在符号左边为空的情况才会进行下面的处理，不为空则不会有任何操作。
    user?.save() ?: User().save()
}