package com.example.lottery42.boleto.data.network.models.loteriaNacional

import kotlinx.serialization.Serializable

@Serializable
data class ResultadoPorNumeroLoteria(
    val idSorteo: String,
    val decimo: String,
    val serie: String?,
    val fraccion: String?,
    val premioEnCentimos: Int,
    val precioDecimoEnCentimos: Int,
    val codigoPremio: String?
)