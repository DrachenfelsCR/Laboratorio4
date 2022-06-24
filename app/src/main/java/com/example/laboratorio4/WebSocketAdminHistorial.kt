package okhttp3.recipes

import android.R
import okhttp3.*
import okio.ByteString
import org.json.JSONObject


class WebSocketAdminHistorial(): WebSocketListener() {

    lateinit var valorBusqueda : String

    /* fun run() {
        this.valorBusqueda = valorBusqueda
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()
        val request = Request.Builder()
            .url("ws://localhost:8080/Willyrex/nota")
            .build()
        client.newWebSocket(request, this)

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown()
    }*/

    override fun onOpen(webSocket: WebSocket, response: Response) {
        val apiCall = JSONObject()
        apiCall.put("action","GETHISTORIALPORID")
        apiCall.put("idAlumno",valorBusqueda)
        val apiCallString = apiCall.toString()
        webSocket.send(apiCallString)
        webSocket.close(1000, "Goodbye, World!")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("MESSAGE: $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        println("MESSAGE: " + bytes.hex())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        println("CLOSE: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        t.printStackTrace()
    }

    /*companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            WebSocketAdminHistorial().run()
        }
    }*/
}

