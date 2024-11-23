package com.example.lottery42.boleto.presentation.boleto_list

import com.example.lottery42.boleto.data.database.Boleto

data class BoletoListState(
    val boletos: List<Boleto> = emptyList(),
    val Loading: Boolean = true
)