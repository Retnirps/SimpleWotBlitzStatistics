package com.example.simplewotblitzstatistics

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simplewotblitzstatistics.models.Player
import com.example.simplewotblitzstatistics.models.Tank
import com.example.simplewotblitzstatistics.models.TankStatistics
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tank_info.view.*

class MainActivity : AppCompatActivity(), SetNicknameDialog.ISetNicknameListener {
    var nickname: String = ""
    var player: Player? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var currentUnixTime: Long = 0
        var tanksStatisticsBefore = HashMap<Long, TankStatistics>()

        val preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        nickname = preferences.getString("NICKNAME", "").toString()

        if (nickname.isEmpty()) {
            calc_session.isEnabled = false
            openDialog()
        }

        calc_session.setOnClickListener {
            if (isOnline()) {
                when (calc_session.text) {
                    "Start Calc Session" -> {
                        tanks.removeAllViews();
                        player = GetInfo().getPlayer(nickname)
                        if (player != null) {
                            currentUnixTime = System.currentTimeMillis() / 1000L
                            tanksStatisticsBefore = GetInfo().getTanksStatistics(player!!.accountId)
                            val myToast = Toast.makeText(
                                this,
                                "calculation of your session statistics started",
                                Toast.LENGTH_SHORT
                            )
                            myToast.show()
                            calc_session.text = "Stop Calc Session"
                            calc_session.setIconResource(android.R.drawable.ic_media_pause)
                        } else {
                            val myToast = Toast.makeText(
                                this,
                                "no player with this nickname",
                                Toast.LENGTH_SHORT
                            )
                            myToast.show()
                        }
                    }
                    "Stop Calc Session" -> {
                        val tanksStatisticsAfter = GetInfo().getTanksStatistics(player!!.accountId)
                        val playedAfterPointInTime = HashMap<Long, TankStatistics>()

                        tanksStatisticsAfter.forEach {
                            if (it.value.lastBattleTime > currentUnixTime && it.value.battles != tanksStatisticsBefore[it.key]!!.battles) {
                                playedAfterPointInTime[it.key] = it.value
                            }
                        }

                        val tierLabels: Array<String> =
                            arrayOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X")

                        playedAfterPointInTime.forEach {
                            val damageDealtPerSession: Int
                            val winsPerSession: Int
                            val battlesPerSession: Int
                            val avgDamagePerSession: Int
                            val percentageOfWinsPerSession: Int
                            val tank: Tank

                            if (tanksStatisticsBefore.containsKey(it.key)) {
                                damageDealtPerSession =
                                    it.value.damageDealt - tanksStatisticsBefore[it.key]!!.damageDealt
                                winsPerSession =
                                    it.value.wins - tanksStatisticsBefore[it.key]!!.wins
                                battlesPerSession =
                                    it.value.battles - tanksStatisticsBefore[it.key]!!.battles
                                avgDamagePerSession = damageDealtPerSession / battlesPerSession
                                percentageOfWinsPerSession =
                                    (winsPerSession.toFloat() / battlesPerSession * 100).toInt()
                                tank = GetInfo().getTank(it.value.tankId)
                            } else {
                                damageDealtPerSession = it.value.damageDealt
                                winsPerSession = it.value.wins
                                battlesPerSession = it.value.battles
                                avgDamagePerSession = damageDealtPerSession / battlesPerSession
                                percentageOfWinsPerSession =
                                    (winsPerSession.toFloat() / battlesPerSession * 100).toInt()
                                tank = GetInfo().getTank(it.value.tankId)
                            }

                            val inflater = LayoutInflater.from(this)
                            val view = inflater.inflate(R.layout.tank_info, tanks, false)
                            view.tank_title.text = "${tierLabels[tank.tier]} ${tank.name}"
                            view.tank_avg.text =
                                "avg damage: $avgDamagePerSession\nbattles played: $battlesPerSession\n% of wins: $percentageOfWinsPerSession%"
                            Picasso.get().load(tank.previewImageUrl).into(view.tank_image)
                            tanks.addView(view)
                        }

                        calc_session.text = "Start Calc Session"
                        calc_session.setIconResource(android.R.drawable.ic_media_play)
                    }
                }
            } else {
                val myToast = Toast.makeText(
                    this,
                    "no internet connection",
                    Toast.LENGTH_SHORT
                )
                myToast.show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 1, 0, "Set Nickname")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            1 -> {
                openDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDialog() {
        val setNicknameDialog = SetNicknameDialog(nickname)
        setNicknameDialog.show(supportFragmentManager, "set nickname")
    }

    override fun applyText(nickname: String) {
        if (nickname.isNotBlank()) {
            this.nickname = nickname
            val preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
            val editor = preferences.edit()
            editor.putString("NICKNAME", nickname)
            editor.apply()

            calc_session.isEnabled = true
        }
    }

    private fun isOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}