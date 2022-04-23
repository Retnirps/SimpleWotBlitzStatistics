package com.majestaDev.blitzcalcsession.fragment.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.majestaDev.blitzcalcsession.databinding.FragmentSessionDetailsBinding
import com.majestaDev.blitzcalcsession.util.BounceEdgeEffectFactory
import com.majestaDev.blitzcalcsession.util.Converter.Companion.toListOfTankAvgStatisticsPerSession
import com.majestaDev.blitzcalcsession.util.LayoutConfigurator
import com.majestaDev.blitzcalcsession.viewmodel.SessionDetailsFragmentViewModel
import kotlinx.coroutines.launch

class SessionDetailsFragment : Fragment() {
    private lateinit var binding: FragmentSessionDetailsBinding
//    private val viewModel: SessionDetailsFragmentViewModel by viewModels()
    private val args by navArgs<SessionDetailsFragmentArgs>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSessionDetailsBinding.inflate(layoutInflater)
        val view = binding.root

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        val adapter = RecyclerViewAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.edgeEffectFactory = BounceEdgeEffectFactory()

        val session = args.selectedSession

        binding.totalPercentageOfWinsValue.setTextColor(
            LayoutConfigurator
                .getColorOfPercentageOfWins(requireContext(), session.total!!.totalPercentageOfWinsPerSession))
        binding.totalNumberOfBattlesValue.setTextColor(
            LayoutConfigurator
                .getColorOfNumberOfBattles(requireContext(), session.total.totalBattlesPerSession))

        binding.sessionDateAndTime.text = "${session.date} ${session.time}"
        binding.totalAvgDamageValue.text = session.total.totalAvgDamagePerSession.toString()
        binding.totalPercentageOfWinsValue.text = "${session.total.totalPercentageOfWinsPerSession}%"
        binding.totalNumberOfBattlesValue.text = session.total.totalBattlesPerSession.toString()

//        lifecycleScope.launch {
//            session.tanks.forEach {
//                if (it.tankAvgStatisticsPerSession.tier == 0) {
//
//                    val tankInfo = viewModel.getTankInfo(it.tankAvgStatisticsPerSession.name)
//
//                    if (tankInfo != null) {
//                        viewModel.updateTankStatistic(
//                            it.statisticsId,
//                            tankInfo.name,
//                            tankInfo.tier,
//                            tankInfo.nation,
//                            tankInfo.images.preview
//                        )
//                    }
//                }
//            }
//
//            adapter.setData(session.tanks.toListOfTankAvgStatisticsPerSession())
//        }

        adapter.setData(session.listOfTanks)

        return view
    }
}