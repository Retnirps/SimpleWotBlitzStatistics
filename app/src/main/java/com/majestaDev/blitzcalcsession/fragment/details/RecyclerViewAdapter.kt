package com.majestaDev.blitzcalcsession.fragment.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.majestaDev.blitzcalcsession.R
import com.majestaDev.blitzcalcsession.databinding.TankStatisticRowBinding
import com.majestaDev.blitzcalcsession.model.TankAvgStatisticsPerSession
import com.majestaDev.blitzcalcsession.util.LayoutConfigurator
import com.squareup.picasso.Picasso

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {

    private var statisticsList = emptyList<TankAvgStatisticsPerSession?>()

    inner class ItemViewHolder(private val binding: TankStatisticRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(tankAvgStatisticsPerSession: TankAvgStatisticsPerSession) {
            binding.tankName.text = tankAvgStatisticsPerSession.name
            binding.avgDamageValue.setTextColor(
                LayoutConfigurator
                    .getColorOfAvgDamage(
                        binding.root.context,
                        tankAvgStatisticsPerSession.avgDamageDealtPerSession,
                        tankAvgStatisticsPerSession.tier
                    )
            )
            binding.avgDamageValue.text =
                tankAvgStatisticsPerSession.avgDamageDealtPerSession.toString()
            binding.percentageOfWinsValue.setTextColor(
                LayoutConfigurator
                    .getColorOfPercentageOfWins(
                        binding.root.context,
                        tankAvgStatisticsPerSession.percentageOfWinsPerSession
                    )
            )
            binding.percentageOfWinsValue.text =
                "${tankAvgStatisticsPerSession.percentageOfWinsPerSession}%"
            binding.numberOfBattlesValue.setTextColor(
                LayoutConfigurator
                    .getColorOfNumberOfBattles(
                        binding.root.context,
                        tankAvgStatisticsPerSession.battlesPerSession
                    )
            )
            binding.numberOfBattlesValue.text =
                tankAvgStatisticsPerSession.battlesPerSession.toString()
            if (tankAvgStatisticsPerSession.urlPreview.isNotEmpty()) {
                Picasso.get()
                    .load(LayoutConfigurator.getNationBackground(tankAvgStatisticsPerSession.nation))
                    .into(
                        binding.nationPreview
                    )
                Picasso.get()
                    .load(tankAvgStatisticsPerSession.urlPreview)
                    .placeholder(R.drawable.loading_animation)
                    .into(binding.tankPreview)
            } else {
                binding.nationPreview.setImageResource(R.drawable.transparent)
                binding.tankPreview.setImageResource(R.drawable.transparent)
            }
            binding.tankTier.text = tankAvgStatisticsPerSession.tierRoman
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            TankStatisticRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = statisticsList[position]
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return statisticsList.size
    }

    fun setData(statistics: List<TankAvgStatisticsPerSession?>) {
        statisticsList = statistics
        notifyDataSetChanged()
    }
}