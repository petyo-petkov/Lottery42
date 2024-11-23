package com.example.lottery42.boleto.data

import com.example.lottery42.boleto.data.database.Boleto
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive

fun crearBoleto(data: String): Boleto {
    val info = parceJson(data)

    val (tipo, gameId) = when (info["P"]?.jsonPrimitive?.content) {
        "1" -> "Primitiva" to "LAPR"
        "4" -> "El Gordo" to "ELGR"
        "2" -> "Bonoloto" to "BONO"
        else -> "Desconosido" to "DESCONOCIDO"
    }
    val combinaciones =
        info["combinaciones"]?.jsonPrimitive?.content?.removeSurrounding("[", "]")?.split(",")
            ?: emptyList()
    val numeroClave =
        info["numeroClave"]?.jsonPrimitive?.content?.removeSurrounding("[", "]")?.split(",")
            ?: emptyList()
    val dreams = info["dreams"]?.jsonPrimitive?.content?.removeSurrounding("[", "]")?.split(",")
        ?: emptyList()
    val estrellas =
        info["estrellas"]?.jsonPrimitive?.content?.removeSurrounding("[", "]")?.split(",")
            ?: emptyList()
    val numeroElMillon =
        info["numeroElMillon"]?.jsonPrimitive?.content?.removeSurrounding("[", "]")?.split(",")
            ?: emptyList()

    return Boleto(
        id = info["A"]?.jsonPrimitive?.content?.slice(0..10)?.toLong() ?: 0L,
        gameID = gameId,
        fecha = info["S"]?.jsonPrimitive?.content?.slice(3..9) ?: "",
        precio = "precio!!!",
        idSorteo = "IdSorteo",
        numSorteo = info["A"]?.jsonPrimitive?.content?.takeLast(3) ?: "",
        apuestaMultiple = false,
        premio = "premio!!!",
        apertura = "fecha-apertura",
        cierre = "fecha-cierre",
        tipo = tipo,
        combinaciones = combinaciones,
        joker = info["J"]?.jsonPrimitive?.content ?: "",
        reintegro = info["R"]?.jsonPrimitive?.content ?: "",
        numeroClave = numeroClave,
        dreams = dreams,
        estrellas = estrellas,
        numeroElMillon = numeroElMillon,
        numeroLoteria = "numeroLoteria"
    )

}

private fun parceJson(data: String): JsonObject {
    val keyValuePairs = data.split(";").filter { it.isNotEmpty() }
    val pairs = keyValuePairs.associate { pair ->
        val combinaciones =
            keyValuePairs.find { it.startsWith(".") }?.substringAfter(".")?.split(".")
        if (pair.startsWith(".")) return@associate "combinaciones" to combinaciones.toString()

        val (key, value) = pair.split("=", limit = 2)
        key to value
    }
    return buildJsonObject { pairs.forEach { (key, value) -> put(key, JsonPrimitive(value)) } }
}

