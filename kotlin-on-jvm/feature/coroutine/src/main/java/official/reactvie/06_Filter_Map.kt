package official.reactvie

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactive.collect
import kotlinx.coroutines.reactive.publish
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Publisher
import kotlin.coroutines.CoroutineContext

//filter 与 map 之类的 reactive 操作符要用协程实现实在很无聊，为了有点挑战性，就来写一个 fusedFilterMap 吧

@ExperimentalCoroutinesApi
private fun <T, R> Publisher<T>.fusedFilterMap(
        context: CoroutineContext,   // the context to execute this coroutine in
        predicate: (T) -> Boolean,   // the filter predicate
        mapper: (T) -> R             // the mapper function
) = publish(context) {
    collect {
        // consume the source stream
        if (predicate(it))       // filter part
            send(mapper(it))     // map part
    }
}

@ExperimentalCoroutinesApi
fun main() = runBlocking<Unit> {
    range(this.coroutineContext, 1, 5)
            .fusedFilterMap(coroutineContext, { it % 2 == 0 }, { "$it is even" })
            .collect { println(it) } // print all the resulting strings
}