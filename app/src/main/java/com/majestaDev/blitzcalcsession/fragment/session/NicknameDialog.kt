package com.majestaDev.blitzcalcsession.fragment.session

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.majestaDev.blitzcalcsession.databinding.SetNicknameLayoutBinding

class NicknameDialog(
    private var nickname: String,
    private var server: String,
    private val listener: INicknameListener
) : DialogFragment() {
    private val servers = arrayOf("ru", "eu", "na", "asia")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialog = AlertDialog.Builder(requireContext())

        val binding = SetNicknameLayoutBinding.inflate(layoutInflater)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            servers
        )
        binding.serverSpinner.adapter = adapter

        binding.nicknameTextInput.editText?.setText(nickname)
        binding.serverSpinner.setSelection(adapter.getPosition(server))

        alertDialog.setView(binding.root)
        alertDialog.setPositiveButton("Set") { _, _ ->
            listener.applyText(
                binding.nicknameTextInput.editText?.text.toString(),
                binding.serverSpinner.selectedItem.toString()
            )
        }
        alertDialog.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        return alertDialog.create()
    }
}