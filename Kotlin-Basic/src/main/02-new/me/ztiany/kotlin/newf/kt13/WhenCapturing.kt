package me.ztiany.kotlin.newf.kt13


fun main() {

}

private sealed class Request {

    abstract fun executeRequest(): Request

    class Success : Request() {
        override fun executeRequest(): Request {
            return this
        }

        val body: String = ""
    }

    object HttpError : Request() {
        override fun executeRequest(): Request {
            return this
        }
    }

}

private class HttpException : Exception()

//In Kotlin 1.3, it is now possible to capture "the when subject" into a variable:
private fun Request.getBody() = when (val response = executeRequest()) {
    is Request.Success -> response.body
    is Request.HttpError -> throw HttpException()
}