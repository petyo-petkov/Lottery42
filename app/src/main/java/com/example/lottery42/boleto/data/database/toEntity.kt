package com.example.lottery42.boleto.data.database

import com.lottery42.BoletoEntity
import kotlinx.serialization.json.Json

fun Boleto.toEntity(): BoletoEntity {
    val json = Json { encodeDefaults = true }
    return BoletoEntity(
        id = id,
        idSorteo = idSorteo,
        gameID = gameID,
        tipo = tipo,
        combinaciones = combinaciones.toString(),
        fecha = fecha,
        apertura = apertura,
        cierre = cierre,
        precio = precio,
        premio = premio,
        numSorteo = numSorteo,
        apuestaMultiple = if (apuestaMultiple) 1 else 0,
        joker = joker,
        reintegro = reintegro,
        numeroClave = numeroClave.toString(),
        dreams = dreams.toString(),
        estrellas = estrellas.toString(),
        numeroElMillon = numeroElMillon.toString(),
        lluvia = lluvia,
        numeroLoteria = numeroLoteria
    )
}