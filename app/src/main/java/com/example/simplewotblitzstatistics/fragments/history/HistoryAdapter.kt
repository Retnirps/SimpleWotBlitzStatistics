package com.example.simplewotblitzstatistics.fragments.history

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplewotblitzstatistics.LayoutManager
import com.example.simplewotblitzstatistics.R
import com.example.simplewotblitzstatistics.data.TanksStatisticsData
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.calc_fragment_layout.*
import kotlinx.android.synthetic.main.calc_fragment_layout.view.*
import kotlinx.android.synthetic.main.history_fragment_layout.view.*
import kotlinx.android.synthetic.main.statistics_by_date.view.*
import java.lang.reflect.Type

class HistoryAdapter(var context: Context): RecyclerView.Adapter<MyViewHolder>() {
    private var statisticsList = emptyList<TanksStatisticsData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.statistics_by_date, parent, false))
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = statisticsList[position]

        val sdf = java.text.SimpleDateFormat("MMM d HH:mm")
        val date = java.util.Date(currentItem.timestampOnStart * 1000)

        val statisticsArrayList = deserializeObject(currentItem.statistic) as ArrayList<Triple<String, String, String>>
        val view = LayoutManager().getTankStatisticsView(
                tankTitle = "Leopard 1",
                tankAvg = "avg damage: 3500\nbattles played: 15\n% of wins: 80%",
                tankImageUrl = "http://glossary-ru-static.gcdn.co/icons/wotb/current/uploaded/vehicles/hd_thumbnail/Leopard1.png",
                layout = holder.itemView.tanks_in_history,
                context = context)
        holder.itemView.tanks_in_history.addView(view)

        val view2 = LayoutManager().getTankStatisticsView(
            tankTitle = "Leopard 2",
            tankAvg = "avg damage: 7000\nbattles played: 30\n% of wins: 160%",
            tankImageUrl = "http://glossary-ru-static.gcdn.co/icons/wotb/current/uploaded/vehicles/hd_thumbnail/Leopard1.png",
            layout = holder.itemView.tanks_in_history,
            context = context)
        holder.itemView.tanks_in_history.addView(view2)
//        statisticsArrayList.forEach {
//            val view = LayoutManager().getTankStatisticsView(
//                tankTitle = it.first,
//                tankAvg = it.second,
//                tankImageUrl = it.third,
//                layout = holder.itemView.tanks_in_history,
//                context = context)
//            holder.itemView.tanks_in_history.addView(view)
//        }

        holder.itemView.statistics_date.text = sdf.format(date)
    }

    override fun getItemCount(): Int {
        return statisticsList.size
    }

    private fun deserializeObject(data: ByteArray): ArrayList<*> {
        val json = String(data)
        return Gson().fromJson(json, object: TypeToken<ArrayList<Triple<String, String, String>>>() {}.type)
    }

    fun setData(statisticsList: List<TanksStatisticsData>) {
        this.statisticsList = statisticsList
        notifyDataSetChanged()
    }
}