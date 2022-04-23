package com.majestaDev.blitzcalcsession.fragment.history

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.majestaDev.blitzcalcsession.R
import com.majestaDev.blitzcalcsession.databinding.SessionRowBinding
import com.majestaDev.blitzcalcsession.databinding.TankStatisticWithDateRow2Binding
import com.majestaDev.blitzcalcsession.db_model.SessionInfoWithTanksStatistics
import com.majestaDev.blitzcalcsession.fragment.main.MainFragmentDirections
import com.majestaDev.blitzcalcsession.model.IListItem
import com.majestaDev.blitzcalcsession.model.TankStatisticWithSessionInfoModel
import com.majestaDev.blitzcalcsession.util.Converter
import com.majestaDev.blitzcalcsession.util.LayoutConfigurator
import com.squareup.picasso.Picasso

class HistoryRecyclerViewAdapter(
    private val selectModeListener: ISelectModeListener,
    private val onClick: (SessionInfoWithTanksStatistics) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var statisticsList = emptyList<IListItem>()
    private val TYPE_SESSION_INFO_WITH_TANKS_STATISTICS = 1
    private val TYPE_TANK_STATISTIC_WITH_SESSION_INFO_MODEL = 2
    var listOfSelected = ArrayList<IListItem>()

    inner class SessionRowViewHolder(private val binding: SessionRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(sessionInfoWithTanksStatistics: SessionInfoWithTanksStatistics) {
            binding.sessionTitle.text =
                "${sessionInfoWithTanksStatistics.sessionInfo.date} ${sessionInfoWithTanksStatistics.sessionInfo.time}"
            binding.sessionPercentageOfWinsValue.setTextColor(
                LayoutConfigurator
                    .getColorOfPercentageOfWins(
                        binding.root.context,
                        sessionInfoWithTanksStatistics.sessionInfo.totalSessionStatistic.totalPercentageOfWinsPerSession
                    )
            )
            binding.sessionNumberOfBattlesValue.setTextColor(
                LayoutConfigurator
                    .getColorOfNumberOfBattles(
                        binding.root.context,
                        sessionInfoWithTanksStatistics.sessionInfo.totalSessionStatistic.totalBattlesPerSession
                    )
            )

            binding.sessionAvgDamageValue.text =
                sessionInfoWithTanksStatistics.sessionInfo.totalSessionStatistic.totalAvgDamagePerSession.toString()
            binding.sessionPercentageOfWinsValue.text =
                "${sessionInfoWithTanksStatistics.sessionInfo.totalSessionStatistic.totalPercentageOfWinsPerSession}%"
            binding.sessionNumberOfBattlesValue.text =
                sessionInfoWithTanksStatistics.sessionInfo.totalSessionStatistic.totalBattlesPerSession.toString()

            binding.cardSession.setOnLongClickListener {
                if (!selectModeListener.isSelectMode()) {
                    selectModeListener.selectMode(true)
                    selectRow(binding, adapterPosition)
                }

                true
            }

            binding.cardSession.setOnClickListener {
                if (selectModeListener.isSelectMode()) {
                    selectRow(binding, adapterPosition)
                } else {
                    onClick(sessionInfoWithTanksStatistics)
                }
            }

            if (listOfSelected.contains(sessionInfoWithTanksStatistics)) {
                binding.deleteMark.visibility = View.VISIBLE
                val selectColor = ContextCompat.getColor(binding.root.context, R.color.purple_200)
                binding.cardSession.setCardBackgroundColor(selectColor)
            } else {
                binding.deleteMark.visibility = View.INVISIBLE
                val defaultColor = ContextCompat.getColor(binding.root.context, R.color.white)
                binding.cardSession.setCardBackgroundColor(defaultColor)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun selectRow(binding: SessionRowBinding, position: Int) {
        if (binding.deleteMark.visibility == View.INVISIBLE) {
            binding.deleteMark.visibility = View.VISIBLE
            val hint = ContextCompat.getColor(binding.root.context, R.color.purple_200)
            binding.cardSession.setCardBackgroundColor(hint)
            listOfSelected.add(statisticsList[position])
        } else {
            binding.deleteMark.visibility = View.INVISIBLE
            val white = ContextCompat.getColor(binding.root.context, R.color.white)
            binding.cardSession.setCardBackgroundColor(white)
            listOfSelected.remove(statisticsList[position])
            if (listOfSelected.isEmpty()) {
                selectModeListener.selectMode(false)
            }
        }

        selectModeListener.selectAll(listOfSelected.size == statisticsList.size)
        selectModeListener.updateNumberOfSelectedItems(listOfSelected.size)
    }

    inner class TankStatisticWithDateRowViewHolder(private val binding: TankStatisticWithDateRow2Binding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(tankStatisticWithSessionInfoModel: TankStatisticWithSessionInfoModel) {
            val content =
                SpannableString("  ${tankStatisticWithSessionInfoModel.date} ${tankStatisticWithSessionInfoModel.time}  ")
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            binding.tankStatisticDate.text = content
            binding.tankName.text = tankStatisticWithSessionInfoModel.statistic.name
            binding.avgDamageValue.setTextColor(
                LayoutConfigurator
                    .getColorOfAvgDamage(
                        binding.root.context,
                        tankStatisticWithSessionInfoModel.statistic.avgDamageDealtPerSession,
                        tankStatisticWithSessionInfoModel.statistic.tier
                    )
            )
            binding.avgDamageValue.text =
                tankStatisticWithSessionInfoModel.statistic.avgDamageDealtPerSession.toString()
            binding.percentageOfWinsValue.setTextColor(
                LayoutConfigurator
                    .getColorOfPercentageOfWins(
                        binding.root.context,
                        tankStatisticWithSessionInfoModel.statistic.percentageOfWinsPerSession
                    )
            )
            binding.percentageOfWinsValue.text =
                "${tankStatisticWithSessionInfoModel.statistic.percentageOfWinsPerSession}%"
            binding.numberOfBattlesValue.setTextColor(
                LayoutConfigurator
                    .getColorOfNumberOfBattles(
                        binding.root.context,
                        tankStatisticWithSessionInfoModel.statistic.battlesPerSession
                    )
            )
            binding.numberOfBattlesValue.text =
                tankStatisticWithSessionInfoModel.statistic.battlesPerSession.toString()
            Picasso.get()
                .load(LayoutConfigurator.getNationBackground(tankStatisticWithSessionInfoModel.statistic.nation))
                .into(
                    binding.nationPreview
                )
            Picasso.get().load(tankStatisticWithSessionInfoModel.statistic.urlPreview)
                .into(binding.tankPreview)
            binding.tankTier.text = tankStatisticWithSessionInfoModel.statistic.tierRoman
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SESSION_INFO_WITH_TANKS_STATISTICS) {
            SessionRowViewHolder(
                SessionRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            TankStatisticWithDateRowViewHolder(
                TankStatisticWithDateRow2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = statisticsList[position]

        if (getItemViewType(position) == TYPE_SESSION_INFO_WITH_TANKS_STATISTICS) {
            (holder as SessionRowViewHolder).bind(currentItem as SessionInfoWithTanksStatistics)
        } else {
            (holder as TankStatisticWithDateRowViewHolder).bind(currentItem as TankStatisticWithSessionInfoModel)
        }
    }

    override fun getItemCount(): Int {
        return statisticsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (statisticsList[position] is SessionInfoWithTanksStatistics) {
            TYPE_SESSION_INFO_WITH_TANKS_STATISTICS
        } else {
            TYPE_TANK_STATISTIC_WITH_SESSION_INFO_MODEL
        }
    }

    fun setData(statistics: List<IListItem>) {
        statisticsList = statistics
        listOfSelected.clear()
        notifyDataSetChanged()
    }

    fun unselectAll() {
        listOfSelected.clear()
        if (listOfSelected.isEmpty()) {
            selectModeListener.selectMode(false)
        }
        notifyDataSetChanged()
    }

    fun selectAll() {
        listOfSelected.clear()
        listOfSelected.addAll(statisticsList)
        selectModeListener.updateNumberOfSelectedItems(listOfSelected.size)
        notifyDataSetChanged()
    }
}