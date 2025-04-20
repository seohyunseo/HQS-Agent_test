/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.hqs_test.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.hqs_test.R

class MainActivity: ComponentActivity(){

    private lateinit var imuManager: IMUManager
    private lateinit var hrManager: HRManager
    private lateinit var socketManager: SocketManager
    private lateinit var instructionText: TextView
    private lateinit var imuText: TextView
    private lateinit var hrText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        Log.d("Debug", "OnCreated")

        setTheme(android.R.style.Theme_DeviceDefault)

        setContentView(R.layout.activity_main)

        instructionText = findViewById<TextView>(R.id.Instruction_Text)
        imuText = findViewById<TextView>(R.id.IMU_Value)
        hrText = findViewById<TextView>(R.id.HR_Value)
        instructionText.text = "Start collecting the data!"

        socketManager = SocketManager("192.168.0.151")
        socketManager.connect()


        imuManager = IMUManager(this){ x, y, z ->
            runOnUiThread{
                val text = "%.2f, %.2f, %.2f".format(x,y,z)
                imuText.text = text

            }
        }

        hrManager = HRManager(this){ bpm ->
            runOnUiThread{
                val text = "%.2f (BPM)".format(bpm)
                hrText.text = text
                socketManager.sendData(text)
            }
        }

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



}

