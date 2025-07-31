package me.ztiany.kotlin.newf.kt22.language

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

interface Logger {
    fun info(msg: String)
}

interface LoggingContext {
    val log: Logger // This context provides a reference to a logger
}

context(lg: LoggingContext)
fun startBusinessOperation() {
    lg.log.info("Operation has started")
}

interface NotificationSender {
    fun send(data: String)
}

context(logger: Logger, sender: NotificationSender)
fun store(data: String) {
    logger.info("store $data")
    sender.send(data)
    // do store
}

fun json(build: JsonObject.() -> Unit) = run {
    JsonObject().apply {
        build()
    }
}

context(json: JsonObject) infix fun String.by(build: JsonObject.() -> Unit) {
    json.add(this, JsonObject().apply { build() })
}

context(json: JsonObject) infix fun String.byArray(build: JsonArray.() -> Unit) {
    json.add(this, JsonArray().apply { build() })
}

fun JsonArray.addArray(build: JsonArray.() -> Unit) {
    add(JsonArray().apply { build() })
}

fun JsonArray.add(build: JsonObject.() -> Unit) {
    add(JsonObject().apply { build() })
}

context(json: JsonObject) infix fun String.by(value: Any) {
    when (value) {
        is Number -> {
            json.addProperty(this, value)
        }

        is String -> {
            json.addProperty(this, value)
        }

        is Boolean -> {
            json.addProperty(this, value)
        }

        is Char -> {
            json.addProperty(this, value)
        }

        is JsonElement -> {
            json.add(this, value)
        }

        else -> {
            throw UnsupportedOperationException()
        }
    }
}

/**
 * Context parameters allow functions and properties to declare dependencies that are implicitly
 * available in the surrounding context.
 *
 * With context parameters, you don't need to manually pass around values, such as services or
 * dependencies, that are shared and rarely change across sets of function calls.
 *
 * Context parameters replace an older experimental feature called context receivers. To migrate
 * from context receivers to context parameters, you can use assisted support in IntelliJ IDEA, as
 * described in the blog post.
 *
 * The main difference is that context parameters are not introduced as receivers in the body of a
 * function. As a result, you need to use the name of the context parameters to access their members,
 * unlike with context receivers, where the context is implicitly available.
 *
 * Context parameters in Kotlin represent a significant improvement in managing dependencies through
 * simplified dependency injection, improved DSL design, and scoped operations. For more information,
 * see the feature's
 * [KEEP](https://github.com/Kotlin/KEEP/blob/context-parameters/proposals/context-parameters.md).
 */
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
