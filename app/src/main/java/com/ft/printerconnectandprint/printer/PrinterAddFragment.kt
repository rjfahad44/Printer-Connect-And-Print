package com.ft.printerconnectandprint.printer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.printerconnectandprint.R
import com.example.printerconnectandprint.databinding.FragmentPrinterAddBinding
import com.ft.printerconnectandprint.changeFragment
import com.mazenrashed.printooth.Printooth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PrinterAddFragment : Fragment() {

    private lateinit var binding: FragmentPrinterAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrinterAddBinding.inflate(inflater)
        initialize()
        return binding.root
    }

    private fun initialize() {
        if (Printooth.hasPairedPrinter()) {
            changeFragment(R.id.fragmentContainer, PrinterFragment(), false)
        }
    }
}