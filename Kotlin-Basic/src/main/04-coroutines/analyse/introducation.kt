package analyse

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


suspend fun main() {
    GlobalScope.launch {
        log("me.ztiany.tools.main")
    }.join()
}