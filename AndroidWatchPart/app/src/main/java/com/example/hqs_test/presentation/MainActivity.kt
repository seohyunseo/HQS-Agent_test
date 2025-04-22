/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.hqs_test.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.hqs_test.R

class MainActivity: ComponentActivity(){

    private lateinit var imuManager: IMUManager
    private val imuDataBuffer = StringBuilder()
    private lateinit var hrManager: HRManager
    private val hrDataBuffer = StringBuilder()
    private lateinit var instructionText: TextView
    private lateinit var imuText: TextView
    private lateinit var hrText: TextView
    private lateinit var socketManager: SocketManager
    private lateinit var targetIp: String
    private val handler = Handler(Looper.getMainLooper())
    object SendInterval {
        var sendInterval: Long = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        Log.d("Debug", "OnCreated")

        setTheme(android.R.style.Theme_DeviceDefault)
        setContentView(R.layout.activity_main)

        targetIp = intent.getStringExtra("TARGET_IP") ?: ""
        instructionText = findViewById<TextView>(R.id.Instruction_Text)
        imuText = findViewById<TextView>(R.id.IMU_Value)
        hrText = findViewById<TextView>(R.id.HR_Value)
        instructionText.text = "Start collecting the data!"

        runSensorManager()

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }


    fun OnStartButtonClick(view: View){
        imuManager.start()
        hrManager.start(this, this)
        instructionText.text = "Data Collecting..."
    }

    fun OnPauseButtonClick(view: View){
        imuManager.stop()
        hrManager.stop()
        instructionText.text = "Resume collecting the data!"
    }

    fun OnQuitButtonClick(view: View){
        imuManager.stop()
        hrManager.stop()
        socketManager.disconnect()
        finishAffinity()
    }

    private val sendRunnable = object : Runnable {
        override fun run() {
            if (imuDataBuffer.isNotEmpty() && hrDataBuffer.isNotEmpty()) {
                val imuData = imuDataBuffer.toString()
                val hrData = hrDataBuffer.toString()
                socketManager.sendData("IMU_message", imuData)
                socketManager.sendData("HR_message", hrData)
                imuDataBuffer.clear()
                hrDataBuffer.clear()
            }
            handler.postDelayed(this, SendInterval.sendInterval)
        }
    }

    private fun runSensorManager(){

        imuManager = IMUManager(this){ mag ->
            runOnUiThread{
                val text = "%.2f,".format(mag)
                imuDataBuffer.append(text)
                imuText.text = text

            }
        }
        handler.postDelayed(sendRunnable, SendInterval.sendInterval)

        socketManager = SocketManager(targetIp, instructionText)
        socketManager.connect()

        hrManager = HRManager(this){ bpm ->
            runOnUiThread{
                val text = "%.2f,".format(bpm)
                hrDataBuffer.append(text)
                hrText.text = text

            }
        }
        handler.postDelayed(sendRunnable, SendInterval.sendInterval)
    }

}

