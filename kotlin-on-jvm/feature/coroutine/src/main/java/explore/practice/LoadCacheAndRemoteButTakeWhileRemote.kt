package explore.practice

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

private data class LoadedData<T>(
    val terminate: Boolean = false,
    val data: T
)

/**
 * refers to [Flow.transformWhile operator](https://github.com/Kotlin/kotlinx.coroutines/issues/2065).
 */
suspend fun main() {
    merge(cache, remote)
        .onEach {
            println("loaded: $it")
        }
        .transformWhile {
            emit(it.data)
            it.terminate// returning true  means to continue collect.
        }
        .onEach {
            println("received: $it")
        }
        .collect()
}

private val cache = flow {
    delay(1000)// simulating that cache loading is slower than remote loading.
    emit(LoadedData(true, "I am Cache"))
}

private val remote = flow {
    delay(100)
    emit(LoadedData(false, "I am Remote"))
}