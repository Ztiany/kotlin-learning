package me.ztiany.kotlin.noarg

import java.lang.reflect.Type

inline fun <reified T> Storage.getEntity(key: String): T? {
    println(object : TypeFlag<T>() {}.type)
    return this.getEntity(key, T::class.java.componentType)
}

class AStorage : Storage {

    override fun <T : Any?> getEntity(key: String?, type: Type?): T? {
        return null
    }

}

fun main() {
    AStorage().getEntity<List<String>>("A")
    AStorage().getEntity<String>("A")
}