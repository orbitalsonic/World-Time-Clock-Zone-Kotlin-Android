package com.orbitalsonic.worldtime.utils

import android.app.Activity
import android.util.DisplayMetrics

object ScreenUtils {

    fun getScreenWidth(activity: Activity): Int {

        val outMetrics = DisplayMetrics()

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
            outMetrics.widthPixels
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)

            outMetrics.widthPixels
        }


    }

    fun getScreenHeight(activity: Activity): Int {
        val outMetrics = DisplayMetrics()

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
            outMetrics.heightPixels
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)

            outMetrics.heightPixels
        }
    }
}