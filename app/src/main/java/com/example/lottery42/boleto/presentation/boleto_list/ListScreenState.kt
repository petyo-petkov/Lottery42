package com.example.lottery42.boleto.presentation.boleto_list

import com.example.lottery42.boleto.data.database.Boleto

data class ListScreenState(
    val boletos: List<Boleto> = emptyList(),
    val ganado: String = "",
    val gastado: String = "",
    val balance: String = "",
    val Loading: Boolean = true
)