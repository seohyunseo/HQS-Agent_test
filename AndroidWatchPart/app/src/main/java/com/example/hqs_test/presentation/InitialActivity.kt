package com.example.hqs_test.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.hqs_test.R


class InitialActivity: ComponentActivity() {

    private lateinit var ipInstructionText: TextView
    private lateinit var ipInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        ipInstructionText = findViewById(R.id.IpInstruction_Text)
        ipInput = findViewById(R.id.Ip_Input)
    }

    fun onConnectButtonClick(view:View){
        val targetIp = ipInput.text.toString().trim()

        if(targetIp.isNotBlank()){

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("TARGET_IP", targetIp)
            startActivity(intent)

        }else{
            ipInstructionText.text = "${targetIp}-invalid"
        }
    }

}

