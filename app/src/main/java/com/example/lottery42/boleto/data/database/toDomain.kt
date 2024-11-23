package com.example.lottery42.boleto.data.database

import com.lottery42.BoletoEntity
import kotlinx.serialization.json.Json

fun BoletoEntity.toDomain(): Boleto {
    val json = Json { ignoreUnknownKeys = true }
    return Boleto(
        id = id,
        idSorteo = idSorteo,
        gameID = gameID,
        tipo = tipo,
        fecha = fecha,
        apertura = apertura,
        cierre = cierre,
        precio = precio,
        premio = premio,
        numSorteo = numSorteo,
        apuestaMultiple = 0 != apuestaMultiple.toInt(),
        combinaciones = combinaciones.removeSurrounding("[", "]").split(","),
        joker = joker,
        reintegro = reintegro,

        numeroClave = numeroClave?.let { json.decodeFromString(it) } ?: emptyList(),

        dreams = dreams?.removeSurrounding("[", "]")?.split(",") ?: emptyList(),
        estrellas = estrellas?.removeSurrounding("[", "]")?.split(",") ?: emptyList(),
        numeroElMillon = numeroElMillon?.removeSurrounding("[", "]")?.split(",") ?: emptyList(),
        numeroLoteria = numeroLoteria,
    )
}