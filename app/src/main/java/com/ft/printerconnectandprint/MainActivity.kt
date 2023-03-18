package com.ft.printerconnectandprint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.printerconnectandprint.R
import com.example.printerconnectandprint.databinding.ActivityMainBinding
import com.ft.printerconnectandprint.printer.PrinterFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            PrinterFragment()
        ).commit()
    }
}