package com.example.lottery42.boleto.data.network.getPremioURLS

import com.example.lottery42.boleto.data.database.Boleto

fun urlPremioLNAC(boleto: Boleto): String {
    return "https://www.loteriasyapuestas.es/es/resultados/loteria-nacional/comprobar?drawId=${boleto.idSorteo}&decimo=${boleto.numeroLoteria}&importe=&serie=&fraccion="
}