package com.edcast.myapplication.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.edcast.myapplication.R

class CustomProgressDialog : AppCompatDialogFragment() {

    companion object {

        const val TAG = "CustomProgressDialog"
        private const val KEY_PROGRESS_TEXT = "progress_text"
        private const val KEY_CANCELABLE = "cancelable"

        /**
         * create new instance of @class[CustomProgressDialog]
         * @param title - Title of progress dialog
         * @param cancelable - Should allow to dismiss on outside touch
         * */
        fun newInstance(title: String, cancelable: Boolean = false): CustomProgressDialog {
            val dialog = CustomProgressDialog()
            val bundle = Bundle()
            bundle.putBoolean(KEY_CANCELABLE, cancelable)
            bundle.putString(KEY_PROGRESS_TEXT, title)
            dialog.arguments = bundle
            return dialog
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction

        val builder = AlertDialog.Builder(requireActivity())
        val view = LayoutInflater.from(requireActivity())
            .inflate(R.layout.progress_dialog, null, false)
        builder.setView(view)

        val message = view.findViewById<TextView>(R.id.message)

        val arguments = requireArguments()
        val progressText = arguments.getString(KEY_PROGRESS_TEXT)
        val isDialogCancelable = arguments.getBoolean(KEY_CANCELABLE)
        message.text = progressText
        val dialog = builder.create()
        isCancelable = isDialogCancelable
        dialog.setCanceledOnTouchOutside(isDialogCancelable)
        if (dialog.window != null) {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return dialog
    }
}