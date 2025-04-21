package com.example.hqs_test.presentation

import android.content.Intent
import io.socket.client.IO
import io.socket.client.Socket
import android.util.Log
import android.widget.TextView
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class SocketManager(private val serverIp: String, private val insText: TextView) {

    private lateinit var socket: Socket
    private val TAG = "Socket"

    fun connect() {
        try {
            val options = IO.Options()
            options.reconnection = true
            options.forceNew = true

            socket = IO.socket("http://$serverIp/android", options)

            socket.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "Connected to server")
                sendData("message","Initial Message from Android")
            }

            socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
                insText.text = "Connection Failed. Retry with valid IP"
            }

            setupSocketEvents()

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

    fun sendData(event: String, message: String){
        if (::socket.isInitialized && socket.connected()) {
            socket.emit(event, message)
            Log.d(TAG, "Sent message to Server: $message")
        } else {
            Log.w(TAG, "Cannot send, not connected")
        }
    }

    fun setupSocketEvents(){
        socket.on("message", onMessage)
        socket.on("duration_message", onDurationMessage)
    }

    private val onMessage = Emitter.Listener { args ->
        var msg = args[1] as? String ?: return@Listener
        msg = msg.trim()
        Log.d(TAG, "Received from server: ${msg}")
    }

    private val onDurationMessage = Emitter.Listener {args ->
        val msg = args[0] as? String ?: return@Listener
        Log.d(TAG, "Received from server: ${msg}")
        try{
            MainActivity.SendInterval.sendInterval = msg.toLong() * 1000
            sendData("message","Duration changed to ${MainActivity.SendInterval.sendInterval}")
        }catch(e: URISyntaxException){
            sendData("message","Set valid duration")
        }

    }

}