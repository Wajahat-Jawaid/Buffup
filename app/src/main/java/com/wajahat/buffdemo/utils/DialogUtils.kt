package com.wajahat.buffdemo.utils

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView
import com.wajahat.buffdemo.R
import com.wajahat.buffdemo.base.BaseActivity
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class DialogUtils {

    fun showSimpleDialog(activity: BaseActivity, title: String, message: String):
            Observable<Boolean> {
        val publishSubject = PublishSubject.create<Boolean>()
        activity.runOnUiThread {
            val dialog = Dialog(activity)

            dialog.setContentView(R.layout.simple_message_dialog)
            if (dialog.window == null)
                return@runOnUiThread
            dialog.context.theme.applyStyle(R.style.MyAlertDialog, true)
            val params = dialog.window!!.attributes
            params.width = dialog.context.resources.getDimension(R.dimen.dialogWidth).toInt()
            params.height = dialog.context.resources.getDimension(R.dimen.dialogHeight).toInt()
            dialog.window!!.attributes = params
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.findViewById<TextView>(R.id.title_text).text = title
            dialog.findViewById<TextView>(R.id.message_text).text =
                message
            // Buttons
            val okButton = dialog.findViewById<Button>(R.id.ok_btn)
            okButton.setOnClickListener {
                dialog.dismiss()
                publishSubject.onNext(false)
                publishSubject.onCompleted()
            }

            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }

        return publishSubject
    }
}