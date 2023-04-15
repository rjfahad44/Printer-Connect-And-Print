package com.ft.printerconnectandprint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.printerconnectandprint.R
import com.example.printerconnectandprint.databinding.ActivityMainBinding
import com.ft.printerconnectandprint.ui.printer.PrinterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragment()
    }

    private fun initFragment() {
        changeFragment(R.id.fragmentContainer, PrinterFragment(), false)
    }
}