package com.example.lottery42.boleto.data.database

import kotlinx.serialization.Serializable

@Serializable
data class Boleto(
    val id: Long = 0L,
    val gameID: String = "",
    val tipo: String = "",
    val fecha: String = "",
    val apertura: String = "",
    val cierre: String = "",
    val precio: String = "",
    val combinaciones: List<String> = emptyList(),
    val reintegro: String? = "",
    val premio: String = "",
    val idSorteo: String = "",
    val numSorteo: String = "",
    val apuestaMultiple: Boolean = false,

    val joker: String? = "",

    val numeroClave: List<String> = emptyList(),

    val dreams: List<String> = emptyList(),

    val estrellas: List<String> = emptyList(),
    val numeroElMillon: List<String> = emptyList(),

    val numeroLoteria: String? = "",

    )

