package com.majestaDev.blitzcalcsession

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.majestaDev.blitzcalcsession.interfaces.INicknameListener
import kotlinx.android.synthetic.main.set_nickname_layout.*
import java.lang.ClassCastException

class NicknameDialog(private var nickname: String, private var server: String): DialogFragment() {
    private var listener: INicknameListener? = null
    private val servers = arrayOf("ru", "eu", "na", "asia")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialog = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.set_nickname_layout, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.set_nickname)

        val serverSpinner = dialogLayout.findViewById<Spinner>(R.id.server_spinner)
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, servers)
        serverSpinner.adapter = adapter

        editText.setText(nickname)
        serverSpinner.setSelection(adapter.getPosition(server))

        alertDialog.setView(dialogLayout)
        alertDialog.setPositiveButton("Set") { dialogInterface, i -> listener?.applyText(editText.text.toString(), serverSpinner.selectedItem.toString())
        }
        alertDialog.setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss()
        }
        return alertDialog.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as INicknameListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement ISetNicknameListener")
        }
    }
}