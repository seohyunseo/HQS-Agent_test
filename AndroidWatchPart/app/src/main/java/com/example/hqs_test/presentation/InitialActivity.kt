package com.example.hqs_test.presentation

import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.example.hqs_test.R
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale


class InitialActivity: ComponentActivity() {

    private lateinit var ipText: TextView
    private lateinit var wifiText: TextView
    private lateinit var ipInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        wifiText = findViewById(R.id.Wifi_Text)
        ipText = findViewById(R.id.Ip_Text)
        ipInput = findViewById(R.id.Ip_Input)


    }

    fun onConnectButtonClick(view:View){
        wifiText.text = "test"
    }

}

