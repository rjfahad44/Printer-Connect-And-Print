package com.ft.printerconnectandprint

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ft.printerconnectandprint.data.data_store_pref.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AppViewModel @Inject constructor(@ApplicationContext context: Context, private val dataStoreRepository: DataStoreRepository) : ViewModel() {

    private var _printerSizeObserver = MutableStateFlow<Float>(48f)
    var printerSizeObserver: StateFlow<Float> = _printerSizeObserver

    fun getPrinterSize() = viewModelScope.launch{
        dataStoreRepository.getPrinterSize().collectLatest {
            _printerSizeObserver.value = it
        }
    }

    fun setPrinterSize(size: Float) = viewModelScope.launch{
        dataStoreRepository.setPrinterSize(size)
    }

    private var _printerTextSizeObserver = MutableStateFlow<Float>(12f)
    var printerTextSizeObserver: StateFlow<Float> = _printerTextSizeObserver

    fun getPrinterTextSize() = viewModelScope.launch{
        dataStoreRepository.getPrinterTextSize().collectLatest {
            _printerTextSizeObserver.value = it
        }
    }

    fun setPrinterTextSize(size: Float) = viewModelScope.launch{
        dataStoreRepository.setPrinterTextSize(size)
    }




}