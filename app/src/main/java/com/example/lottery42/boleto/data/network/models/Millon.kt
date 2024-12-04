package com.example.lottery42.boleto.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Millon(
    val activo: String? = null,
    val combinacion: String? = null,
    val gameid: String? = null,
    val importe: Int? = null,
    val relsorteoid_asociado: String? = null

)
