package com.example.simplewotblitzstatistics

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.simplewotblitzstatistics.fragments.calc.CalcSessionFragment
import com.example.simplewotblitzstatistics.fragments.history.HistoryFragment
import com.example.simplewotblitzstatistics.interfaces.IDataTransfer
import com.example.simplewotblitzstatistics.interfaces.INicknameListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.calc_fragment_layout.*

private const val NUM_PAGES = 2

class MainActivity : AppCompatActivity(), INicknameListener, IDataTransfer {
    private var nickname: String = ""
    private val dataController = DataController()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nickname = dataController.getString("NICKNAME", this)

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

    override fun applyText(nickname: String) {
        if (nickname.isNotBlank()) {
            this.nickname = nickname
            dataController.saveString("NICKNAME", nickname, this)
            calc_session.isEnabled = true
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

    fun openDialog() {
        val setNicknameDialog = NicknameDialog(nickname)
        setNicknameDialog.show(supportFragmentManager, "set nickname")
    }

    override fun getData(): String {
        return nickname
    }
}