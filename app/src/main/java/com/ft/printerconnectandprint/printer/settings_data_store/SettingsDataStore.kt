package com.ft.printerconnectandprint.printer.settings_data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ft.printerconnectandprint.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsDataStore(private val context: Context) {


    private val PRINTER_SIZE = stringPreferencesKey("printer_size")
    private val PRINTER_TEXT_SIZE = intPreferencesKey("printer_text_size")

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
        name = Constants.DATA_STORE_PREFERENCE_FOR_PRINTER
    )

    suspend fun savePrinterSize(value: String) {
        context.dataStore.edit { preferences ->
            preferences[PRINTER_SIZE] = value
        }
    }

    val printerSizeFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> preferences[PRINTER_SIZE] ?: "48mm" }


    suspend fun savePrinterTextSize(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[PRINTER_TEXT_SIZE] = value
        }
    }

    val printerTextSizeFlow: Flow<Int> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> preferences[PRINTER_TEXT_SIZE] ?: 12 }
}
