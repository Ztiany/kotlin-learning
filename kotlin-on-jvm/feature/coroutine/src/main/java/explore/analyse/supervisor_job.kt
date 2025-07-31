package explore.analyse

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import java.io.IOException

suspend fun main() {
    supervisorScope {
        try {
            val list1 = async<List<Int>> {
                delay(2000)
                throw IOException("Failed to load the list.")
            }

            val list2 = async {
                delay(4000)
                val result = listOf(1, 2, 3)
                println("list2: $result")
                return@async result
            }

            println("loaded: list2 = ${list2.await()}, list1 = ${list1.await()}")
            println("load end")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        println("final end")
    }
}