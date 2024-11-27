package com.example.lottery42.boleto.data

import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.database.combinacionesPosibles
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive

fun crearBoleto(data: String): Boleto {
    val barCodeType = if (data.startsWith("QR")) "QR" else "BAR"
    var boleto = Boleto()

    when (barCodeType) {
        "QR" -> {
            val info = parceJson(data.substringAfter("|"))

            var precioBoleto = 0.0
            var reintegro = ""
            val apuestaMultiple: Boolean
            val combiPosibles: Int
            val columnas = mutableListOf<String>()
            var numeroClaveElGordo: List<String> = emptyList()
            var estrellasEuromillones: List<String> = emptyList()
            var numeroELMillon: List<String> = emptyList()
            var lluviaMillones: String? = ""
            var suenos: List<String> = emptyList()
            var numeroLNAC = ""


            val fecha = info["S"]?.jsonPrimitive?.content?.slice(3..9) ?: ""
            val numeroSorteosJugados =
                info["S"]?.jsonPrimitive?.content?.substringAfter(":")?.toInt() ?: 1
            val (tipo, gameId) = when (info["P"]?.jsonPrimitive?.content) {
                "1" -> "Primitiva" to "LAPR"
                "4" -> "El Gordo" to "ELGR"
                "2" -> "Bonoloto" to "BONO"
                "10" -> "Loteria Nacional" to "LNAC"
                "7" -> "Euro Millones" to "EMIL"
                "14" -> "Euro Dreams" to "EDMS"
                else -> "Desconosido" to "DESCO"
            }
            val combinaciones =
                info["combinaciones"]?.jsonPrimitive?.content?.removeSurrounding("[", "]")
                    ?.split(",")
                    ?: emptyList()
            val joker = info["J"]?.jsonPrimitive?.content ?: ""

            val dreams =
                info["dreams"]?.jsonPrimitive?.content?.removeSurrounding("[", "]")?.split(",")
                    ?: emptyList()


            when (gameId) {
                "LAPR" -> {
                    reintegro = info["R"]?.jsonPrimitive?.content ?: ""
                    columnas.addAll(combinaciones.map {
                        it.substringAfter("=").chunked(2).joinToString(" ")
                    })
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
                    reintegro = info["R"]?.jsonPrimitive?.content ?: ""
                    columnas.addAll(combinaciones.map {
                        it.substringAfter("=").chunked(2).joinToString(" ")
                    })
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
                    columnas.addAll(combinaciones.map {
                        it.substringAfter("=").chunked(2).dropLast(2).joinToString(" ")
                    })
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
                    numeroELMillon = data.split(";")[6].substringAfter(",").dropLast(1).split("-")
                    lluviaMillones =  data.split(";")[7].substringAfter(",").dropLast(1)
                    columnas.addAll(combinaciones.map {
                        it.substringAfter("=").substringBefore(":").chunked(2).joinToString(" ")
                    })
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
                    columnas.addAll(combinaciones.map {
                        it.substringAfter("=").chunked(2).dropLast(2).joinToString(" ")
                    })
                    suenos = combinaciones.map {
                        it.substringAfter(":").chunked(2).joinToString(" ")
                    }
                    val combinacion = columnas[0].split(" ")        // Combinacion jugada
                    apuestaMultiple = combinacion.size > 6 || combinacion.size == 5
                    if (combinacion.size == 5) {
                        combiPosibles = 35
                    }else {
                        combiPosibles = combinacionesPosibles(combinacion.size, 6)
                    }
                    precioBoleto = if (apuestaMultiple) {
                        ((combiPosibles * 2.5) * numeroSorteosJugados)
                    } else {
                        ((columnas.size * 2.5) * numeroSorteosJugados)
                    }
                }

                "LNAC" -> {
                    numeroLNAC = info["N"]?.jsonPrimitive?.content ?: ""
                }
            }
            boleto = Boleto(
                id = info["A"]?.jsonPrimitive?.content?.slice(0..10)?.toLong() ?: 0L,
                gameID = gameId,
                fecha = fechaParaGuardar(fecha),
                precio = precioBoleto.toString(),
                idSorteo = "idSorteo",
                numSorteo = info["S"]?.jsonPrimitive?.content?.slice(0..2) ?: "",
                apuestaMultiple = false,
                premio = "0.0",
                apertura = "fecha-apertura",
                cierre = "fecha-cierre",
                tipo = tipo,
                combinaciones = columnas,
                joker = joker,
                reintegro = reintegro,
                numeroClave = numeroClaveElGordo,
                dreams = dreams,
                estrellas = estrellasEuromillones,
                numeroElMillon = numeroELMillon,
                numeroLoteria = numeroLNAC
            )
        }

        "BAR" -> {}

    }


    return boleto

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

