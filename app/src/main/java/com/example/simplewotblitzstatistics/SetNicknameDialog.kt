package com.example.simplewotblitzstatistics

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException

class SetNicknameDialog(var nickname: String): DialogFragment() {
    private var listener: ISetNicknameListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialog = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.set_nickname_layout, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.set_nickname)
        editText.setText(nickname)
        alertDialog.setView(dialogLayout)
        alertDialog.setPositiveButton("Set") { dialogInterface, i -> listener?.applyText(editText.text.toString())
        }
        alertDialog.setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss()
        }
        return alertDialog.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ISetNicknameListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement ISetNicknameListener")
        }
    }

    interface ISetNicknameListener {
        fun applyText(nickname: String)
    }
}