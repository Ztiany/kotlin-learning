package explore.practice

import kotlinx.coroutines.flow.*

/**
 * refers to [transform](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/transform.html).
 */
suspend fun main() {
    intArrayOf(1, 2, 3, 4, 5).asFlow()
        .skipOddAndDuplicateEven()
        .onEach {
            println(it)
        }
        .collect()
}

private fun Flow<Int>.skipOddAndDuplicateEven(): Flow<Int> = transform { value ->
    // Emit only even values, but twice
    if (value % 2 == 0) {
        emit(value)
        emit(value)
    } // Do nothing if odd
}