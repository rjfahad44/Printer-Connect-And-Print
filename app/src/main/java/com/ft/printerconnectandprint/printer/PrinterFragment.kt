package com.ft.printerconnectandprint.printer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.printerconnectandprint.databinding.FragmentPrinterBinding
import com.ft.printerconnectandprint.checkPermission
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.ui.ScanningActivity

class PrinterFragment : Fragment() {

    private lateinit var binding: FragmentPrinterBinding

    private val permissionList = listOf(
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.BLUETOOTH,
        android.Manifest.permission.BLUETOOTH_ADMIN,
        android.Manifest.permission.ACCESS_FINE_LOCATION)

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == ScanningActivity.SCANNING_FOR_PRINTER && result.resultCode == Activity.RESULT_OK) {
            printDetails(result.data)
        }else checkPermission(requireContext(), permissionList)
    }

    private fun printDetails(result: Intent?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrinterBinding.inflate(inflater)
        initialize()
        return binding.root
    }

    private fun initialize() {

        binding.printBtn.setOnClickListener {
            if (!Printooth.hasPairedPrinter()) {
                resultLauncher.launch(
                    Intent(
                        requireContext(),
                        ScanningActivity::class.java
                    ),
                )
            }
            else{

            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!Printooth.hasPairedPrinter()) {
            resultLauncher.launch(
                Intent(
                    requireContext(),
                    ScanningActivity::class.java
                ),
            )
        }
    }
}