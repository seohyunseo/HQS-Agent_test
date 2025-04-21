package com.example.hqs_test.presentation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.TextView
import kotlin.math.*

class IMUManager(
    private val context: Context,
    private val onSensorData: (Float) -> Unit
): SensorEventListener {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var acc: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val (x, y, z) = it.values
            onSensorData(getMagnitude(x,y,z))  // callback to activity
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

    fun start(){
        acc?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stop(){
        sensorManager.unregisterListener(this)
    }

    private fun getMagnitude(x:Float, y:Float, z:Float):Float{
        val magnitude = sqrt(x*x + y*y + z*z)

        return magnitude
    }

}