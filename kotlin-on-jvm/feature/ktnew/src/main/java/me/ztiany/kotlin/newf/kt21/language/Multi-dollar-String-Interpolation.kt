package me.ztiany.kotlin.newf.kt21.language

import kotlin.reflect.KClass

private class JsonDemo {

    val KClass<*>.jsonSchema: String
        get() = $$"""
    {
      "$schema": "https://json-schema.org/draft/2020-12/schema",
      "$id": "https://example.com/product.schema.json",
      "$dynamicAnchor": "meta"
      "title": "$${simpleName ?: qualifiedName ?: "unknown"}",
      "type": "object"
    }
    """

    fun output(clazz: KClass<*>): String {
        return clazz.jsonSchema
    }
}

/**
 * Kotlin 2.1.0 introduces support for multi-dollar string interpolation, improving how the dollar
 * sign (`$`) is handled within string literals. This feature is helpful in contexts that require
 * multiple dollar signs, such as templating engines, JSON schemas, or other data formats.
 *
 * String interpolation in Kotlin uses a single dollar sign. However, using a literal dollar sign in
 * a string, which is common in financial data and templating systems, required workarounds such as
 * `${'$'}`. With the multi-dollar interpolation feature enabled, you can configure how many dollar
 * signs trigger interpolation, with fewer dollar signs being treated as string literals.
 *
 * Here's an example of how to generate an JSON schema multi-line string with placeholders using `$`:
 */
fun main() {
    println(JsonDemo().output(String::class))
    println(JsonDemo().output(Number::class))
}