package com.majestaDev.blitzcalcsession.fragment.session

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facebook.shimmer.ShimmerFrameLayout
import com.majestaDev.blitzcalcsession.R
import com.majestaDev.blitzcalcsession.databinding.FragmentSessionBinding
import com.majestaDev.blitzcalcsession.fragment.main.MainFragmentDirections
import com.majestaDev.blitzcalcsession.util.ConnectionLiveData
import com.majestaDev.blitzcalcsession.util.LayoutConfigurator
import com.majestaDev.blitzcalcsession.viewmodel.SessionFragmentViewModel
import com.squareup.picasso.Picasso

class SessionFragment : Fragment(), INicknameListener {

    private lateinit var binding: FragmentSessionBinding
    private lateinit var connectionLiveData: ConnectionLiveData
    private val viewModel: SessionFragmentViewModel by viewModels()
    private var hasInternet = false
    private var stopButtonPressed = false
    private var startButtonPressed = false
    private var savingSessionConfirmation = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSessionBinding.inflate(layoutInflater)
        val view = binding.root

        var nicknameExist = false

//        viewModel.addTanksStatistics(
//            TanksStatisticsModelFinal(
//                "Jan 29",
//                "18:44",
//                TotalTanksStatistics(
//                    10000,
//                    100,
//                    100
//                ),
//                listOf(
//                    TankAvgStatisticsPerSession(
//                        10000,
//                        100,
//                        100,
//                        0,
//                        "Tank(22817)",
//                        "Unknown",
//                        ""
//                    )
//                )
//            )
//        )

        connectionLiveData = ConnectionLiveData(requireContext())

        connectionLiveData.observe(viewLifecycleOwner, {
            hasInternet = it

            if (viewModel.getNickname().isEmpty()) {
                openDialog()
            } else {
                if (hasInternet) {
                    binding.playerInfoShimmer.startShimmerAndShow()

                    viewModel.getPlayerPersonalData(viewModel.getNickname())
                }
            }
        })

        viewModel.playerPersonalData.observe(viewLifecycleOwner) { player ->
            if (player == null) {
                nicknameExist = false

                Toast.makeText(
                    requireContext(),
                    "Player ${viewModel.getNickname()} doesn't exist",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                nicknameExist = true

                binding.playerNickname.text = player.nickname

                binding.playerNumberOfBattles.setTextColor(
                    LayoutConfigurator.getColorOfNumberOfBattlesByAccount(
                        requireContext(),
                        player.battles
                    )
                )
                binding.playerNumberOfBattles.text = player.battles.toString()

                binding.playerAvgDamage.setTextColor(
                    LayoutConfigurator.getColorOfAvgDamageByAccount(
                        requireContext(),
                        player.avgDamageDealt
                    )
                )
                binding.playerAvgDamage.text = player.avgDamageDealt.toString()

                binding.playerPercentageOfWins.setTextColor(
                    LayoutConfigurator.getColorOfPercentageOfWins(
                        requireContext(),
                        player.percentageOfWins
                    )
                )
                binding.playerPercentageOfWins.text = player.percentageOfWins.toString()

                if (viewModel.hasSavedStatistics) {
                    binding.cardTotal.totalStatisticShimmer.startShimmerAndShow()
                    binding.cardFavourite.favouriteTankShimmer.startShimmerAndShow()

                    viewModel.getTanksStatisticsOnStop()
                }
            }

            binding.playerInfoShimmer.stopShimmerAndHide()
        }

        if (viewModel.hasSavedStatistics) {
            binding.startAndStopButton.text = getString(R.string.stop)
            binding.activeState.imageTintList =
                ColorStateList.valueOf(Color.GREEN)
            startButtonPressed = true
        }

        viewModel.tanksStatistics.observe(viewLifecycleOwner, { tanksStatistics ->
            binding.cardTotal.totalAvgDamageValue.text =
                tanksStatistics.total?.totalAvgDamagePerSession.toString()
            binding.cardTotal.totalNumberOfBattlesValue.setTextColor(
                LayoutConfigurator
                    .getColorOfNumberOfBattles(
                        requireContext(),
                        tanksStatistics.total?.totalBattlesPerSession ?: 0
                    )
            )
            binding.cardTotal.totalNumberOfBattlesValue.text =
                tanksStatistics.total?.totalBattlesPerSession.toString()
            binding.cardTotal.totalPercentageOfWinsValue.setTextColor(
                LayoutConfigurator
                    .getColorOfPercentageOfWins(
                        requireContext(),
                        tanksStatistics.total?.totalPercentageOfWinsPerSession ?: 0
                    )
            )
            binding.cardTotal.totalPercentageOfWinsValue.text =
                "${tanksStatistics.total?.totalPercentageOfWinsPerSession.toString()}%"
            binding.cardTotal.cardTotal.setOnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToSessionDetailsFragment(
                    tanksStatistics
                )
                findNavController().navigate(action)
            }

            binding.cardTotal.totalStatisticShimmer.stopShimmerAndHide()

            if (savingSessionConfirmation) {
                viewModel.addTanksStatistics(tanksStatistics)
                savingSessionConfirmation = false
            }
        })

        viewModel.favouriteTank.observe(viewLifecycleOwner, {
            binding.cardFavourite.favouriteName.text = it.name
            binding.cardFavourite.avgDamageValue.setTextColor(
                LayoutConfigurator
                    .getColorOfAvgDamage(requireContext(), it.avgDamageDealtPerSession, it.tier)
            )
            binding.cardFavourite.avgDamageValue.text = it.avgDamageDealtPerSession.toString()
            binding.cardFavourite.percentageOfWinsValue.setTextColor(
                LayoutConfigurator
                    .getColorOfPercentageOfWins(requireContext(), it.percentageOfWinsPerSession)
            )
            binding.cardFavourite.percentageOfWinsValue.text = "${it.percentageOfWinsPerSession}%"
            binding.cardFavourite.numberOfBattlesValue.setTextColor(
                LayoutConfigurator
                    .getColorOfNumberOfBattles(requireContext(), it.battlesPerSession)
            )
            binding.cardFavourite.numberOfBattlesValue.text = it.battlesPerSession.toString()
            if (it.urlPreview.isNotEmpty()) {
                Picasso.get().load(LayoutConfigurator.getNationBackground(it.nation))
                    .into(binding.cardFavourite.nationPreview)
                Picasso.get()
                    .load(it.urlPreview)
                    .placeholder(R.drawable.loading_animation)
                    .into(binding.cardFavourite.tankPreview)
            } else {
                binding.cardFavourite.nationPreview.setImageResource(R.drawable.transparent)
                binding.cardFavourite.tankPreview.setImageResource(R.drawable.transparent)
            }
            binding.cardFavourite.tankTier.text = it.tierRoman

            binding.cardFavourite.favouriteTankShimmer.stopShimmerAndHide()
        })

        binding.startAndStopButton.setOnClickListener {
            when (binding.startAndStopButton.text) {
                "Start" -> {
                    if (viewModel.getNickname().isEmpty()) {
                        Toast.makeText(requireContext(), "Nickname not set", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        if (hasInternet) {
                            if (!nicknameExist) {
                                Toast.makeText(
                                    requireContext(),
                                    "Player ${viewModel.getNickname()} doesn't exist",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.getTanksStatisticsOnStart()

                                stopButtonPressed = false
                                startButtonPressed = true
                                binding.startAndStopButton.text = getString(R.string.stop)
                                binding.activeState.imageTintList =
                                    ColorStateList.valueOf(Color.GREEN)
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "No internet connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                "Stop" -> {
                    stopSessionDialog()
                }
            }
        }

        binding.refreshButton.setOnClickListener {
            connectionLiveData = ConnectionLiveData(requireContext())

            connectionLiveData.observe(viewLifecycleOwner, {
                hasInternet = it
                if (hasInternet) {
                    binding.playerInfoShimmer.startShimmerAndShow()

                    viewModel.getPlayerPersonalData(viewModel.getNickname())
                }
            })

            if (startButtonPressed) {
                if (hasInternet) {
                    viewModel.getTanksStatisticsOnStop()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.switchAccountButton.setOnClickListener {
            if (binding.startAndStopButton.text == getString(R.string.stop)) {
                Toast.makeText(
                    requireContext(),
                    "Can't change nickname while session is running",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                openDialog()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        if (startButtonPressed) {
            viewModel.getTanksStatisticsOnStop()
        }
    }

    private fun openDialog() {
        NicknameDialog(viewModel.getNickname(), viewModel.getRegion(), this)
            .show(childFragmentManager, "set nickname")
    }

    override fun applyText(nickname: String, server: String) {
        if (nickname.isNotBlank()) {
            binding.playerInfoShimmer.startShimmerAndShow()

            viewModel.setNickname(nickname)
            viewModel.setRegion(server)

            if (hasInternet) {
                viewModel.getPlayerPersonalData(nickname)
            } else {
                Toast.makeText(
                    requireContext(),
                    "No internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun stopSessionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            savingSessionConfirmation = true
            changeLayoutWhenSessionStopped()
        }
        builder.setNegativeButton("No") { _, _ ->
            savingSessionConfirmation = false
            changeLayoutWhenSessionStopped()
        }
        builder.setNeutralButton("Cancel") { _, _ ->

        }
        builder.setMessage("Save session to history?")
        builder.create().show()
    }

    private fun changeLayoutWhenSessionStopped() {
        if (hasInternet) {
            stopButtonPressed = true
            startButtonPressed = false

            viewModel.getTanksStatisticsOnStop()
            viewModel.clearSavedStatistics()
            binding.startAndStopButton.text = getString(R.string.start)
            binding.activeState.imageTintList = ColorStateList.valueOf(Color.RED)
        } else {
            Toast.makeText(
                requireContext(),
                "No internet connection",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun ShimmerFrameLayout.startShimmerAndShow() {
        this.startShimmer()
        this.showShimmer(true)
    }

    private fun ShimmerFrameLayout.stopShimmerAndHide() {
        this.stopShimmer()
        this.hideShimmer()
    }
}