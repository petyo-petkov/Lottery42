package com.example.lottery42.boleto.data.network.getPremioURLS

import com.example.lottery42.boleto.data.database.Boleto

fun urlPremioEDMS(boleto: Boleto): String {
    val idSorteo = boleto.idSorteo
    val combinaciones = boleto.combinaciones
    val dreams = boleto.dreams
    val modalidad = if (boleto.apuestaMultiple) "multiple" else "simple"
    val combinacionesTransformadas = transformListEDMS(combinaciones, dreams)

    return "https://www.loteriasyapuestas.es/es/resultados/eurodreams/comprobar?" +
            "drawId=$idSorteo" +
            "&modalidad=$modalidad" +
            "&$combinacionesTransformadas"


}

private fun transformListEDMS(combinaciones: List<String>, dreams: List<String>): String {
    return combinaciones.mapIndexed { index, combinacion ->
        val bloque = combinacion.split(" ").joinToString("y")
        val numero = dreams[index].trim()
        "bloque${index + 1}=$bloque&numero${index + 1}=$numero"
    }.joinToString("&")

}

/*
* Ejemplo consulta apuesta actual
* https://www.loteriasyapuestas.es/es/resultados/eurodreams/comprobar?drawId=1171214044&modalidad=multiple&bloque1=28y27y9y32y36y11y2y12y3y22&numero1=3
* https://www.loteriasyapuestas.es/es/resultados/eurodreams/comprobar?drawId=1176214043&modalidad=simple&bloque1=28y27y1y2y3y16&numero1=4
* https://www.loteriasyapuestas.es/es/resultados/eurodreams/comprobar?drawId=1176214043&modalidad=simple&bloque1=28y27y1y2y3y16&numero1=4&bloque2=28y27y1y2y5y34&numero2=4&bloque3=28y27y1y2y8y11&numero3=3
*/