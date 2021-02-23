package com.majestaDev.blitzcalcsession.fragments.history

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.majestaDev.blitzcalcsession.LayoutManager
import com.majestaDev.blitzcalcsession.R
import com.majestaDev.blitzcalcsession.data.TanksStatisticsData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.statistics_by_date.view.*

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
        holder.itemView.tanks_in_history.removeAllViews()
        statisticsArrayList.forEach {
            val view = LayoutManager().getTankStatisticsView(
                tankTitle = it.first,
                tankAvg = it.second,
                tankImageUrl = it.third,
                layout = holder.itemView.tanks_in_history,
                context = context)
            holder.itemView.tanks_in_history.addView(view)
        }

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