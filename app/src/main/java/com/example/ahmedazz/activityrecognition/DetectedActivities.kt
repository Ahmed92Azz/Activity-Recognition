package com.example.ahmedazz.activityrecognition

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity

class IntentDetectedActivities : IntentService(TAG) {

    companion object {
        const val TAG = "detection_is"
    }

    override fun onHandleIntent(intent: Intent?) {
        val result = ActivityRecognitionResult.extractResult(intent)

        val localIntent = Intent(Constants.BROADCAST_ACTION)

        val detectedActivities = result.probableActivities as ArrayList
        Log.i(TAG, "detectedActivities")

        localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivities)

        LocalBroadcastManager.getInstance(this@IntentDetectedActivities).sendBroadcast(localIntent)
    }
}