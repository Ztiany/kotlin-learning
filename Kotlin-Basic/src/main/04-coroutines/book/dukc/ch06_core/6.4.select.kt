package book.dukc.ch06_core

import book.dukc.common.api.User
import book.dukc.common.api.githubApi
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select
import java.io.File
import kotlin.random.Random

/**
 * 《深入理解Kotlin协程》chapter 6.4：select
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 2020/9/25 10:08
 */
suspend fun main() {
    //在 UNIX 的 IO 多路复用中，我们应该都接触过 select，其实在协程中，select 的作用也与在 UNIX 中类似。

    //复用多个 await【6.4.1】
    //reuseAwait()

    //复用多个 channel【6.4.2】
    //reuseChannel()

    //SelectClause【6.4.3】
    //whatIsSelectClause()

    //使用 Flow 实现多路复用【6.4.4】
    flowWithReuse()
}

///////////////////////////////////////////////////////////////////////////
// 公用
///////////////////////////////////////////////////////////////////////////
private val localDir = File("localCache").also { it.mkdirs() }

private val gson = Gson()

fun CoroutineScope.getUserFromApi(login: String) = async(Dispatchers.IO) {
    githubApi.getUserSuspend(login)
}

fun CoroutineScope.getUserFromLocal(login: String) = async(Dispatchers.IO) {
    File(localDir, login).takeIf {
        it.exists()
    }?.readText()?.let {
        gson.fromJson(it, User::class.java)
    }
}

private fun cacheUser(login: String, user: User) {
    File(localDir, login).writeText(gson.toJson(user))
}

private data class Response<T>(val value: T, val isLocal: Boolean)

///////////////////////////////////////////////////////////////////////////
// 复用多个 await
///////////////////////////////////////////////////////////////////////////
private suspend fun reuseAwait() {
    //需求：如果有这样一个场景，两个 API 分别从网络和本地缓存获取数据，期望哪个先返回就先用哪个做展示

    //复杂的使用
    //notSatisfy()

    //select 的实现【其实这个例子有 bug，因为每次都会是 local 先返回。】
    useSelectToAwait()
}

private suspend fun useSelectToAwait() = coroutineScope {
    val login = "bennyhuo"

    val localDeferred = getUserFromLocal(login)
    val remoteDeferred = getUserFromApi(login)

    //我们没有直接调用 await，而是调用了 onAwait 在 select 中注册了回调，select 总是会立即调用最先返回的事件的回调。
    val userResponse = select<Response<User?>> {
        localDeferred.onAwait {
            Response(it, true)
        }
        remoteDeferred.onAwait {
            Response(it, false)
        }
    }

    //假设 localDeferred.onAwait 先返回，那么 userResponse 的值就是 Response(it,true)，由于我们的本地缓存可能不存在，因此 select 的结果类型是 Response<User?>。
    println("user = ${userResponse.value}, from local = ${userResponse.isLocal}")

    //如果先返回的是本地缓存，那么我们还需要获取网络结果来展示最终结果。
    userResponse.isLocal.takeIf {
        it
    }?.let {
        println("save to local")
        val userFromApi = remoteDeferred.await()
        cacheUser(login, userFromApi)
        println("userFromApi = $userFromApi")
    }
}

private suspend fun notSatisfy() {
    val local = supervisorScope {
        getUserFromApi("Ztiany")
    }
    val remote = supervisorScope {
        getUserFromLocal("Ztiany")
    }
    //如何实现哪个先返回就先用哪个做展示呢?当然，我们也可以启动两个协程来分别调用await，不过这样会将问题复杂化。
}

///////////////////////////////////////////////////////////////////////////
// 复用多个 channel
///////////////////////////////////////////////////////////////////////////
private suspend fun reuseChannel() {
    val channels = List(10) {
        Channel<Int>()
    }

    GlobalScope.launch {
        delay(100)
        channels[Random.nextInt(10)].send(200)
    }

    val result = select<Int?> {
        channels.forEach { channel ->
            //对于 onReceive，如果 Channel 被关闭，select 会直接抛出异常
            //channel.onReceive { it }

            //对于 onReceiveOrNull，如果遇到 Channel 被关闭的情况，i t的值就是 null。【onReceiveOrNull 已经被废弃】
            //使用 onReceiveCatching 可以更好地处理各种情况。
            channel.onReceiveCatching {
                it.getOrNull()
            }
        }
    }

    println(result)
}

///////////////////////////////////////////////////////////////////////////
//SelectClause
///////////////////////////////////////////////////////////////////////////
private suspend fun whatIsSelectClause() {
/*
    怎么知道哪些事件可以被select呢？其实所有能够被select的事件都是SelectClauseN类型，包括：
        1. SelectClause0：对应事件没有返回值，例如 join 没有返回值，那么 onJoin 就是 SelectClauseN 类型。使用时，onJoin 的参数是一个无参函数。
        2. SelectClause1：对应事件有返回值，前面的 onAwait 和 onReceive 都是此类情况。
        3. SelectClause2：对应事件有返回值，此外还需要一个额外的参数，例如 Channel.onSend 有两个参数，第一个是 Channel 数据类型的值，表示即将发送的值；第二个是发送成功时的回调参数。
 */

    //SelectClause0
    val job = GlobalScope.launch { delay(1000) }
    select<Unit> { job.onJoin { println("Join resumed!") } }

    //SelectClause2
    val channels = List(10) {
        GlobalScope.actor<Int> {
            consumeEach {
                println("receive $it")
            }
        }
    }
    List(100) { element ->
        select<Unit> {
            channels.forEach { channel ->
                //当 element 被发送到 Channel，则后面的 lambda 回调被调用。
                channel.onSend(element) { sentChannel ->
                    println("sent on $sentChannel")
                }
            }
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// 使用 Flow 实现多路复用
///////////////////////////////////////////////////////////////////////////
private suspend fun flowWithReuse() = coroutineScope {
/*
    多数情况下，我们可以通过构造合适的 Flow 来实现多路复用的效果。
    【6.4.1】中对 await 的复用方法也可以用 Flow 实现
 */
    val login = "bennyhuo"
    listOf(::getUserFromApi, ::getUserFromLocal)
            .map { function ->
                function.call(login)
            }
            .map { deferred ->
                flow { emit(deferred.await()) }
            }
            //此时调用 merge 函数，将两个 Flow 整合成一个 Flow 进行处理
            .merge()
            .onEach { user ->
                println("Result: $user")
            }
            .launchIn(this)

    //Channel 的读取复用的场景也可以使用 Flow 来完成。
    val channels = List(10) { Channel<Int>() }
    GlobalScope.launch {
        delay(100)
        List(10) {
            channels[it].send(it)
        }
    }
    val result = channels.map {
        it.consumeAsFlow()
    }.merge().first()
    println("result = $result")
}
