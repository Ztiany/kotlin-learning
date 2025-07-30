package me.ztiany.kotlin.newf.kt20_2.stdlib

/**
 * Kotlin 2.0.20 adds a new minLength property to the NumberHexFormat class, accessed through
 * `HexFormat.number`. This property lets you specify the minimum number of digits in hexadecimal
 * representations of numeric values, enabling padding with zeros to meet the required length.
 * Additionally, leading zeros can be trimmed using the removeLeadingZeros property:
 */
@OptIn(ExperimentalStdlibApi::class)
fun main() {
    println(93.toHexString(HexFormat {
        number.minLength = 8
        number.removeLeadingZeros = true
    }))
    // "005d"
}