package com.example.yuvish.retrofit

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText

fun <T> T?.isNull() = (this == null)

fun <T> T?.isNotNull() = (this != null)

fun <T> T.isZero() = (this == 0)

fun <T> T.isNotZero() = (this != 0)

fun checkLoadedAllData(dataList: List<Any?>): Boolean {
    dataList.forEach {
        if (it.isNull()) return false
    }

    return true
}

fun TextInputEditText.closeKeyboard(context: Context){
    if (this.isFocused) {
        this.clearFocus()
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }
}