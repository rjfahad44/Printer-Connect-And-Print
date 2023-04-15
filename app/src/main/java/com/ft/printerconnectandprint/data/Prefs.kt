package com.ft.printerconnectandprint.data

import android.content.Context
import android.content.SharedPreferences
import com.ft.printerconnectandprint.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Prefs @Inject constructor(@ApplicationContext context: Context)
{
    private val APP_PREF_LANGUAGE = "LANGUAGE_BD"
    private val preferences: SharedPreferences = context.getSharedPreferences(APP_PREF_LANGUAGE ,Context.MODE_PRIVATE)

    var printerSizePrefs: Float
        get() = preferences.getFloat(Constants.PRINTER_SIZE, 48f)
        set(value) = preferences.edit().putFloat(Constants.PRINTER_SIZE, value).apply()
}