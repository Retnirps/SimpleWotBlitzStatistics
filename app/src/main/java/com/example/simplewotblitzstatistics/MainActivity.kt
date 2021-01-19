package com.example.simplewotblitzstatistics

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NicknameDialog.INicknameListener {
    private var nickname: String = ""
    private val dataController = DataController()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nickname = dataController.getString("NICKNAME", this)

        if (nickname.isEmpty()) {
            calc_session.isEnabled = false
            openDialog()
        }

        calc_session.setOnClickListener {
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
                LayoutManager().getToast("no internet connection", this)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleStartSession() {
        tanks.removeAllViews()
        dataController.setPlayer(nickname)
        val state = dataController.getStartState()

        if (state) {
            LayoutManager().getToast("calculation of your session statistics started", this)
            calc_session.text = "Stop Calc Session"
            calc_session.setIconResource(android.R.drawable.ic_media_pause)
        } else {
            LayoutManager().getToast("no player with this nickname", this)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleStopSession() {
        val tanksStatisticsData = dataController.stopSession()

        tanksStatisticsData.forEach {
            val view = LayoutManager().getTankStatisticsView(
                tankTitle = it.first,
                tankAvg = it.second,
                tankImageUrl = it.third,
                layout = tanks,
                context = this)
            tanks.addView(view)
        }

        calc_session.text = "Start Calc Session"
        calc_session.setIconResource(android.R.drawable.ic_media_play)
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
        val setNicknameDialog = NicknameDialog(nickname)
        setNicknameDialog.show(supportFragmentManager, "set nickname")
    }

    override fun applyText(nickname: String) {
        if (nickname.isNotBlank()) {
            this.nickname = nickname
            dataController.saveString("NICKNAME", nickname, this)
            calc_session.isEnabled = true
        }
    }

    private fun isOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}