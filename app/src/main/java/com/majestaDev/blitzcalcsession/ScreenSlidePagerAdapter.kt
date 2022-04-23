package com.majestaDev.blitzcalcsession

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.majestaDev.blitzcalcsession.fragment.history.HistoryFragment
import com.majestaDev.blitzcalcsession.fragment.session.SessionFragment

private const val NUM_PAGES = 2

class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SessionFragment()
            1 -> HistoryFragment()
            else -> SessionFragment()
        }
    }
}