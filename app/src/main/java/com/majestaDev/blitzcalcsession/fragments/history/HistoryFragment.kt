package com.majestaDev.blitzcalcsession.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.majestaDev.blitzcalcsession.R
import com.majestaDev.blitzcalcsession.data.TanksStatisticsViewModel
import kotlinx.android.synthetic.main.history_fragment_layout.view.*

class HistoryFragment: Fragment() {
    private lateinit var tanksStatisticsViewModel: TanksStatisticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.history_fragment_layout, container, false)

        val adapter = HistoryAdapter(requireContext())
        val recyclerView = view.statistics_history
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tanksStatisticsViewModel = ViewModelProvider(this).get(TanksStatisticsViewModel::class.java)
        tanksStatisticsViewModel.getAllTanksStatistics.observe(viewLifecycleOwner, Observer { statistics ->
            adapter.setData(statistics)
        })

        return view
    }
}