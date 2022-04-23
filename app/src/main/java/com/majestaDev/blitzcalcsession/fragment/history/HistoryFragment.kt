package com.majestaDev.blitzcalcsession.fragment.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.majestaDev.blitzcalcsession.R
import com.majestaDev.blitzcalcsession.databinding.FragmentHistoryBinding
import com.majestaDev.blitzcalcsession.db_model.SessionInfoWithTanksStatistics
import com.majestaDev.blitzcalcsession.fragment.main.MainFragmentDirections
import com.majestaDev.blitzcalcsession.model.IListItem
import com.majestaDev.blitzcalcsession.util.BounceEdgeEffectFactory
import com.majestaDev.blitzcalcsession.util.Converter.Companion.convertToListOfTankStatisticWithSessionInfoModel
import com.majestaDev.blitzcalcsession.util.Converter.Companion.toTanksStatisticsModelFinal
import com.majestaDev.blitzcalcsession.viewmodel.HistoryFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener,
    DatePickerDialog.OnDateSetListener, ISelectModeListener {

    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryFragmentViewModel by viewModels()
    private lateinit var adapter: HistoryRecyclerViewAdapter
    private var calendar = Calendar.getInstance()
    private var selectMode = false
    private var selectAll = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        val view = binding.root

        adapter = HistoryRecyclerViewAdapter(this) {
            val action = MainFragmentDirections.actionMainFragmentToSessionDetailsFragment(
                it.toTanksStatisticsModelFinal()
            )
            binding.root.findNavController().navigate(action)
        }

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.edgeEffectFactory = BounceEdgeEffectFactory()

        binding.calendar.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(), this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Reset") { _, _ ->
                viewModel.searchByDate("%%").observe(viewLifecycleOwner, {
                    adapter.setData(it)
                })
                calendar = Calendar.getInstance()
            }

            datePickerDialog.show()
        }

        // SearchView Customization
        val searchText =
            binding.searchLine.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchText.setTextColor(android.graphics.Color.BLACK)
        val hint = ContextCompat.getColor(requireContext(), R.color.hint)
        searchText.setHintTextColor(hint)

        val customGrey = ContextCompat.getColor(requireContext(), R.color.custom_grey)
        val searchButton =
            binding.searchLine.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        searchButton.imageTintList = ColorStateList.valueOf(customGrey)

        val searchCloseButton =
            binding.searchLine.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        searchCloseButton.imageTintList = ColorStateList.valueOf(customGrey)
        // SearchView Customization


        binding.searchLine.setOnQueryTextListener(this)

        binding.deleteAllButton.setOnClickListener {
            if (adapter.listOfSelected.isEmpty()) {
                Toast.makeText(requireContext(), "Select items to delete", Toast.LENGTH_SHORT).show()
            } else {
                deleteSelectedStatisticsDialog(adapter.listOfSelected)
            }
        }

        viewModel.sessions.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })

        binding.selectAllButton.setOnClickListener {
            if (selectAll) {
                selectAll = false
                adapter.unselectAll()
                binding.selectAllButton.setImageResource(R.drawable.ic_circle)
            } else {
                selectAll = true
                adapter.selectAll()
                binding.selectAllButton.setImageResource(R.drawable.ic_check_circle)
            }
        }

        return view
    }

    private fun deleteSelectedStatisticsDialog(listOfSelected: ArrayList<IListItem>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            listOfSelected.forEach {
                val sessionInfo = (it as SessionInfoWithTanksStatistics).sessionInfo
                viewModel.deleteStatistics(sessionInfo)
            }

            selectMode(false)
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete Sessions")
        if (listOfSelected.size > 1) {
            builder.setMessage("Are you sure you want to delete ${listOfSelected.size} sessions?")
        } else {
            builder.setMessage("Are you sure you want to delete ${listOfSelected.size} session?")
        }
        builder.create().show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            search(newText)
        }

        return true
    }

    private fun search(query: String?) {
        val searchQuery = "%$query%"

        if (searchQuery == "%%") {
            viewModel.searchByDate(searchQuery).observe(viewLifecycleOwner, {
                adapter.setData(it)
            })
        } else {
            viewModel.getTankStatisticWithSessionInfoFilteredByName(searchQuery).observe(
                viewLifecycleOwner,
                {
                    val listOfTankStatisticWithSessionInfoModel =
                        convertToListOfTankStatisticWithSessionInfoModel(it)
                    adapter.setData(listOfTankStatisticWithSessionInfoModel)
                })
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val format = "MMM d"
        val sdf = SimpleDateFormat(format)

        viewModel.searchByDate(sdf.format(calendar.time)).observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
    }

    override fun selectMode(state: Boolean) {
        selectMode = state

        if (selectMode) {
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                if (selectMode) {
                    selectMode = false

                    adapter.unselectAll()

                    hideSelectModeMenu()
                }

                this.remove()
            }

            binding.calendar.visibility = View.GONE
            binding.searchLine.visibility = View.GONE

            binding.selectAllButton.visibility = View.VISIBLE
            binding.numberOfSelectedItems.visibility = View.VISIBLE
            binding.deleteAllButton.visibility = View.VISIBLE
        } else {
            hideSelectModeMenu()
        }
    }

    private fun hideSelectModeMenu() {
        binding.selectAllButton.visibility = View.GONE
        binding.numberOfSelectedItems.visibility = View.GONE
        binding.deleteAllButton.visibility = View.INVISIBLE

        binding.calendar.visibility = View.VISIBLE
        binding.searchLine.visibility = View.VISIBLE
    }

    override fun isSelectMode(): Boolean {
        return selectMode
    }

    @SuppressLint("SetTextI18n")
    override fun updateNumberOfSelectedItems(countOfSelectedItems: Int) {
        binding.numberOfSelectedItems.text = "$countOfSelectedItems selected"
    }

    override fun selectAll(state: Boolean) {
        selectAll = state

        if (selectAll) {
            binding.selectAllButton.setImageResource(R.drawable.ic_check_circle)
        } else {
            binding.selectAllButton.setImageResource(R.drawable.ic_circle)
        }
    }
}