package com.example.lottery42.boleto.presentation.boleto_list

import com.example.lottery42.boleto.data.database.Boleto

sealed interface ListScreenActions {
    data class onBoletoClick(val boleto: Boleto) : ListScreenActions
    object loadBoletos : ListScreenActions
    object onFABClick : ListScreenActions
    object onBorrarClick : ListScreenActions
}