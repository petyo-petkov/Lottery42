package com.example.lottery42.boleto.data.network.models.loteriaNacional.resultadoLNAC

import kotlinx.serialization.Serializable

@Serializable
data class ExtraccionesDeTresCifra(
    val alambre: String? = null,
    val decimo: String,
    val fila: Int,
    val literalPremio: String? = null,
    val orden: Int,
    val ordenFila: Int,
    val prize: Int,
    val prizeType: String,
    val showFolded: Boolean,
    val tabla: String? = null
)