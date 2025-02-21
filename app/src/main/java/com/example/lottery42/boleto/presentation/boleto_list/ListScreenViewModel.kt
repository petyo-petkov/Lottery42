package com.example.lottery42.boleto.presentation.boleto_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottery42.boleto.data.crearBoleto
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.database.toEntity
import com.example.lottery42.boleto.domain.DatabaseRepo
import com.example.lottery42.boleto.domain.NetworkRepo
import com.example.lottery42.boleto.domain.ScannerRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListScreenViewModel(
    private val databaseRepo: DatabaseRepo,
    private val scannerRepo: ScannerRepo,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    init {
        getAllBoletos("fecha")
    }

    val balance = databaseRepo.getBalance()

    val boletoState: StateFlow<Boleto?>
        field = MutableStateFlow<Boleto?>(null)

    val boletosState: StateFlow<List<Boleto>>
        field = MutableStateFlow<List<Boleto>>(emptyList())


    fun startScanning() {
        viewModelScope.launch(Dispatchers.IO) {
            scannerRepo.startScanning().collect { data ->
                if (!data.isNullOrBlank()) {
                    Log.d("RawData", data)
                    try {
                        val boleto = crearBoleto(data, networkRepo)
                        Log.d("Boleto", boleto.toEntity().toString())
                        databaseRepo.insertBoleto(boleto.toEntity())
                    } catch (e: Exception) {
                        Log.d("Error insertar boleto", e.toString())
                    }

                }
            }
        }
    }


    fun getAllBoletos(order: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepo.getAllBoletos().collect { boletos ->
                boletosState.value = when (order) {
                    "tipo" -> boletos.sortedBy { it.tipo }
                    "premio" -> boletos.sortedByDescending { it.premio.toDouble() }
                    else -> boletos.sortedByDescending { it.fecha }
                }
            }
        }
    }


    fun getBoletoByID(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepo.getBoletoByID(id).collect { boleto ->
                boletoState.value = boleto
            }
        }
    }

    fun deleteAllBoletos() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepo.deleteAllBoletos()
        }
    }

    fun deleteBoleto(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepo.deleteBoletoById(id)
        }
    }
}
