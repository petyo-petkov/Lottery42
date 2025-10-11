package com.example.lottery42.boleto.presentation.boleto_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.database.toEntity
import com.example.lottery42.boleto.domain.DatabaseRepo
import com.example.lottery42.boleto.domain.NetworkRepo
import com.example.lottery42.boleto.presentation.esAnterior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.time.format.DateTimeParseException

class DetailsViewModel(
    private val databaseRepo: DatabaseRepo,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    private val _premioState = MutableStateFlow<PremioState>(PremioState.Empty)
    val premioState: StateFlow<PremioState> = _premioState

//    val premioState: StateFlow<PremioState>
//        field = MutableStateFlow<PremioState>(PremioState.Empty)


    fun getPremio(boleto: Boleto) {
        _premioState.value = PremioState.Loading
        viewModelScope.launch(Dispatchers.Default) {
            val esAnterior = try {
                esAnterior(boleto.cierre)
            } catch (e: DateTimeParseException) {
                Log.e("Error", "Error al parsear la fecha: ${e.message}")
                _premioState.value = PremioState.Error(e)
                false
            }
            if (esAnterior) {
                try {
                    val premio = withTimeout(5000) {
                        if (boleto.gameID == "LNAC") {
                            networkRepo.getPremioLNAC(boleto)
                        } else {
                            networkRepo.getPremios(boleto)
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

    private suspend fun manejarResultadoPremio(rawPremio: String?, boleto: Boleto) {
        val premio = rawPremio?.removeSuffix("€")?.replace(",", ".")
        when (premio) {
            "0.0" -> {
                databaseRepo.updateBoleto(boleto.copy(premio = premio).toEntity())
                _premioState.value = PremioState.Success("NO PREMIADO")
            }
            "Error Boton" -> _premioState.value =
                PremioState.Error(Exception("Error obtener el premio"))

            else -> {
                databaseRepo.updateBoleto(boleto.copy(premio = premio!!).toEntity())
                _premioState.value = PremioState.Success("$premio €")
            }

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