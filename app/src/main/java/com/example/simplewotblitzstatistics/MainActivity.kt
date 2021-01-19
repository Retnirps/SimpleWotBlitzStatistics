package com.example.simplewotblitzstatistics

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tank_info.view.*

class MainActivity : AppCompatActivity(), NicknameDialog.INicknameListener {
    var nickname: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nickname = DataController().getString("NICKNAME", this)

        if (nickname.isEmpty()) {
            calc_session.isEnabled = false
            openDialog()
        }

        val dataController = DataController()

        calc_session.setOnClickListener {
            if (isOnline()) {
                when (calc_session.text) {
                    "Start Calc Session" -> {
                        tanks.removeAllViews()
                        dataController.setPlayer(nickname)
                        val state = dataController.getStartState()

                        if (state) {
                            LayoutManager().makeToast("calculation of your session statistics started", this)
                            calc_session.text = "Stop Calc Session"
                            calc_session.setIconResource(android.R.drawable.ic_media_pause)
                        } else {
                            LayoutManager().makeToast("no player with this nickname", this)
                        }
                    }
                    "Stop Calc Session" -> {
                        val tanksStatisticsData = dataController.stopSession()

                        tanksStatisticsData.forEach {
                            val inflater = LayoutInflater.from(this)
                            val view = inflater.inflate(R.layout.tank_info, tanks, false)
                            view.tank_title.text = it.first
                            view.tank_avg.text = it.second
                            Picasso.get().load(it.third).into(view.tank_image)
                            tanks.addView(view)
                        }

                        calc_session.text = "Start Calc Session"
                        calc_session.setIconResource(android.R.drawable.ic_media_play)
                    }
                }
            } else {
                LayoutManager().makeToast("no internet connection", this)
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
        val setNicknameDialog = NicknameDialog(nickname)
        setNicknameDialog.show(supportFragmentManager, "set nickname")
    }

    override fun applyText(nickname: String) {
        if (nickname.isNotBlank()) {
            this.nickname = nickname
            DataController().saveString("NICKNAME", nickname, this)
            calc_session.isEnabled = true
        }
    }

    private fun isOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}