package com.majestaDev.blitzcalcsession.fragment.main

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.majestaDev.blitzcalcsession.ScreenSlidePagerAdapter
import com.majestaDev.blitzcalcsession.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        val view = binding.root

        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        binding.viewPager2.adapter = pagerAdapter

        binding.let {
            TabLayoutMediator(it.tabLayout, it.viewPager2) { tab, position ->
                when (position) {
                    0 -> {
                        tab.setIcon(R.drawable.ic_media_play)
                        tab.text = "Session"
                    }
                    1 -> {
                        tab.setIcon(R.drawable.ic_menu_recent_history)
                        tab.text = "History"
                    }
                }
            }.attach()
        }

        return view
    }
}