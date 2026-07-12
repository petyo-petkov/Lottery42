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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

sealed class BoletosQuery {
    data class All(val order: String) : BoletosQuery()
    data class DateRange(val start: String, val end: String) : BoletosQuery()
}

class ListScreenViewModel(
    private val databaseRepo: DatabaseRepo,
    private val scannerRepo: ScannerRepo,
    private val networkRepo: NetworkRepo,
    private val backupRepo: BackupRepo
) : ViewModel() {

    private val _boletoId = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val boletoState: StateFlow<Boleto?> = _boletoId
        .flatMapLatest { id ->
            if (id != null) databaseRepo.getBoletoByID(id)
            else flowOf(null)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val queryState = MutableStateFlow<BoletosQuery>(BoletosQuery.All("fecha"))

    @OptIn(ExperimentalCoroutinesApi::class)
    val boletosState: StateFlow<List<Boleto>> = queryState
        .flatMapLatest { query ->
            when (query) {
                is BoletosQuery.All -> databaseRepo.getAllBoletos(query.order)
                is BoletosQuery.DateRange -> databaseRepo.getBoletosByDateRange(query.start, query.end)
            }
        }
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
        queryState.value = BoletosQuery.All(order)
    }

    fun getBoletoByID(id: Long) {
        _boletoId.value = id
    }

    @OptIn(ExperimentalTime::class)
    fun sortBoletosByDate(startDate: Long, endDate: Long) {
        val start = Instant.fromEpochMilliseconds(startDate).toString()
        val end = Instant.fromEpochMilliseconds(endDate).toString()
        queryState.value = BoletosQuery.DateRange(start, end)
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
