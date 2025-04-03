package com.example.lottery42.boleto.data.crearBoleto

import android.util.Log
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.database.combinacionesPosibles
import com.example.lottery42.boleto.data.fechaParaGuardar
import com.example.lottery42.boleto.domain.NetworkRepo
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun crearBoletoFromQR(data: String, networkRepo: NetworkRepo?): Boleto {

    val info = parceJson(data.substringAfter("|"))

    var precioBoleto = 0.0
    var reintegro = ""
    var joker = ""
    var apuestaMultiple = false
    val combiPosibles: Int
    val columnas: MutableList<String> = mutableListOf()
    var numeroClaveElGordo: List<String> = emptyList()
    var estrellasEuromillones: List<String> = emptyList()
    var numeroELMillon: List<String> = emptyList()
    var lluviaMillones: String? = ""
    var suenos: List<String> = emptyList()
    var numeroLNAC = ""
    //var idSorteoBoleto = ""
    //val idSorteoPrimeraParte = info["A"]?.jsonPrimitive?.content?.slice(0..4) ?: ""

    val idBoleto = info["A"]?.jsonPrimitive?.content?.slice(0..10)?.toLong() ?: 0L
    val fecha = info["S"]?.jsonPrimitive?.content?.slice(3..9) ?: ""
    val numeroSorteo = info["S"]?.jsonPrimitive?.content?.slice(0..2) ?: ""
    val numeroSorteosJugados =
        info["S"]?.jsonPrimitive?.content?.substringAfter(":")?.toInt() ?: 1
    val (tipo, gameId) = when (info["P"]?.jsonPrimitive?.content) {
        "1" -> "Primitiva" to "LAPR"
        "4" -> "El Gordo" to "ELGR"
        "2" -> "Bonoloto" to "BONO"
        "5" -> "Loteria Nacional" to "LNAC"
        "7" -> "Euro Millones" to "EMIL"
        "14" -> "Euro Dreams" to "EDMS"
        else -> "Desconosido" to "DESCO"
    }
    val combinaciones =
        info["combinaciones"]?.jsonPrimitive?.content?.removeSurrounding("[", "]")
            ?.split(",")
            ?: emptyList()


    val infoSorteo = try {
        networkRepo?.getInfoSorteo(numeroSorteo, gameId)
    } catch (e: Exception) {
        Log.e(
            "NETWORK ERROR",
            "Error al obtener info del sorteo, en crear desde qr:  ${e.message}"
        )
        null
    }
    Log.i("infoSorteo", infoSorteo.toString())
    val aperturaSorteo = infoSorteo?.apertura ?: fecha
    val cierreSorteo = infoSorteo?.cierre ?: fecha
    val idSorteoBoleto = infoSorteo?.idSorteo ?: ""

    when (gameId) {
        "LAPR" -> {
            //idSorteoBoleto = idSorteoPrimeraParte + "04" + numeroSorteo
            reintegro = info["R"]?.jsonPrimitive?.content ?: ""
            val jokerRaw = info["J"]?.jsonPrimitive?.content ?: ""
            joker = if (jokerRaw.length == 6) "0$jokerRaw" else jokerRaw
            columnas += combinaciones.map {
                it.substringAfter("=").chunked(2).joinToString(" ")
            }
            val combinacionesJugadas = columnas[0].split(" ").size
            apuestaMultiple = combinacionesJugadas > 6 || combinacionesJugadas == 5
            if (combinacionesJugadas == 5) {
                combiPosibles = 44
            } else {
                combiPosibles = combinacionesPosibles(combinacionesJugadas, 6)
            }
            precioBoleto = if (apuestaMultiple) {
                ((combiPosibles * 1.0) * numeroSorteosJugados)
            } else {
                (combinaciones.size * 1.0) * numeroSorteosJugados
            }
            if (joker != "NO") precioBoleto += 1.0
        }

        "BONO" -> {
            //idSorteoBoleto = idSorteoPrimeraParte + "01" + numeroSorteo
            reintegro = info["R"]?.jsonPrimitive?.content ?: ""
            columnas += combinaciones.map {
                it.substringAfter("=").chunked(2).joinToString(" ")
            }
            val combinacion = columnas[0].split(" ")
            apuestaMultiple = combinacion.size > 6 || combinacion.size == 5
            if (combinacion.size == 5) {
                combiPosibles = 44
            } else {
                combiPosibles = combinacionesPosibles(combinacion.size, 6)
            }
            precioBoleto = if (apuestaMultiple) {
                ((combiPosibles * 0.5) * numeroSorteosJugados)
            } else {
                ((columnas.size * 0.5) * numeroSorteosJugados)
            }
        }

        "ELGR" -> {
            //idSorteoBoleto = idSorteoPrimeraParte + "05" + numeroSorteo
            columnas += combinaciones.map {
                it.substringAfter("=").chunked(2).dropLast(2).joinToString(" ")
            }
            numeroClaveElGordo = combinaciones.map {
                it.substringAfter(":").chunked(2).joinToString(" ")
            }
            val combinacion = columnas[0].split(" ").size
            apuestaMultiple = combinacion > 5
            combiPosibles = combinacionesPosibles(combinacion, 5)
            precioBoleto = if (apuestaMultiple) {
                ((combiPosibles * 1.5) * numeroSorteosJugados)
            } else {
                ((columnas.size * 1.5) * numeroSorteosJugados)
            }
        }

        "EMIL" -> {
            //idSorteoBoleto = idSorteoPrimeraParte + "02" + numeroSorteo
            numeroELMillon = data.split(";")[6].substringAfter(",").dropLast(1).split("-")
            lluviaMillones = data.split(";")[7].substringAfter(",").dropLast(1)
            columnas += combinaciones.map {
                it.substringAfter("=").substringBefore(":").chunked(2).joinToString(" ")
            }
            estrellasEuromillones = combinaciones.map {
                it.substringAfter(":").chunked(2).joinToString(" ")
            }
            val combinacion = columnas[0].split(" ")
            val estrella = estrellasEuromillones[0].split(" ")
            combiPosibles = combinacionesPosibles(combinacion.size, 5)
            val numerosApuestasEstrellas = combinacionesPosibles(estrella.size, 2)

            apuestaMultiple = combinacion.size > 5 || estrella.size > 2
            precioBoleto = if (apuestaMultiple) {
                ((combiPosibles * numerosApuestasEstrellas) * 2.5)
            } else {
                ((columnas.size * 2.5) * numeroSorteosJugados)
            }

        }

        "EDMS" -> {
            //idSorteoBoleto = idSorteoPrimeraParte + "14" + numeroSorteo
            columnas += combinaciones.map {
                it.substringAfter("=").chunked(2).dropLast(2).joinToString(" ")
            }
            suenos = combinaciones.map {
                it.substringAfter(":").chunked(2).joinToString(" ")
            }
            val combinacion = columnas[0].split(" ")        // Combinacion jugada
            apuestaMultiple = combinacion.size > 6 || combinacion.size == 5
            if (combinacion.size == 5) {
                combiPosibles = 35
            } else {
                combiPosibles = combinacionesPosibles(combinacion.size, 6)
            }
            precioBoleto = if (apuestaMultiple) {
                ((combiPosibles * 2.5) * numeroSorteosJugados)
            } else {
                ((columnas.size * 2.5) * numeroSorteosJugados)
            }
        }

        "LNAC" -> {
            //idSorteoBoleto = idSorteoPrimeraParte + "09" + numeroSorteo
            numeroLNAC = info["N"]?.jsonPrimitive?.content ?: ""
            precioBoleto = infoSorteo?.precio?.toDouble() ?: 0.0
        }
    }
    return Boleto(
        id = idBoleto,
        gameID = gameId,
        fecha = fechaParaGuardar(fecha),
        precio = "$precioBoleto",
        idSorteo = idSorteoBoleto,
        numSorteo = numeroSorteo,
        apuestaMultiple = apuestaMultiple,
        premio = "-0.0",
        apertura = aperturaSorteo,
        cierre = cierreSorteo,
        tipo = tipo,
        combinaciones = columnas,
        joker = joker,
        reintegro = reintegro,
        numeroClave = numeroClaveElGordo,
        dreams = suenos,
        estrellas = estrellasEuromillones,
        numeroElMillon = numeroELMillon,
        lluvia = lluviaMillones,
        numeroLoteria = numeroLNAC
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