package com.example.lottery42.boleto.presentation.extra_info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.domain.NetworkRepo
import com.example.lottery42.boleto.presentation.esAnterior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeParseException

class ExtraInfoViewModel(
    val networkRepo: NetworkRepo
) : ViewModel() {

//    private val _infoState = MutableStateFlow<InfoSorteoState<Any>>(InfoSorteoState.Empty)
//    val infoState: StateFlow<InfoSorteoState<Any>> = _infoState

    val infoState: StateFlow<InfoSorteoState<Any>>
        field = MutableStateFlow<InfoSorteoState<Any>>(InfoSorteoState.Empty)

    fun infoSorteoCelebrado(boleto: Boleto) {

        viewModelScope.launch(Dispatchers.IO) {
            val esAnterior = try {
                esAnterior(boleto.cierre)
            } catch (e: DateTimeParseException) {
                Log.e("Error", "Error al parsear la fecha: ${e.message}")
                infoState.value = InfoSorteoState.Error(e)
                false
            }
            if (esAnterior) {
                infoState.value = InfoSorteoState.Loading
                val info = networkRepo.fetchExtraInfo(boleto)
                val infoLNAC = networkRepo.fetchExtraInfoLNAC(boleto)

                Log.i("info", info.toString())
                Log.i("infoLNAC", infoLNAC.toString())

                infoState.value = try {
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
                infoState.value = InfoSorteoState.Empty
            }
        }
    }

    sealed class InfoSorteoState<out T> {
        data class Success<T>(val info: T) : InfoSorteoState<T>()
        data class Error(val exception: Exception) : InfoSorteoState<Nothing>()
        object Empty : InfoSorteoState<Nothing>()
        object Loading : InfoSorteoState<Nothing>()
    }

}