package me.ztiany.kotlin.newf.kt13


fun main() {
    // You can define unsigned types using literal suffixes
    val uint = 42u
    val ulong = 42uL
    val ubyte: UByte = 255u

// You can convert signed types to unsigned and vice versa via stdlib extensions:
    val int = uint.toInt()
    val byte = ubyte.toByte()
    val ulong2 = byte.toULong()

// Unsigned types support similar operators:
    val x = 20u + 22u
    val y = 1u shl 8
    val z = "128".toUByte()
    val range = 1u..5u
}