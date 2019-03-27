package com.example.ahmedazz.activityrecognition

import com.google.android.gms.location.DetectedActivity

class Constants {

    companion object {
        const val PACKAGE_NAME = "com.example.ahmedazz.locationrecognition"

        val BROADCAST_ACTION = "$PACKAGE_NAME.BROADCAST_ACTION"

        val ACTIVITY_EXTRA = "$PACKAGE_NAME.ACTIVITY_EXTRA"

        val SHARED_PREFERENCES_NAME = "$PACKAGE_NAME.SHARED_PREFERENCES"

        val ACTIVITY_UPDATES_REQUESTED_KEY = "$PACKAGE_NAME.ACTIVITY_UPDATES_REQUESTED"

        val DETECTED_ACTIVITIES = "$PACKAGE_NAME.DETECTED_ACTIVITIES"
        val DETECTION_INTERVAL_IN_MILLISECONDS: Long = 0

        val MONITORED_ACTIVITIES = intArrayOf(
            DetectedActivity.STILL,
            DetectedActivity.ON_FOOT,
            DetectedActivity.WALKING,
            DetectedActivity.RUNNING,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.TILTING,
            DetectedActivity.UNKNOWN
        )
    }


}