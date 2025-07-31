package common

data class State<out T>(
    val value: T? =null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)