package com.ft.printerconnectandprint.ui.printer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.ft.printerconnectandprint.MainActivity
import com.ft.printerconnectandprint.prefs
import com.ft.printerconnectandprint.toast
import com.mazenrashed.printooth.Printooth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.StringBuilder

class PrinterUtils {
    companion object {
        private fun checkConnection(): Boolean = Printooth.hasPairedPrinter()

        @SuppressLint("MissingPermission")
        fun printReceipt(context: Context, bitmap: Bitmap? = null) {

            if (!checkConnection()) {
                "Please Connect Printer".toast(context)
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val connections = BluetoothConnections().list
                        connections?.find { it.device.address == Printooth.getPairedPrinter()?.address }
                            ?.let {
                                Log.e("printer", it.device.name.toString())

                                val printer = EscPosPrinter(
                                    it,
                                    203,
                                    prefs.printerSizePrefs,
                                    32
                                )

                                val decodedString: ByteArray = Base64.decode(
                                    getBase64String(bitmap!!),
                                    Base64.DEFAULT
                                )
                                val decodedByte = BitmapFactory.decodeByteArray(
                                    decodedString,
                                    0,
                                    decodedString.size
                                )
                                val width = decodedByte.width
                                val height = decodedByte.height
                                val textToPrint = StringBuilder()
                                var y = 0
                                while (y < height) {
                                    val _bitmap = Bitmap.createBitmap(
                                        decodedByte,
                                        0,
                                        y,
                                        width,
                                        if (y + 256 >= height) height - y else 256
                                    )
                                    textToPrint.append(
                                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(
                                            printer,
                                            _bitmap
                                        ) + "</img>\n"
                                    )
                                    y += 256
                                }
                                printer.printFormattedTextAndCut(textToPrint.toString())
                            }

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        private fun getBase64String(bitmap: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes: ByteArray = baos.toByteArray()
            return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
        }

    }
}