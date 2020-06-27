package com.ipopov.usplash.ui.base

import android.widget.EditText


fun EditText.onAction(onAction: (Int) -> Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        onAction.invoke(actionId)
        false
    }
}