package com.example.lottery42.boleto.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Joker(
    val activo: String,
    val bote_joker: Int,
    val combinacion: String?,
    val gameid: String,
    val relsorteoid_asociado: String
)