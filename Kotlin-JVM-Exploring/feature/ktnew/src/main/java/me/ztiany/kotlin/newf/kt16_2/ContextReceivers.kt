package me.ztiany.kotlin.newf.kt16_2

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

/*
ContextReceivers Feature.

Official Docs:

- https://kotlinlang.org/docs/whatsnew1620.html#prototype-of-context-receivers-for-kotlin-jvm
- https://www.youtube.com/watch?v=GISPalIVdQY

Community Blogs:

- https://kt.academy/article/fk-context-receivers
- https://nomisrev.github.io/context-receivers/
- https://blog.rockthejvm.com/kotlin-context-receivers/
 */

interface Logger {
    fun info(msg: String)
}

interface LoggingContext {
    val log: Logger // This context provides a reference to a logger
}

context(LoggingContext)
fun startBusinessOperation() {
    // You can access the log property since LoggingContext is an implicit receiver
    log.info("Operation has started")
}

interface NotificationSender {
    fun send(data: String)
}

context(Logger, NotificationSender)
fun store(data: String) {
    info("store $data")
    send(data)
    // do store
}

fun json(build: JsonObject.() -> Unit) = run {
    JsonObject().apply {
        build()
    }
}

context(JsonObject) infix fun String.by(build: JsonObject.() -> Unit) {
    add(this, JsonObject().apply { build() })
}

context(JsonObject) infix fun String.byArray(build: JsonArray.() -> Unit) {
    add(this, JsonArray().apply { build() })
}

fun JsonArray.addArray(build: JsonArray.() -> Unit) {
    add(JsonArray().apply { build() })
}

fun JsonArray.add(build: JsonObject.() -> Unit) {
    add(JsonObject().apply { build() })
}

context(JsonObject) infix fun String.by(value: Any) {
    when (value) {
        is Number -> {
            addProperty(this, value)
        }

        is String -> {
            addProperty(this, value)
        }

        is Boolean -> {
            addProperty(this, value)
        }

        is Char -> {
            addProperty(this, value)
        }

        is JsonElement -> {
            add(this, value)
        }

        else -> {
            throw UnsupportedOperationException()
        }
    }
}

fun main() {
    val testLoggingContext = object : LoggingContext {
        override val log: Logger = object : Logger {
            override fun info(msg: String) {
                println("info: $msg")
            }
        }
    }

    val testLogger = object : Logger {
        override fun info(msg: String) {
            println(msg)
        }
    }

    val testNotificationSender = object : NotificationSender {
        override fun send(data: String) {
            println("send: $data")
        }

    }

    with(testLoggingContext) {
        startBusinessOperation()
    }

    with(testLogger) {
        with(testNotificationSender) {
            store("A")
            store("B")
            store("C")
            store("D")
        }
    }

    val json = json {
        "name" by "Kotlin"
        "age" by 10
        "creator" by {
            "name" by "JetBrains"
            "age" by "21"
        }
        "attribute" byArray {
            add("1")
            add("2")
            add("3")
            add {
                "a" by "A"
                "b" by 10
            }
            addArray {
                add("4")
                add("5")
                add("6")
            }
        }
    }

    println(json.toString())
}