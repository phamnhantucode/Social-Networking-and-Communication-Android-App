import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
object TestClientSocket1 {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val client = HttpClient(CIO).config { install(WebSockets) }
            client.ws(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/chat-socket") {
                launch(Dispatchers.Default) {
                    delay(10000L)
                    close()
                }
                incoming.consumeEach { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            println(frame.readText())
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

}