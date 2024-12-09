package com.example.lottery42.boleto.data.network.models.loteriaNacional.resultadoLNAC

import kotlinx.serialization.Serializable

@Serializable
data class PremioEspecial(
    val fila: Int? = null,
    val fraccion: Int? = null,
    val literalPremio: LiteralPremio? = null,
    val numero: String? = null,
    val orden: Int? = null,
    val premio: Int? = null,
    val serie: Int? = null,
    val showFolded: Boolean? = null
)