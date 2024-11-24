package com.example.lottery42.boleto.presentation.boleto_list

import com.example.lottery42.boleto.data.database.Boleto

data class BoletoListState(
    val boletos: List<Boleto> = emptyList(),
    val ganado: Double = 0.0,
    val gastado: Double = 0.0,
    val balance: Double = 0.0,
    val Loading: Boolean = true
)