package com.wajahat.buffup.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
object ViewUtils {

    /** Return 40% of the screen width to accommodate buff controls */
    fun getBuffContainerWidth(activity: Activity): Int {
        return (activity.window.decorView.width * 0.4).toInt()
    }

    /** Scale in/out a view
     * @param view View to scale
     * @param scale Scale level
     * */
    fun scaleView(view: View, scale: Float) {
        view.scaleX = scale
        view.scaleY = scale
    }

    fun showToast(context: Context, message: String, isLong: Boolean) {
        Toast.makeText(
            context,
            message,
            if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        ).show()
    }

    fun showToast(context: Context, message: String) {
        showToast(context, message, false)
    }
}