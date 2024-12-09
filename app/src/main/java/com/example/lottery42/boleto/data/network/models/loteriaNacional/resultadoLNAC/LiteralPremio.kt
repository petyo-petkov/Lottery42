package com.example.lottery42.boleto.data.network.models.loteriaNacional.resultadoLNAC

import kotlinx.serialization.Serializable

@Serializable
data class LiteralPremio(
    val es: String,
    val ca: String,
    val en: String,
    val eu: String,
    val gl: String,
    val va: String
)