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
import kotlin.math.round

class ListScreenViewModel(
    val databaseRepo: DatabaseRepo,
    val scannerRepo: ScannerRepo
) : ViewModel() {

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

    init {
        getAllBoletos()
    }

    val _listState = MutableStateFlow(ListScreenState())
    val listState = _listState.asStateFlow()

    fun getAllBoletos() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepo.getAllBoletos().collect { boletos ->
                val ganado = boletos.sumOf { it.premio.toDouble() }
                val gastado = boletos.sumOf { it.precio.toDouble() }
                val balance = ganado - gastado
                _listState.update {
                    it.copy(
                        boletos = boletos,
                        ganado = Round(ganado).toString(),
                        gastado = Round(gastado).toString(),
                        balance = Round(balance).toString(),
                        Loading = false
                    )

                }
            }
        }
    }

    fun deleteAllBoletos() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepo.deleteAllBoletos()
        }

    }


}

// redondea hasta las dos decimas ganado, gastado y balance
private fun Round(dato: Double): Double {
    return round(dato * 100) / 100
}