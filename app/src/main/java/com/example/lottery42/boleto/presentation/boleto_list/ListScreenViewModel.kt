package com.example.lottery42.boleto.presentation.boleto_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottery42.boleto.data.crearBoleto
import com.example.lottery42.boleto.data.database.toEntity
import com.example.lottery42.boleto.domain.DatabaseRepo
import com.example.lottery42.boleto.domain.ScannerRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListScreenViewModel(
    val databaseRepo: DatabaseRepo,
    val scannerRepo: ScannerRepo
): ViewModel() {


    fun startScanning() {
        viewModelScope.launch(Dispatchers.IO) {
            scannerRepo.startScanning()
                .collect { data ->
                    if (!data.isNullOrBlank()) {
                        Log.d("RawData", data)
                        try {
                            val boleto = crearBoleto(data)
                            Log.d("Boleto", boleto.toString())
                            databaseRepo.insertBoleto(boleto.toEntity())

                        } catch (e: Exception) {
                            Log.d("Error insertar boleto", e.toString())
                        }

                    }
                }
        }
    }

    val boletos = databaseRepo.getAllBoletos()

    init {
        getAllBoletos()
    }


    private val _boletoState = MutableStateFlow<BoletoListState>(BoletoListState())
    val boletoState = _boletoState.asStateFlow()

    fun getAllBoletos(){
        viewModelScope.launch(Dispatchers.IO)  {
            boletos.collect { boletos ->
                _boletoState.update {
                    it.copy(
                        boletos = boletos,
                        Loading = false
                    )
                }
            }
        }
    }

    fun deleteAllBoletos(){
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepo.deleteAllBoletos()
        }

    }



}