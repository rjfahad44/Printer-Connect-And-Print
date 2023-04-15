package com.ft.printerconnectandprint.data.data_store_pref

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setPrinterSize(size:Float)
    suspend fun getPrinterSize(): Flow<Float>

    suspend fun setPrinterTextSize(size:Float)
    suspend fun getPrinterTextSize(): Flow<Float>

    suspend fun clearAllPreferences()
}