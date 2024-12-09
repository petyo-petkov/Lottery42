package com.example.lottery42.boleto.presentation.extra_info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.domain.NetworkRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ExtraInfoViewModel(
    val networkRepo: NetworkRepo
) : ViewModel() {

    private val _infoState = MutableStateFlow<InfoSorteoState<Any>>(InfoSorteoState.Empty)
    val infoState: StateFlow<InfoSorteoState<Any>> = _infoState

    fun infoSorteoCelebrado(boleto: Boleto) {
        _infoState.value = InfoSorteoState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val esAnterior = esAnteriorA(boleto.cierre)
            val info = networkRepo.fetchExtraInfo(boleto)
            val infoLNAC = networkRepo.fetchExtraInfoLNAC(boleto)
            _infoState.value = if (esAnterior) {
                try {
                    if (info.isNotEmpty()) {
                        InfoSorteoState.Success(info = info[0])
                    } else {
                        InfoSorteoState.Success(info = infoLNAC[0])
                    }

                } catch (e: Exception) {
                    Log.e("ERROR en obtenerInfoResultado", e.message.toString())
                    InfoSorteoState.Error(e)
                }

            } else {
                InfoSorteoState.Empty
            }
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
            _infoState.value = InfoSorteoState.Error(e)
            false
        }
    }

    sealed class InfoSorteoState<out T> {
        data class Success<T>(val info: T) : InfoSorteoState<T>()
        data class Error(val exception: Exception) : InfoSorteoState<Nothing>()
        object Empty : InfoSorteoState<Nothing>()
        object Loading : InfoSorteoState<Nothing>()
    }

}