package com.example.lottery42.boleto.data.network.getPremioURLS

import com.example.lottery42.boleto.data.database.Boleto

fun urlPremioEMIL(boleto: Boleto): String {
    val idSorteo = boleto.idSorteo
    val combinaciones = boleto.combinaciones
    val modalidad = if (boleto.apuestaMultiple) "multiple" else "simple"
    val estrellas = boleto.estrellas

    val bloques = combinaciones.mapIndexed { index, combinacion ->
        val numeros = combinacion.replace(" ", "y")
        val estrellasFormatted = estrellas[index].replace(" ", "y")
        "bloque${index + 1}=$numeros&estrellas${index + 1}=$estrellasFormatted"
    }

    val url = "https://www.loteriasyapuestas.es/es/resultados/euromillones/comprobar?" +
            "drawId=$idSorteo" +
            "&modalidad=$modalidad" +
            "&" +
            bloques.joinToString("&")

    return url
}

/**
 *
 * Ejemplo consulta apuesta simple
 * https://www.loteriasyapuestas.es/es/resultados/euromillones/comprobar?drawId=1254702089&modalidad=simple&bloque1=08y20y24y01y29&estrellas1=08y09&bloque2=07y11y22y36y44&estrellas2=07y08&bloque3=30y50y14y36y43&estrellas3=01y05&bloque4=03y14y16y04y05&estrellas4=01y10&bloque5=50y03y14y36y44&estrellas5=11y10
 *
 * Ejemplo consulta apuesta multiple
 *  https://www.loteriasyapuestas.es/es/resultados/euromillones/comprobar?drawId=1254702089&modalidad=multiple&bloque1=08y20y24y28y29y32&estrellas1=08y09y10
 *
 */