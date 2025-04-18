package com.example.hqs_test.presentation

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import android.util.Log

class SocketManager(private val serverIp: String) {

    private lateinit var webSocket: WebSocket
    private var TAG = "Socket"

    fun connect(){
        val request = Request.Builder()
            .url(serverIp)
            .build()

        val client = OkHttpClient()
        webSocket = client.newWebSocket(request, object: WebSocketListener(){

            override fun onOpen(webSocket: WebSocket, response: Response){
                Log.d(TAG, "Server connected")
            }

            override fun onMessage(webSocket: WebSocket, text:String){
                Log.d(TAG, "Server: $text")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String){
                println("WebsSocket Closing")
                webSocket.close(1000,null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?){
                Log.d(TAG, "WebSocket Error: ${t.message}")
            }

        })
    }

    fun sendData(data: String){
        webSocket.send(data)
    }

    fun close(){
        webSocket.close(1000,null)
    }
}