package com.example.lottery42.boleto.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Escrutinio(
    val categoria: Int,
    val ganadores: String,
    val premio: String,
    val tipo: String,

    val ganadores_eu: String? = "",
    val num_pagos: Int? = 0,
    val periodicidad: String? = "",
    val cantidad_periodica: String? = ""
)
