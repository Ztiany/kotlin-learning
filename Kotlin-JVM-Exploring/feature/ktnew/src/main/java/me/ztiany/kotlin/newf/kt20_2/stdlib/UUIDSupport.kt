package me.ztiany.kotlin.newf.kt20_2.stdlib

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * otlin 2.0.20 introduces a class for representing UUIDs (universally unique identifiers) in the
 * common Kotlin standard library to address the challenge of uniquely identifying items.
 */
@OptIn(ExperimentalUuidApi::class)
fun main() {
    // Constructs a byte array for UUID creation
    val byteArray = byteArrayOf(
        0x55, 0x0E, 0x84.toByte(), 0x00, 0xE2.toByte(), 0x9B.toByte(), 0x41, 0xD4.toByte(),
        0xA7.toByte(), 0x16, 0x44, 0x66, 0x55, 0x44, 0x00, 0x00
    )

    val uuid1 = Uuid.fromByteArray(byteArray)
    val uuid2 = Uuid.fromULongs(0x550E8400E29B41D4uL, 0xA716446655440000uL)
    val uuid3 = Uuid.parse("550e8400-e29b-41d4-a716-446655440000")

    println(uuid1)
    // 550e8400-e29b-41d4-a716-446655440000
    println(uuid1 == uuid2)
    // true
    println(uuid2 == uuid3)
    // true

    // Accesses UUID bits
    val version = uuid1.toLongs { mostSignificantBits, _ ->
        ((mostSignificantBits shr 12) and 0xF).toInt()
    }
    println(version)
    // 4

    // Generates a random UUID
    val randomUuid = Uuid.random()

    println(uuid1 == randomUuid)
    // false
}