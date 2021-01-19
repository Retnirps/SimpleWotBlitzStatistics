package com.example.simplewotblitzstatistics

import android.content.Context
import android.widget.Toast

class LayoutManager {
    fun makeToast(message: String, context: Context) {
        val myToast = Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        )
        myToast.show()
    }
}