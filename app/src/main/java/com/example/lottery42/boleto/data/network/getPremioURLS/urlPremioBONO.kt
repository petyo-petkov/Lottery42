package com.example.lottery42.boleto.data.network.getPremioURLS

import com.example.lottery42.boleto.data.database.Boleto
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments

fun urlPremioBONO(boleto: Boleto): String {

    val combinacionesParams = transformListToParams(boleto.combinaciones)
    val modalidad = if (boleto.apuestaMultiple) "multiple" else "simple"

    return URLBuilder("https://www.loteriasyapuestas.es").apply {
        appendPathSegments("es", "resultados", "bonoloto", "comprobar")
        parameters.append("drawId", boleto.idSorteo)
        parameters.append("modalidad", modalidad)

        combinacionesParams.forEach { (key, value) ->
            parameters.append(key, value)
        }
        parameters.append("reintegro", boleto.reintegro!!)

    }.buildString()

}


// BONO
// https://www.loteriasyapuestas.es/es/resultados/bonoloto/comprobar?drawId=937601050&modalidad=simple&bloque1=4y5y6y23y36y15&bloque2=1y2y3y22y25y16&reintegro=4
// "https://www.loteriasyapuestas.es/es/resultados/bonoloto/comprobar?drawId=1254801311&modalidad=simple&bloque1=5y7y16y23y28y44&bloque2=2y6y12y19y27y44&bloque3=3y7y25y31y41y49&reintegro=4"