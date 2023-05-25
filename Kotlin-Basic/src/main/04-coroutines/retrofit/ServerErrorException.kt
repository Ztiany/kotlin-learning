package retrofit

class ServerErrorException(private val serverDataError: Int) : RuntimeException("服务器错误") {

    companion object {
        const val SERVER_DATA_ERROR = 1
        const val NO_DATA_ERROR = 3
    }

    override fun toString(): String {
        return "ServerErrorException(code=$serverDataError, message=${message})"
    }

}