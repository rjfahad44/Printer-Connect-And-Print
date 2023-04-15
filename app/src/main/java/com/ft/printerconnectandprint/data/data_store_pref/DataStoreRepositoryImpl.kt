package com.ft.printerconnectandprint.data.data_store_pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ft.printerconnectandprint.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATA_STORE_NAME)

class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DataStoreRepository {

    companion object {
        private val PRINTER_SIZE = floatPreferencesKey(Constants.PRINTER_SIZE)
        private val PRINTER_TEXT_SIZE = floatPreferencesKey(Constants.PRINTER_TEXT_SIZE)
    }

    override suspend fun setPrinterSize(size: Float) {
        context.datastore.edit {
            it[PRINTER_SIZE] = size
        }
    }

    override suspend fun getPrinterSize(): Flow<Float> {
        return context.datastore.data.map {
            it[PRINTER_SIZE] ?: 48f
        }
    }

    override suspend fun setPrinterTextSize(size: Float) {
        context.datastore.edit {
            it[PRINTER_TEXT_SIZE] = size
        }
    }

    override suspend fun getPrinterTextSize(): Flow<Float> {
        return context.datastore.data.map {
            it[PRINTER_TEXT_SIZE] ?: 12f
        }
    }

    override suspend fun clearAllPreferences() {
        context.datastore.edit {
            it.remove(PRINTER_SIZE)
            it.remove(PRINTER_TEXT_SIZE)
            it.clear()
        }
    }
}