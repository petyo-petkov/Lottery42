package com.example.lottery42.boleto.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Lluvia(
    val gameid: String,
    val relsorteoid_asociado: String,
    val combinacion: String,
    val importe: Int,
    val activo: String
)