package com.example.lottery42.boleto.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class EscrutinioJoker(
    val ganadores: Int,
    val orden_escrutinio: String,
    val premio: String,
    val tipo: String
)
