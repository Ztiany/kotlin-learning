package official.core.flow

import common.printAligned
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
 * Intermediate flow operators: Flows can be transformed using operators, in the same way as you would transform collections
 * and sequences. Intermediate operators are applied to an upstream flow and return a downstream flow. These operators are
 * cold, just like flows are. A call to such an operator is not a suspending function itself. It works quickly, returning
 * the definition of a new transformed flow.
 */
suspend fun main() {
    // transform
    mapOperator()
    transformOperator()

    // size limiting
    takeOperator()
}

///////////////////////////////////////////////////////////////////////////
// Transform
///////////////////////////////////////////////////////////////////////////


/**
 * The basic operators have familiar names like map and filter. An important difference of these operators from sequences
 * is that blocks of code inside these operators can call suspending functions.
 *
 * For example, a flow of incoming requests can be mapped to its results with a map operator, even when performing a request
 * is a long-running operation that is implemented by a suspending function:
 */
private suspend fun mapOperator() {
    "map".printAligned()
    (1..3).asFlow()
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


/**
 * Among the flow transformation operators, the most general one is called transform. It can be used to imitate simple
 * transformations like map and filter, as well as implement more complex transformations. Using the transform operator,
 * we can emit arbitrary values an arbitrary number of times.
 */
private suspend fun transformOperator() {
    "transform".printAligned()
    (1..3).asFlow() // a flow of requests
        .transform { request ->
            emit("Making request $request")
            emit(performRequest(request))
        }
        .collect { response -> println("transform: $response") }
}

///////////////////////////////////////////////////////////////////////////
// Size Limiting
///////////////////////////////////////////////////////////////////////////

/**
 * Size-limiting intermediate operators like take cancel the execution of the flow when the corresponding limit is reached.
 */
private suspend fun takeOperator() {
    "take".printAligned()
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
