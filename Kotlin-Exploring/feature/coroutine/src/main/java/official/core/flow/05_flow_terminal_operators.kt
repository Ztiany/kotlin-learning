package official.core.flow

import common.printAligned
import kotlinx.coroutines.flow.*

/**
 * Intermediate flow operators: Terminal operators on flows are suspending functions that start a collection of the flow.
 * The collect operator is the most basic one, but there are other terminal operators, which can make it easier.
 */
suspend fun main() {
    toListOperator()
    reduceOperator()
    foldOperator()
}

/**
 * Conversion to various collections like toList and toSet.
 */
private suspend fun toListOperator() {
    "toList".printAligned()
    (1..10).asFlow()
        .map {
            it * it
        }
        .toList()
        .let {
            println("toList: $it")
        }
}

/**
 * Reducing a flow to a value with reduce and fold.
 */
private suspend fun reduceOperator() {
    "reduce".printAligned()
    (1..5).asFlow()
        .map { it * it }
        .reduce { a, b -> a + b }
        .let {
            println("reduce: $it")
        }
}

/**
 * Reducing a flow to a value with reduce and fold.
 */
private suspend fun foldOperator() {
    "fold".printAligned()
    (1..5).asFlow()
        .map { it * it }
        .fold(1) { a, b -> a + b }
        .let {
            println("fold: $it")
        }
}
