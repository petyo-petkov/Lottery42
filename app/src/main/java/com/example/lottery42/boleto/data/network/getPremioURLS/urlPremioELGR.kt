package com.example.lottery42.boleto.data.network.getPremioURLS

import com.example.lottery42.boleto.data.database.Boleto


fun urlPremioELGR(boleto: Boleto): String {

    val idSorteo = boleto.idSorteo
    val reintegro = boleto.numeroClave
    val combinaciones = boleto.combinaciones
    val combinacionesTransformadas = transformListELGR(combinaciones, reintegro)
    val modalidad = if (boleto.apuestaMultiple) "multiple" else "simple"

    return "https://www.loteriasyapuestas.es/es/resultados/gordo-primitiva/comprobar?" +
            "drawId=$idSorteo" +
            "&modalidad=$modalidad" +
            "&$combinacionesTransformadas"

}

private fun transformListELGR(combinaciones: List<String>, numeroClave: List<String>): String {
    return combinaciones.mapIndexed { index, combinacion ->
        val bloque = combinacion.split(" ").joinToString("y")
        val reintegro = numeroClave[index].trim()
        "bloque${index + 1}=$bloque&reintegro${index + 1}=$reintegro"
    }.joinToString("&")

}


/*
"https://www.loteriasyapuestas.es/es/resultados/gordo-primitiva/comprobar?drawId=940205013&modalidad=multiple&bloque1=28y27y9y32y53y11y12&reintegro1=4"
"https://www.loteriasyapuestas.es/es/resultados/gordo-primitiva/comprobar?drawId=940205013&modalidad=simple&bloque1=28y27y1y2y3&reintegro1=4"
"https://www.loteriasyapuestas.es/es/resultados/gordo-primitiva/comprobar?drawId=940205013&modalidad=simple&bloque1=28y27y1y2y3&reintegro1=4&bloque2=28y27y1y2y5&reintegro2=4&bloque3=28y27y1y2y8&reintegro3=3"

 Ejemplo consulta apuesta anterior a la 3.12.0.
 * <HOST><URL>?drawId=940205013&modalidad=multiple&bloque1=28y27y9y32y53y11y12&reintegro=4&quality=true
 * <HOST><URL>?drawId=940205013&modalidad=simple&bloque1=28y27y1y2y3&reintegro=4&quality=true
 * <HOST><URL>?drawId=940205013&modalidad=simple&bloque1=28y27y1y2y3&bloque2=28y27y1y2y5&bloque3=28y27y1y2y8&reintegro=4&quality=true
 */