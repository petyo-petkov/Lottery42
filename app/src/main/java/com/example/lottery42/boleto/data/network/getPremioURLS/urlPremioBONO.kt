package com.example.lottery42.boleto.data.network.getPremioURLS

import com.example.lottery42.boleto.data.database.Boleto

fun urlPremioBONO(boleto: Boleto): String {

    val idSorteo = boleto.idSorteo
    val combinaciones = boleto.combinaciones
    val reintegro = boleto.reintegro
    val modalidad = "simple"


    val combinacionesString = transformList(combinaciones)
    val url = "https://www.loteriasyapuestas.es/es/resultados/bonoloto/comprobar?" +
            "drawId=$idSorteo" +
            "&modalidad=$modalidad" +
            "&$combinacionesString" +
            "&reintegro=$reintegro"

    return url
}




// BONO
// https://www.loteriasyapuestas.es/es/resultados/bonoloto/comprobar?drawId=937601050&modalidad=simple&bloque1=4y5y6y23y36y15&bloque2=1y2y3y22y25y16&reintegro=4
// "https://www.loteriasyapuestas.es/es/resultados/bonoloto/comprobar?drawId=1254801311&modalidad=simple&bloque1=5y7y16y23y28y44&bloque2=2y6y12y19y27y44&bloque3=3y7y25y31y41y49&reintegro=4"