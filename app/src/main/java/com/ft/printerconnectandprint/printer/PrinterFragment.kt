package com.ft.printerconnectandprint.printer

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.printerconnectandprint.R
import com.example.printerconnectandprint.databinding.FragmentPrinterBinding
import com.ft.printerconnectandprint.AppViewModel
import com.ft.printerconnectandprint.logE
import com.ft.printerconnectandprint.printer.settings_data_store.SettingsDataStore
import com.ft.printerconnectandprint.toast
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.ui.ScanningActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class PrinterFragment : Fragment() {

    private lateinit var binding: FragmentPrinterBinding
    private val appViewModel by viewModels<AppViewModel>()
    private var isHide: Boolean = false
    private var textSize: Int = 12

    private val permissionList = arrayOf(
        android.Manifest.permission.BLUETOOTH,
        android.Manifest.permission.BLUETOOTH_ADMIN,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.BLUETOOTH_CONNECT,
    )

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == ScanningActivity.SCANNING_FOR_PRINTER && result.resultCode == Activity.RESULT_OK) {
                printDetails(result.data)
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            if (granted.all { it.value }) {
                scanPrinterAndConnect()
            } else {
                "permission denied".toast(requireContext())
            }
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
        appViewModel.getPrinterTextSize()
        appViewModel.getPrinterSize()

        binding.printPrinterChange.setOnClickListener {
            scanPrinterAndConnect()
        }

        binding.printerSettings.setOnClickListener {
            isHide = !isHide
            binding.printSettingLayout.isVisible = isHide
        }

        lifecycleScope.launch {
            appViewModel.printerSizeObserver.collectLatest {
                binding.printerSize.text = it
                it.logE("LOG_E")
            }
        }

        lifecycleScope.launch {
            appViewModel.printerTextSizeObserver.collectLatest {
                "$it".logE("LOG_E")
                textSize = it
                binding.txtSize.text = "$it"
                binding.userText.textSize = it.toFloat()
            }
        }


        binding.printBtn.setOnClickListener {
            if (!Printooth.hasPairedPrinter()) {
                requestPermissionLauncher.launch(permissionList)
            } else {
                "print your text".toast(requireContext())
            }
        }
    }

    private fun scanPrinterAndConnect() {
        resultLauncher.launch(
            Intent(
                requireContext(),
                ScanningActivity::class.java
            )
        )
    }

    override fun onResume() {
        super.onResume()
        setPrinterSize()
        setPrinterTextSize()

        if (!Printooth.hasPairedPrinter()) {
            //scanPrinterAndConnect()
        } else {
            val printer = Printooth.getPairedPrinter()
            binding.printPrinterName.text = printer?.name
            binding.printPrinterAddress.text = printer?.address
        }

    }

    private fun setPrinterTextSize() {
        var job: Job? = null

        binding.txtSizeMinus.setOnClickListener {
            if (textSize > 12 ){
                textSize-=2
                appViewModel.setPrinterTextSize(textSize)
                "textSize = $textSize".logE("LOG_E")
            }
        }
        binding.txtSizePlus.setOnClickListener {
            if (textSize < 100 ){
                textSize+=2
                appViewModel.setPrinterTextSize(textSize)
                "textSize = $textSize".logE("LOG_E")
            }
        }

        binding.txtSizeMinus.setOnLongClickListener {
            if (job == null || !job!!.isActive) {
                job = lifecycleScope.launch {
                    while (it.isPressed) {
                        if (textSize > 12 ){
                            textSize-=2
                            appViewModel.setPrinterTextSize(textSize)
                            "textSize = $textSize".logE("LOG_E")
                        }
                        delay(100)
                    }
                }
            }
            true
        }

        binding.txtSizePlus.setOnLongClickListener {
            if (job == null || !job!!.isActive) {
                job = lifecycleScope.launch {
                    while (it.isPressed) {
                        if (textSize < 100 ){
                            textSize+=2
                            appViewModel.setPrinterTextSize(textSize)
                            "textSize = $textSize".logE("LOG_E")
                        }
                        delay(100)
                    }
                }
            }
            true
        }
    }

    private fun setPrinterSize() {
        binding.printerSize.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.searchable_spinner_layout)
            dialog.window?.setLayout(650, 800)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText: EditText = dialog.findViewById(R.id.editText)
            val listView: ListView = dialog.findViewById(R.id.listView)
            val printerSizeLise = arrayListOf("48mm","72mm" )
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, printerSizeLise)
            listView.adapter = adapter
            editText.addTextChangedListener { adapter.filter.filter(it) }
            listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
                binding.printerSize.text = adapter.getItem(position)
                appViewModel.setPrinterSize(printerSizeLise[position])
                dialog.dismiss()
            }
        }
    }
}