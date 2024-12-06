package com.example.lottery42.boleto.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LotteryModel(
    @SerialName("game_id")
    val gameId: String,
    @SerialName("id_sorteo")
    val idSorteo: String,
    @SerialName("fecha_sorteo")
    val fechaSorteo: String,
    val combinacion: String,
    val cdc: String,
    @SerialName("combinacion_acta")
    val combinacionActa: String,
    @SerialName("dia_semana")
    val diaSemana: String,
    val anyo: String,
    val numero: Int,
    @SerialName("premio_bote")
    val premioBote: String,
    val escrutinio: List<Escrutinio>,

    @SerialName("fondo_bote")
    val fondoBote: String? = null,

    val premios: String? = null,
    val apuestas: String? = null,
    val recaudacion: String? = null,

    @SerialName("escrutinio_joker")
    val escrutinioJoker: List<EscrutinioJoker>? = emptyList(),

    val joker: Joker? = null,

    @SerialName("escrutinio_lluvia")
    val escrutinioLluvia: List<EscrutinioLluvia>? = emptyList(),

    @SerialName("escrutinio_millon")
    val escrutinioMillon: List<EscrutinioMillon>? = emptyList(),

    val millon: Millon? = null,
    val lluvia: Lluvia? = null,

    @SerialName("recaudacion_europea")
    val recaudacionEuropea: String? = "",

    )