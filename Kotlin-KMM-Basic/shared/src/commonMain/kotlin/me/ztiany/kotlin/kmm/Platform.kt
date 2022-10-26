package me.ztiany.kotlin.kmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform