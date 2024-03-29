package com.ft.printerconnectandprint.ui.printer

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.printerconnectandprint.R
import com.example.printerconnectandprint.databinding.FragmentPrinterBinding
import com.ft.printerconnectandprint.*
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.ui.ScanningActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.util.Objects


@AndroidEntryPoint
class PrinterFragment : Fragment() {

    private lateinit var binding: FragmentPrinterBinding
    private val appViewModel by viewModels<AppViewModel>()
    private var isHide: Boolean = false
    private var textSize: Float = 12f

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
                resultLauncher.launch(Intent(requireContext(), ScanningActivity::class.java))
            } else {
                "permission denied".toast(requireContext())
            }
        }


    private fun printDetails(result: Intent?) {
        "${result}".logD("LOG_D")
        "Printer is Connected".toast(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                binding.printerSize.setText(String.format("%.0f mm", it))
                prefs.printerSizePrefs = it
                "${it}".logE("LOG_E")
            }
        }

        lifecycleScope.launch {
            appViewModel.printerTextSizeObserver.collectLatest {
                "$it".logE("LOG_E")
                textSize = it
                binding.txtSize.text = String.format("%.0f", it)
                binding.userText.textSize = it
            }
        }


        binding.printBtn.setOnClickListener {
            if (binding.userText.text.isNullOrEmpty()){
                "Please Type Something.".toast(requireContext())
            }else{
                if (!Printooth.hasPairedPrinter()) {
                    requestPermissionLauncher.launch(permissionList)
                } else {
                    "Please wait....".toast(requireContext(), Toast.LENGTH_SHORT)
                    PrinterUtils.printReceipt(
                        requireContext(), getBitmapFromView(binding.userText)
                    )
                }
            }
        }
    }

    private fun scanPrinterAndConnect() {
        if (!Printooth.hasPairedPrinter()) {
            requestPermissionLauncher.launch(permissionList)
        }else{
            resultLauncher.launch(Intent(requireContext(), ScanningActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        setPrinterSize()
        setPrinterTextSize()

        if (Printooth.hasPairedPrinter()) {
            val printer = Printooth.getPairedPrinter()
            binding.printPrinterName.text = printer?.name
            binding.printPrinterAddress.text = printer?.address
        }

    }

    private fun setPrinterTextSize() {
        var job: Job? = null

        binding.txtSizeMinus.setOnClickListener {
            if (textSize > 12) {
                textSize -= 2
                appViewModel.setPrinterTextSize(textSize)
                "textSize = $textSize".logE("LOG_E")
            }
        }
        binding.txtSizePlus.setOnClickListener {
            if (textSize < 100) {
                textSize += 2
                appViewModel.setPrinterTextSize(textSize)
                "textSize = $textSize".logE("LOG_E")
            }
        }

        binding.txtSizeMinus.setOnLongClickListener {
            if (job == null || !job!!.isActive) {
                job = lifecycleScope.launch {
                    while (it.isPressed) {
                        if (textSize > 12) {
                            textSize -= 2
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
                        if (textSize < 100) {
                            textSize += 2
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

//        val printerSizeList = arrayOf(48f, 72f)
//        val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_menu_item, printerSizeList)
//        binding.printerSize.setAdapter(arrayAdapter)
//        binding.printerSize.setOnItemClickListener { parent, view, position, id ->
//            appViewModel.setPrinterSize(printerSizeList[position])
//        }


        binding.printerSize.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.searchable_spinner_layout)
            dialog.window?.setLayout(650, 800)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText: EditText = dialog.findViewById(R.id.editText)
            val listView: ListView = dialog.findViewById(R.id.listView)
            val printerSizeLise = arrayListOf(48f, 72f)
            val adapter: ArrayAdapter<Float> = ArrayAdapter<Float>(
                requireContext(), android.R.layout.simple_list_item_1, printerSizeLise
            )
            listView.adapter = adapter
            editText.addTextChangedListener { adapter.filter.filter(it) }
            listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
                appViewModel.setPrinterSize(printerSizeLise[position])
                dialog.dismiss()
            }
        }
    }


    private fun getBitmapFromView(view: View): Bitmap? {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }
}