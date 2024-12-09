package com.example.lottery42.boleto.presentation.boleto_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.database.toEntity
import com.example.lottery42.boleto.domain.DatabaseRepo
import com.example.lottery42.boleto.domain.NetworkRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DetailsViewModel(
    private val databaseRepo: DatabaseRepo,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    private val _premioState = MutableStateFlow<PremioState>(PremioState.Empty)
    val premioState: StateFlow<PremioState> = _premioState

    fun getPremio(boleto: Boleto) {
        _premioState.value = PremioState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val esAnterior = esAnteriorA(boleto.cierre)
            if (esAnterior) {
                try {
                    val premio = withTimeout(5000) {
                        if (boleto.gameID == "LNAC") {
                            networkRepo.getPremioLNAC(boleto)
                        } else {
                            networkRepo.getPremios(boleto).firstOrNull()?.removeSuffix("€")
                                ?.replace(",", ".")
                                ?: " "
                        }
                    }
                    manejarResultadoPremio(premio, boleto)
                } catch (e: TimeoutCancellationException) {
                    Log.e("ERROR en obtenerPremio", e.message.toString())
                    _premioState.value = PremioState.Timeout
                } catch (e: Exception) {
                    Log.e("ERROR en obtenerPremio", e.message.toString())
                    _premioState.value = PremioState.Error(e)
                }
            } else {
                _premioState.value = PremioState.Empty
            }

        }

    }

    private suspend fun manejarResultadoPremio(premio: String?, boleto: Boleto) {
        if (premio == "0.0") {
            _premioState.value = PremioState.Success("NO PREMIADO")
        } else if (premio != null) {
            databaseRepo.updateBoleto(boleto.copy(premio = premio).toEntity())
            _premioState.value = PremioState.Success("$premio €")
        }
    }

    private fun esAnteriorA(fechaCierre: String?): Boolean {
        if (fechaCierre.isNullOrEmpty()) return false
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val fechaCierreParseada = LocalDateTime.parse(fechaCierre, formatter)
            val hoy = LocalDateTime.now()
            fechaCierreParseada.plusHours(1).isBefore(hoy)
        } catch (e: DateTimeParseException) {
            Log.e("Error", "Error al parsear la fecha: ${e.message}")
            _premioState.value = PremioState.Error(e)
            false
        }
    }


    sealed class PremioState {
        data class Success(val premio: String) : PremioState()
        data class Error(val exception: Exception) : PremioState()
        object Empty : PremioState()
        object Loading : PremioState()
        object Timeout : PremioState()
    }
}