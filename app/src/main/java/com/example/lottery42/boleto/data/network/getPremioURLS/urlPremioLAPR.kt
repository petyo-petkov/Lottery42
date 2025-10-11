package com.example.lottery42.boleto.data.network.getPremioURLS

import com.example.lottery42.boleto.data.database.Boleto
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments

fun urlPremioLAPR(boleto: Boleto): String {

    val combinacionesParams = transformListToParams(boleto.combinaciones)
    val modalidad = if (boleto.apuestaMultiple) "multiple" else "simple"

    return URLBuilder("https://www.loteriasyapuestas.es").apply {
        appendPathSegments("es", "resultados", "primitiva", "comprobar")
        parameters.append("drawId", boleto.idSorteo)
        parameters.append("modalidad", modalidad)

        combinacionesParams.forEach { (key, value) ->
            parameters.append(key, value)
        }

        parameters.append("reintegro", boleto.reintegro!!)
        if (boleto.joker != "NO") {
            parameters.append("joker", boleto.joker!!)
        }
    }.buildString()
}


//fun urlPremioLAPR(boleto: Boleto): String {
//
//    val idSorteo = boleto.idSorteo
//    val combinaciones = boleto.combinaciones
//    val reintegro = boleto.reintegro
//    val joker = if(boleto.joker == "NO") "" else boleto.joker
//    val modalidad = "simple"
//
//    val combinacionesString = transformList(combinaciones)
//
//    val url = "https://www.loteriasyapuestas.es/es/resultados/primitiva/comprobar?" +
//            "drawId=$idSorteo" +
//            "&modalidad=$modalidad" +
//            "&$combinacionesString" +
//            "&reintegro=$reintegro" +
//            "&joker=$joker"
//
//    return url
//
//}


// LAPR
// "https://www.loteriasyapuestas.es/es/resultados/primitiva/comprobar?drawId=1005204002&modalidad=simple&bloque1=1y2y3y4y5y6&reintegro=4&joker=4224561"
//" https://www.loteriasyapuestas.es/es/resultados/primitiva/comprobar?drawId=1005204002&modalidad=simple&bloque1=29y19y2y1y5y37&bloque2=1y5y2y10y29y31&bloque3=29y19y37y2y3y20&reintegro=4&joker=4224561"
// "https://www.loteriasyapuestas.es/es/resultados/primitiva/comprobar?drawId=1005204002&modalidad=multiple&bloque1=2y3y19y21y28y17y30y4y5y6y7&reintegro=4&joker=4224561"