package core.flow

import common.logCoroutine
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

/**
 * operators
 */
suspend fun main() {
    //transform
    //transformOperator()
    //mapOperator()

    //size limiting
    //takeOperator()

    //terminal operators
    //toListOperator()
    //reduceOperator()
    //foldOperator()

    //flowOn
    //flowOnOperator()

    //buffer
    //bufferOperator()
    //conflationOperator()
    //processLatestOperator()

    //compose
    //zipOperator()
    //combineOperator()

    //flatten
    flatMapConcatOperator()
    flatMapMergeOperator()
    flatMapLatestOperator()
}

///////////////////////////////////////////////////////////////////////////
// Transform
///////////////////////////////////////////////////////////////////////////

/**
 * The most general one is called transform. It can be used to imitate simple transformations like map and filter,
 * as well as implement more complex transformations. Using the transform operator, we can emit arbitrary values
 * an arbitrary number of times.
 */
private suspend fun transformOperator() {
    (1..10).asFlow()
            .transform {
                emit(it)
            }
            .collect {
                println("transform: $it")
            }
}

private suspend fun mapOperator() {
    (1..10).asFlow()
            .map {
                //blocks of code inside these operators can call suspending functions.
                performRequest(it)
            }
            .collect { println("map: $it") }
}

private suspend fun performRequest(request: Int): String {
    delay(1000) // imitate long-running asynchronous work
    return "response $request"
}

///////////////////////////////////////////////////////////////////////////
// Size Limiting.
///////////////////////////////////////////////////////////////////////////

private suspend fun takeOperator() {
    flow {
        var a = 1
        // Cancellation in coroutines is always performed by throwing an exception,
        // so that all the resource-management functions (like try { ... } finally { ... } blocks)
        // operate normally in case of cancellation.
        try {
            while (true) {
                emit(a++)
                delay(100)
            }
        } catch (e: CancellationException) {
            e.printStackTrace()
        }
    }
            // Size-limiting intermediate operators like take cancel the execution of the flow when
            // the corresponding limit is reached.
            .take(5)
            .collect {
                println("take: $it")
            }
}

///////////////////////////////////////////////////////////////////////////
// Terminal Operators
///////////////////////////////////////////////////////////////////////////

private suspend fun toListOperator() {
    (1..10).asFlow()
            .map {
                it * it
            }
            .toList()
            .let {
                print("result: $it")
            }
}

private suspend fun reduceOperator() {
    (1..5).asFlow()
            .map { it * it }
            .reduce { a, b -> a + b }
            .let {
                println("reduce: $it")
            }
}

private suspend fun foldOperator() {
    (1..5).asFlow()
            .map { it * it }
            .fold(1) { a, b -> a + b }
            .let {
                println("fold: $it")
            }
}

///////////////////////////////////////////////////////////////////////////
//流的每次单独收集都是按顺序执行的。
///////////////////////////////////////////////////////////////////////////
private suspend fun flowAreSequential() {
    (1..5).asFlow()
            .filter {
                println("Filter $it")
                it % 2 == 0
            }
            .map {
                println("Map $it")
                "string $it"
            }.collect {
                println("Collect $it")
            }
}

///////////////////////////////////////////////////////////////////////////
// context preservation
///////////////////////////////////////////////////////////////////////////
private suspend fun flowOnOperator() {
    //collect：Collection of a flow always happens in the context of the calling coroutine.【main】
    flow {
        logCoroutine("Started simple flow")
        // Since simple().collect is called from the main thread, the body of simple 's flow is also called in the main thread.
        // This is the perfect default for fast-running or asynchronous code that does not care about the execution context and does not block the caller.
        for (i in 1..3) {
            emit(i)
        }
    }.collect { value ->
        logCoroutine("Collected $value")
    }

    // collect with withContext：Collection of a flow always happens in the context
    // of the calling coroutine.【DefaultDispatcher-worker-1】
    withContext(Dispatchers.Default) {
        flow {
            logCoroutine("Started simple flow")
            for (i in 1..3) {
                emit(i)
            }
        }.collect { value ->
            logCoroutine("WithContext Collected $value")
        }
    }

    /* Usually, withContext is used to change the context in the code using Kotlin coroutines,
     * but code in the flow { ... } builder has to honor the context preservation property and
     * is not allowed to emit from a different context.
     */
    flow {
        // The WRONG way to change context for CPU-consuming code in flow builder
        withContext(Dispatchers.Default) {
            for (i in 1..3) {
                Thread.sleep(100) // pretend we are computing it in CPU-consuming way
                emit(i) // emit next value
            }
        }
    }/*.collect {}*/

    //The correct way to change the context of a flow is using flowOn.
    flow {
        for (i in 1..3) {
            Thread.sleep(100) // pretend we are computing it in CPU-consuming way
            logCoroutine("Emitting $i")
            emit(i) // emit next value
        }
    }
            //notice: The flowOn operator will change the default sequential nature of the flow.
            .flowOn(Dispatchers.Default)
            .collect { value ->
                logCoroutine("Collected $value")
            }

}

///////////////////////////////////////////////////////////////////////////
// Buffer
///////////////////////////////////////////////////////////////////////////
private suspend fun bufferOperator() {
    //We can use a buffer operator on a flow to run emitting code of the simple flow concurrently with collecting code
    measureTimeMillis {
        flow {
            for (i in 1..3) {
                //not waiting the downstream to finish processing.
                delay(100) // pretend we are asynchronously waiting 100 ms
                emit(i) // emit next value
            }
        }
                .buffer()
                .collect { value ->
                    //time = 100 + (300 * 3) ≈ 1000
                    delay(300) // pretend we are processing it for 300 ms
                    println(value)
                }
    }.let {
        println("bufferOperator: time = $it")
    }
}

private suspend fun conflationOperator() {
    // the conflate operator can be used to skip intermediate values when a collector is too slow to process them.
    measureTimeMillis {
        flow {
            for (i in 1..3) {
                delay(100) // pretend we are asynchronously waiting 100 ms
                emit(i) // emit next value
            }
        }
                .conflate() // conflate emissions, don't process each one
                .collect { value ->
                    delay(300) // pretend we are processing it for 300 ms
                    println(value)
                }
    }.let {
        println("conflationOperator: time = $it")
    }
}

private suspend fun processLatestOperator() {
    //cancel a slow collector and restart it every time a new value is emitted.
    measureTimeMillis {
        flow {
            for (i in 1..3) {
                delay(100) // pretend we are asynchronously waiting 100 ms
                emit(i) // emit next value
            }
        }
                .collectLatest { value ->
                    delay(300) // pretend we are processing it for 300 ms
                    println(value)
                }
    }.let {
        println("processLatestOperator: time = $it")
    }
}

///////////////////////////////////////////////////////////////////////////
// compose
///////////////////////////////////////////////////////////////////////////
private suspend fun zipOperator() {
    //flows have a zip operator that combines the corresponding values of two flows one by one.
    val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..3 every 300 ms
    val strs = flowOf("one", "two", "three").onEach { delay(400) } // strings every 400 ms
    val startTime = System.currentTimeMillis() // remember the start time
    nums.zip(strs) { a, b -> "$a -> $b" } // compose a single string with "zip"
            .collect { value -> // collect and print
                println("zipOperator: $value at ${System.currentTimeMillis() - startTime} ms from start")
            }
}

private suspend fun combineOperator() {
    //Combine operator combines the latest values of two flows once emission happens.
    val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..3 every 300 ms
    val strs = flowOf("one", "two", "three").onEach { delay(400) } // strings every 400 ms
    val startTime = System.currentTimeMillis() // remember the start time
    nums.combine(strs) { a, b -> "$a -> $b" } // compose a single string with "combine"
            .collect { value -> // collect and print
                println("combineOperator: $value at ${System.currentTimeMillis() - startTime} ms from start")
            }
}

///////////////////////////////////////////////////////////////////////////
// Flatten
///////////////////////////////////////////////////////////////////////////
private suspend fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500) // wait 500 ms
    emit("$i: Second")
}

private suspend fun flatMapConcatOperator() {
    //Concatenating mode is implemented by flatMapConcat and flattenConcat operators.
    // They are the most direct analogues of the corresponding sequence operators. They wait for the inner flow to complete before starting to collect the next one
    val startTime = System.currentTimeMillis() // remember the start time
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
            .flatMapConcat { requestFlow(it) }
            .collect { value -> // collect and print
                println("flatMapConcatOperator $value at ${System.currentTimeMillis() - startTime} ms from start")
            }
}

private suspend fun flatMapMergeOperator() {
    //A way to concurrently collect all the incoming flows and merge their values into a single flow so that values are emitted as soon as possible.
    // It is implemented by flatMapMerge and flattenMerge operators.
    //They both accept an optional concurrency parameter that limits the number of concurrent flows that are collected at the same time (it is equal to DEFAULT_CONCURRENCY by default).
    val startTime = System.currentTimeMillis() // remember the start time
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
            .flatMapMerge { requestFlow(it) }
            .collect { value -> // collect and print
                println("flatMapMergeOperator $value at ${System.currentTimeMillis() - startTime} ms from start")
            }
}

private suspend fun flatMapLatestOperator() {
    //In a similar way to the collectLatest operator, there is the corresponding "Latest" flattening mode where a collection of the previous flow is cancelled
    // as soon as new flow is emitted. It is implemented by the flatMapLatest operator.
    val startTime = System.currentTimeMillis() // remember the start time
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
            .flatMapLatest { requestFlow(it) }
            .collect { value -> // collect and print
                println("flatMapLatestOperator $value at ${System.currentTimeMillis() - startTime} ms from start")
            }
}
