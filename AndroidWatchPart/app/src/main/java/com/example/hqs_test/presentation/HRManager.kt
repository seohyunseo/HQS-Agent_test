package com.example.hqs_test.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.health.services.client.HealthServices
import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DeltaDataType

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


//import androidx.health.services.client.data.MeasureRequest
//import androidx.health.services.client.data.DataTypeAccuracyMode


class HRManager(
    private val context: Context,
    private val onHeartRate: (Float) -> Unit
) {

    private val healthClient: HealthServicesClient = HealthServices.getClient(context)
    private val measureClient = healthClient.measureClient

    private val callback = object : MeasureCallback {
        override fun onDataReceived(data: DataPointContainer) {
            val heartRates = data.getData(DataType.HEART_RATE_BPM)
            heartRates.forEach {
                val bpm = it.value.toFloat()
                onHeartRate(bpm)  // callback to activity
            }
        }

        override fun onAvailabilityChanged(
            dataType: DeltaDataType<*, *>,
            availability: Availability
        ) {
            Log.d("HRManager", "Availability changed: $availability")
        }
    }

    fun start(context: Context, activity: Activity) {
        initialCheck()
        checkAndRequestSensorPermission(context, activity)
    }

    fun stop() {
        runBlocking {
            measureClient.unregisterMeasureCallbackAsync(DataType.Companion.HEART_RATE_BPM, callback)
        }
    }

    private fun initialCheck(){
        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.BODY_SENSORS)
        Log.d("Permissions", "BODY_SENSORS granted: ${granted == PackageManager.PERMISSION_GRANTED}")

        CoroutineScope(Dispatchers.Default).launch {
            val capabilities = measureClient.getCapabilitiesAsync().await()
            val supportsHeartRate = DataType.HEART_RATE_BPM in capabilities.supportedDataTypesMeasure
            Log.d("HRManager", "Is HR available: $supportsHeartRate")
        }
    }

    private fun checkAndRequestSensorPermission(context: Context, activity: Activity) {
        val permissionRequestCode = 1001
        val permission = Manifest.permission.BODY_SENSORS

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Debug", "Permission not granted")
            ActivityCompat.requestPermissions(activity, arrayOf(permission), permissionRequestCode)
        } else {
            // Permission already granted
            Log.d("Debug", "Permission granted")
            measureClient.registerMeasureCallback(DataType.Companion.HEART_RATE_BPM, callback)
        }
    }


}