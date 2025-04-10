package com.example.lottery42.boleto.data.crearBoleto

import android.util.Log
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.domain.NetworkRepo

suspend fun crearBoletoFromBarcode(data: String, networkRepo: NetworkRepo?): Boleto {

    val info = data.substringAfter("|")

    val gameId = "LNAC"
    val tipo = "Loteria Nacional"
    val numeroSorteo = info.substring(1..3)

    val infoSorteo = try {
        networkRepo?.getInfoSorteo(numeroSorteo, gameId)
    } catch (e: Exception) {
        Log.e("NETWORK ERROR","Error al obtener info del sorteo, en crear desde barcode:  ${e.message}")
        null
    }
    return Boleto(
        id = info.slice(0..10).toLong(),
        gameID = gameId,
        fecha = infoSorteo?.fecha?.substringBefore(" ") ?: "",
        precio = infoSorteo?.precio ?: "0.0",
        idSorteo = infoSorteo?.idSorteo ?: "",
        numSorteo = numeroSorteo,
        apuestaMultiple = false,
        premio = "-0.0",
        apertura = infoSorteo?.apertura ?: "",
        cierre = infoSorteo?.cierre ?: "",
        tipo = tipo,
        combinaciones = emptyList(),
        joker = null,
        reintegro = null,
        numeroClave = emptyList(),
        dreams = emptyList(),
        estrellas = emptyList(),
        numeroElMillon = emptyList(),
        lluvia = null,
        numeroLoteria = info.substring(11..15)
    )
}