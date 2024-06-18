package practice

import common.State
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.util.Date
import kotlin.random.Random

/**
 * 通过状态驱动 UI，一次加载的行为为：刚开始的 loading，然后不是 failure 就是 success。
 *
 * 但是如果之前 load 成功过，再次刷新时失败了，希望还是展示旧的数据，然后通过 message 的方式提示用户加载失败，因此
 * 每一次通过 flow 分发 State，都想要看看之前是否有过成功加载的 User，如果有，则连同旧数据一起分发给 UI。
 *
 * 感谢：[Get current and previous value in flow's collect](https://stackoverflow.com/questions/72626286/get-current-and-previous-value-in-flows-collect)。
 */
suspend fun main() {
    coroutineScope {
        launch {
            subscribeUser()
                .onEach {
                    // 打印加载到的数据
                    println("            received $it")
                }.collect()
        }

        launch {
            while (isActive) {
                // 触发刷新
                delay(5000)
                searchCondition.emit(Random.nextInt())
            }
        }
    }
}

private data class User(val id: Int, val name: String)

private val searchCondition = MutableSharedFlow<Int>()

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
private fun subscribeUser() = searchCondition
    .debounce(300)
    .flatMapMerge(1) { id ->
        loadUser(id)
    }.runningFold(State<User>(isLoading = true)) { accumulator, value ->
        println("do accumulation: $accumulator, $value")
        if (value.value == null) {
            value.copy(accumulator.value)
        } else value
    }

private fun loadUser(userId: Int): Flow<State<User>> {
    println("loading user $userId")
    return flow {
        emit(State(isLoading = true))
        val user = withContext(Dispatchers.IO) {
            delay(1000)
            User(userId, "Ztiany")
        }
        if (Random.nextBoolean()) {
            println("successfully loaded user $userId")
            emit(State(isLoading = false, value = user))
        } else {
            println("failed to load user $userId")
            emit(State(isLoading = false, error = IOException("Can't load user: ${Date().toLocaleString()}")))
        }
    }
}