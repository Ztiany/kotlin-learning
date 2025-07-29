package me.ztiany.kotlin.newf.kt18_2

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * We've added support for Base64 encoding and decoding. We provide 3 class instances, each using
 * different encoding schemes and displaying different behaviors.
 */
@OptIn(ExperimentalEncodingApi::class)
fun main() {
    val foBytes = "fo".map { it.code.toByte() }.toByteArray()
    Base64.Default.encode(foBytes) // "Zm8="
    // Alternatively:
    // Base64.encode(foBytes)

    val foobarBytes = "foobar".map { it.code.toByte() }.toByteArray()
    Base64.UrlSafe.encode(foobarBytes) // "Zm9vYmFy"

    Base64.Default.decode("Zm8=") // foBytes
    // Alternatively:
    // Base64.decode("Zm8=")

    Base64.UrlSafe.decode("Zm9vYmFy") // foobarBytes
}