package com.example.hqs_test.presentation

import io.socket.client.IO
import io.socket.client.Socket
import android.util.Log
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class SocketManager(private val serverIp: String) {

    private lateinit var socket: Socket
    private val TAG = "Socket"

    fun connect() {
        try {
            val options = IO.Options()
            options.reconnection = true
            options.forceNew = true

            socket = IO.socket("http://$serverIp:5000/android", options)

            socket.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "Connected to server")
                sendData("Initial Message from Android")
            }

            socket.on("message", onMessage)

            socket.connect()

        } catch (e: URISyntaxException) {
            Log.e(TAG, "Socket URI Error: ${e.message}")
        }
    }

    fun disconnect(){
        if (::socket.isInitialized) {
            socket.disconnect()
            socket.off("message", onMessage)
            Log.d(TAG, "Disconnected manually")
        }
    }

    fun sendData(message: String){
        if (::socket.isInitialized && socket.connected()) {
            socket.emit("message", message)
            Log.d(TAG, "Sent message to Server: $message")
        } else {
            Log.w(TAG, "Cannot send, not connected")
        }
    }

    private val onMessage = Emitter.Listener { args ->
        val msg = args[0] as? String ?: return@Listener
        Log.d(TAG, "Received from server: $msg")
    }
}