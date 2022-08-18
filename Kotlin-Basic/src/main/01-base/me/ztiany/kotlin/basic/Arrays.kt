package me.ztiany.kotlin.basic


/** 数组类型 */
private fun createArray() {
    // 使用库函数 arrayOf() 来创建一个数组并传递元素值给它
    val intArray = arrayOf(1, 2, 3, 4, 5)
    println(intArray.contentToString())
    println(intArray[2])
    println(intArray.set(1, 2))
    println(intArray.size)

    // 库函数 arrayOfNulls() 可以用于创建一个指定大小、元素都为空的数组。
    val arrayNulls = arrayOfNulls<String>(size = 4)
    println(arrayNulls.contentToString())

    //创建一个 Array<String> 初始化为 ["0", "1", "4", "9", "16"]
    val asc = Array(5) { i -> (i * i).toString() }
    println(asc.contentToString())

    //各种基本数据类型的Array
    IntArray(10)
    ByteArray(10)
    ShortArray(10)
    LongArray(10)
    FloatArray(10)
    BooleanArray(10)
    DoubleArray(10)
}
