package com.ft.printerconnectandprint.di


import com.ft.printerconnectandprint.data.data_store_pref.DataStoreRepository
import com.ft.printerconnectandprint.data.data_store_pref.DataStoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PreferenceModule {
    @Binds
    fun bindDataStoreRepository(repo: DataStoreRepositoryImpl): DataStoreRepository
}