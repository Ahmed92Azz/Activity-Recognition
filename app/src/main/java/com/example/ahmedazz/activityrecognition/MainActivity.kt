package com.example.ahmedazz.activityrecognition

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.DetectedActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {


    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mBroadcastReceiver: ActivityDetectionBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBroadcastReceiver = ActivityDetectionBroadcastReceiver()

        buildGoogleApiClient()

        request_activity_updates_button.setOnClickListener {
            if (!mGoogleApiClient.isConnected) {
                Toast.makeText(
                    this, getString(R.string.not_connected),
                    Toast.LENGTH_SHORT
                ).show();

                return@setOnClickListener
            }

            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                mGoogleApiClient,
                Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent()
            ).setResultCallback(this)
        }

        remove_activity_updates_button.setOnClickListener {
            if (!mGoogleApiClient.isConnected) {
                Toast.makeText(
                    this, getString(R.string.not_connected),
                    Toast.LENGTH_SHORT
                ).show();

                return@setOnClickListener
            }

            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                mGoogleApiClient,
                getActivityDetectionPendingIntent()
            ).setResultCallback(this)
        }

    }

    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this@MainActivity)
            .addConnectionCallbacks(this@MainActivity)
            .addOnConnectionFailedListener(this@MainActivity)
            .addApi(ActivityRecognition.API)
            .build()
    }

    inner class ActivityDetectionBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val updatesActivity = p1?.getParcelableArrayListExtra<DetectedActivity>(Constants.ACTIVITY_EXTRA)

            var startstatus = ""
            if (updatesActivity != null) {
                for (thisActivity in updatesActivity) {
                    startstatus += getActivityString(thisActivity.type) + thisActivity.confidence + "%\n"
                }
            }
            detectedActivities.text = startstatus
        }

    }

    private fun getActivityString(detectedActivityType: Int): String {

        val res = this.resources
        return when (detectedActivityType) {
            DetectedActivity.IN_VEHICLE -> res.getString(R.string.in_vehicle)
            DetectedActivity.ON_BICYCLE -> res.getString(R.string.on_bicycle)
            DetectedActivity.ON_FOOT -> res.getString(R.string.on_foot)
            DetectedActivity.RUNNING -> res.getString(R.string.running)
            DetectedActivity.STILL -> res.getString(R.string.still)
            DetectedActivity.TILTING -> res.getString(R.string.tilting)
            DetectedActivity.UNKNOWN -> res.getString(R.string.unknown)
            DetectedActivity.WALKING -> res.getString(R.string.walking)
            else -> res.getString(R.string.unidentifiable_activity, detectedActivityType)
        }
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        mGoogleApiClient.disconnect()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this@MainActivity).unregisterReceiver(mBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this@MainActivity)
            .registerReceiver(mBroadcastReceiver, IntentFilter(Constants.BROADCAST_ACTION))
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onResult(p0: Status) {

        if (p0.isSuccess) {

        } else {
        }
    }

    private fun getActivityDetectionPendingIntent(): PendingIntent {

        val intent = Intent(this, IntentDetectedActivities::class.java)

        return PendingIntent.getService(this@MainActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
