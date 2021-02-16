package com.example.simplewotblitzstatistics.fragments.calc

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.simplewotblitzstatistics.DataController
import com.example.simplewotblitzstatistics.LayoutManager
import com.example.simplewotblitzstatistics.R
import com.example.simplewotblitzstatistics.data.TanksStatisticsData
import com.example.simplewotblitzstatistics.data.TanksStatisticsViewModel
import com.example.simplewotblitzstatistics.interfaces.IDataTransfer
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.calc_fragment_layout.*

class CalcSessionFragment: Fragment() {
    private var nickname: String = ""
    private val dataController = DataController()
    private var dataTransfer: IDataTransfer? = null
    private lateinit var tanksStatisticsViewModel: TanksStatisticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.calc_fragment_layout, container, false)
        tanksStatisticsViewModel = ViewModelProvider(this).get(TanksStatisticsViewModel::class.java)

        val calcSession = view.findViewById<MaterialButton>(R.id.calc_session)

        nickname = dataTransfer!!.getData()

        if (nickname.isNotEmpty()) {
            calcSession.isEnabled = true
        }

        calcSession.setOnClickListener {
            nickname = dataTransfer!!.getData()
            if (isOnline()) {
                when (calc_session.text) {
                    "Start Calc Session" -> {
                        handleStartSession()
                    }
                    "Stop Calc Session" -> {
                        handleStopSession()
                    }
                }
            } else {
                LayoutManager().getToast("no internet connection", requireContext())
            }
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun handleStartSession() {
        tanks.removeAllViews()
        dataController.setPlayer(nickname)
        val state = dataController.getStartState()

        if (state) {
            LayoutManager().getToast("calculation of your session statistics started", requireContext())
            calc_session.text = "Stop Calc Session"
            calc_session.setIconResource(android.R.drawable.ic_media_pause)
        } else {
            LayoutManager().getToast("no player with this nickname", requireContext())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleStopSession() {
        val tanksStatisticsData = dataController.stopSession()

        insertDataToDatabase(dataController.currentUnixTime, tanksStatisticsData)

        tanksStatisticsData.forEach {
            val view = LayoutManager().getTankStatisticsView(
                tankTitle = it.first,
                tankAvg = it.second,
                tankImageUrl = it.third,
                layout = tanks,
                context = requireContext())
            view.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.show_tank_info_anim))
            tanks.addView(view)
        }

        calc_session.text = "Start Calc Session"
        calc_session.setIconResource(android.R.drawable.ic_media_play)
    }

    private fun serializeObject(list: ArrayList<Triple<String, String, String>>): ByteArray {
        return Gson().toJson(list).toByteArray()
    }

    private fun isOnline(): Boolean {
        val connMgr = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataTransfer = context as IDataTransfer
    }

    private fun insertDataToDatabase(startTime: Long, data: ArrayList<Triple<String, String, String>>) {
        val dataBlob = serializeObject(data)
        val tanksStatisticsData = TanksStatisticsData(0, startTime, dataBlob)
        tanksStatisticsViewModel.addTanksStatistics(tanksStatisticsData)
    }
}