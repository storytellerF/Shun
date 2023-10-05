package com.storyteller_f.common_dialog

import android.content.Context
import android.content.DialogInterface
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class RenameDialog(context: Context, init: String, val rename: (String) -> Unit) {
    private val editText = EditText(context).apply {
        setText(init)
    }

    private val dialog =
        AlertDialog.Builder(context).setTitle("rename").setView(editText)
            .setPositiveButton("OK", null).create().apply {
        }

    fun show() {
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val input = editText.text.toString()
            if (input.isNotEmpty()) {
                rename(input)
                dialog.dismiss()
            }
        }
    }
}