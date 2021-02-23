package com.majestaDev.blitzcalcsession

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.majestaDev.blitzcalcsession.data.TanksStatisticsViewModel
import com.majestaDev.blitzcalcsession.fragments.calc.CalcSessionFragment
import com.majestaDev.blitzcalcsession.fragments.history.HistoryFragment
import com.majestaDev.blitzcalcsession.interfaces.IDataTransfer
import com.majestaDev.blitzcalcsession.interfaces.INicknameListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.calc_fragment_layout.*

private const val NUM_PAGES = 2

class MainActivity : AppCompatActivity(), INicknameListener, IDataTransfer {
    private var nickname: String = ""
    private var server: String = ""
    private val dataController = DataController()
    private lateinit var tanksStatisticsViewModel: TanksStatisticsViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tanksStatisticsViewModel = ViewModelProvider(this).get(TanksStatisticsViewModel::class.java)

        nickname = dataController.getString("NICKNAME", this)
        server = dataController.getString("SERVER", this)

        if (nickname.isEmpty()) {
            openDialog()
        }

        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager2.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(android.R.drawable.ic_media_play)
                    tab.text = "Calc Session"
                }
                1 -> {
                    tab.setIcon(android.R.drawable.ic_menu_recent_history)
                    tab.text = "History"
                }
            }
        }.attach()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    return CalcSessionFragment()
                }
                1 -> HistoryFragment()
                else -> HistoryFragment()
            }
        }
    }

    override fun applyText(nickname: String, server: String) {
        if (nickname.isNotBlank()) {
            this.nickname = nickname
            this.server = server
            dataController.saveString("NICKNAME", nickname, this)
            dataController.saveString("SERVER", server, this)
            calc_session.isEnabled = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 1, 0, "Set Nickname")
        menu?.add(0, 2, 0, "Clear History")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            1 -> {
                openDialog()
            }
            2 -> {
                deleteAllStatistics()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDialog() {
        val setNicknameDialog = NicknameDialog(nickname, server)
        setNicknameDialog.show(supportFragmentManager, "set nickname")
    }

    override fun getNickname(): String {
        return nickname
    }

    override fun getServer(): String {
        return server
    }

    private fun deleteAllStatistics() {
        var builder = AlertDialog.Builder(this)
        builder.setPositiveButton("Yes") { _, _ ->
            tanksStatisticsViewModel.deleteAllStatistics()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Clear History")
        builder.setMessage("Are you sure you want to clear history?")
        builder.create().show()
    }
}