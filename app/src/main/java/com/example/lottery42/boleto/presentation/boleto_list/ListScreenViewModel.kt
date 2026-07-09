package com.example.lottery42.boleto.presentation.boleto_list

import android.util.Log
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottery42.boleto.data.crearBoleto
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.database.toEntity
import com.example.lottery42.boleto.domain.BackupRepo
import com.example.lottery42.boleto.domain.DatabaseRepo
import com.example.lottery42.boleto.domain.NetworkRepo
import com.example.lottery42.boleto.domain.ScannerRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class ListScreenViewModel(
    private val databaseRepo: DatabaseRepo,
    private val scannerRepo: ScannerRepo,
    private val networkRepo: NetworkRepo,
    private val backupRepo: BackupRepo
) : ViewModel() {

//    init {
//        getAllBoletos("fecha")
//    }

    val _boletoState = MutableStateFlow<Boleto?>(null)
    val boletoState: StateFlow<Boleto?> = _boletoState


    val _boletosState = MutableStateFlow<List<Boleto>>(emptyList())
    val boletosState: StateFlow<List<Boleto>> = _boletosState
        .onStart { getAllBoletos("fecha") }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val balance = databaseRepo.getBalance(boletosState)

    fun startScanning() {
        viewModelScope.launch(Dispatchers.IO) {
            scannerRepo.startScanning().collect { data ->
                if (!data.isNullOrBlank()) {
                    Log.d("RawData", data)
                    try {
                        val boleto = crearBoleto(data, networkRepo)
                        Log.d("Boleto", "$boleto")
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
            databaseRepo.getAllBoletos(order).collect { boletos ->
                _boletosState.value = boletos
            }
        }
    }

    fun getBoletoByID(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepo.getBoletoByID(id).collect { boleto ->
                _boletoState.value = boleto
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun sortBoletosByDate(startDate: Long, endDate: Long) {
        val start = Instant.fromEpochMilliseconds(startDate).toString()
        val end = Instant.fromEpochMilliseconds(endDate).toString()
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepo.getBoletosByDateRange(start, end).collect { boletos ->
                _boletosState.value = boletos.sortedByDescending { it.fecha }
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

    fun backupDatabase(uri: Uri) {
        viewModelScope.launch {
            backupRepo.backupDatabase(uri).onSuccess {
                Log.d("Backup", "Backup successful")
            }.onFailure {
                Log.e("Backup", "Backup failed", it)
            }
        }
    }

    fun restoreDatabase(uri: Uri, onSuccess: () -> Unit) {
        viewModelScope.launch {
            backupRepo.restoreDatabase(uri).onSuccess {
                Log.d("Restore", "Restore successful")
                onSuccess()
            }.onFailure {
                Log.e("Restore", "Restore failed", it)
            }
        }
    }
}
