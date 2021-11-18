package com.application.spevents.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.application.spevents.R

class ProgressDialog {
    private lateinit var progressDialog: AlertDialog
    private lateinit var builder: AlertDialog.Builder

    fun show(context: Context) {
        if(!::builder.isInitialized) {
            builder = AlertDialog.Builder(context)
            val inflater: LayoutInflater =  context.getSystemService (Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View = inflater.inflate(R.layout.dialog_progress, null)
            builder.setView(view)
            builder.setCancelable(false)
            progressDialog = builder.create()
        }
        progressDialog.show()
    }

    fun hide() {
        if (::builder.isInitialized) {
            progressDialog.dismiss()
        }
    }
}