package com.example.lottery42.boleto.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class LotteryModel(
    val game_id: String,
    val id_sorteo: String,
    val fecha_sorteo: String,
    val combinacion: String,
    val apuestas: String,
    val cdc: String,
    val combinacion_acta: String,
    val dia_semana: String,
    val anyo: String,
    val fondo_bote: String,
    val numero: Int,
    val premio_bote: String,
    val premios: String,
    val recaudacion: String?,

    val escrutinio: List<Escrutinio>,

    val escrutinio_joker: List<EscrutinioJoker>? = emptyList(),
    val joker: Joker? = null,

    val escrutinio_lluvia: String? = "",
    val escrutinio_millon: List<EscrutinioMillon>? = emptyList(),
    val millon: Millon? = null,
    val recaudacion_europea: String? = "",



)