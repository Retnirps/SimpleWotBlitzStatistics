package com.majestaDev.blitzcalcsession

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.majestaDev.blitzcalcsession.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        supportActionBar?.hide()

        setContentView(view)
    }

    override fun onResume() {
        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment_container))
        super.onResume()
    }
}