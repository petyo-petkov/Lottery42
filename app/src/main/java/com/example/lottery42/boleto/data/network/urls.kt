package com.example.lottery42.boleto.data.network

import java.time.LocalDate
import java.time.format.DateTimeFormatter


const val GET_PROXIMOS_SORTEOS_TODOS = "https://www.loteriasyapuestas.es/servicios/proximosv3?game_id=TODOS&num=50"

const val GET_ULTIMOS_CELEBRADOS_LNAC = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosLoteriaNacional"
const val GET_ULTIMOS_CELEBRADOS_LAPR = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosPrimitiva"
const val GET_ULTIMOS_CELEBRADOS_EMIL = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosEuromillones"
const val GET_ULTIMOS_CELEBRADOS_ELGR = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosGordoPrimitiva"
const val GET_ULTIMOS_CELEBRADOS_BONO = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosBonoloto"
const val GET_ULTIMOS_CELEBRADOS_EDMS = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosEurodreams"

fun urlUltimosTresMeses(gameId: String) : String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    val ahora = LocalDate.now()
    val antes = ahora.minusMonths(3)

    val fechaInicio = formatter.format(antes)
    val fechaFin = formatter.format(ahora)

    return "https://www.loteriasyapuestas.es/servicios/buscadorSorteos?game_id=$gameId&celebrados=&fechaInicioInclusiva=$fechaInicio&fechaFinInclusiva=$fechaFin"
}

fun urlResultadosPorFechas(gameId: String, fechaInicio: String, fechaFin: String) : String {
    return "https://www.loteriasyapuestas.es/servicios/buscadorSorteos?game_id=$gameId&celebrados=&fechaInicioInclusiva=$fechaInicio&fechaFinInclusiva=$fechaFin"
}

fun urlPremioLNACPorNumero(numeroLoteria: String, idSorteo: String) : String {
    return  "https://www.loteriasyapuestas.es/servicios/premioDecimoWebParaVariosSorteos?decimo=$numeroLoteria&serie=&fraccion=&importeComunEnCentimos&idSorteos=$idSorteo"

}



/*
URL'S

 Loteria Nacional
 const val GET_PROXIMOS_SORTEOS_LNAC = "https://www.loteriasyapuestas.es/servicios/proximosv3?game_id=LNAC&num=50"
 const val GET_ULTIMOS_CELEBRADOS_LNAC = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosLoteriaNacional"

 https://www.loteriasyapuestas.es/servicios/sorteosFuturosPorTipoDeSorteo?game_id=LNAC&tipoSorteo=EX&numeroResultados=103

 La Primitiva
 const val GET_ULTIMOS_CELEBRADOS_LAPR = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosPrimitiva"

 EuroMilliones
 const val GET_ULTIMOS_CELEBRADOS_EMIL = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosEuromillones"

 El Gordo
 const val GET_ULTIMOS_CELEBRADOS_ELGR = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosGordoPrimitiva"

 Bonoloto
 const val GET_ULTIMOS_CELEBRADOS_BONO = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosBonoloto"

 Euro Dreams
 const val GET_ULTIMOS_CELEBRADOS_EDMS = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosEurodreams"


 Ejemplo URL LAPR
 https://www.loteriasyapuestas.es/es/resultados/primitiva/comprobar?drawId=1005204002&modalidad=simple&bloque1=1y2y3y4y5y6&reintegro=4&joker=4224561
 https://www.loteriasyapuestas.es/es/resultados/primitiva/comprobar?drawId=1005204002&modalidad=simple&bloque1=29y19y2y1y5y37&bloque2=1y5y2y10y29y31&bloque3=29y19y37y2y3y20&reintegro=4&joker=4224561
 https://www.loteriasyapuestas.es/es/resultados/primitiva/comprobar?drawId=1005204002&modalidad=multiple&bloque1=2y3y19y21y28y17y30y4y5y6y7&reintegro=4&joker=4224561

 Ejemplo URL BONO
 https://www.loteriasyapuestas.es/es/resultados/bonoloto/comprobar?drawId=937601050&modalidad=multiple&bloque1=4y5y6y23y36y15y27&reintegro=4
 https://www.loteriasyapuestas.es/es/resultados/bonoloto/comprobar?drawId=937601050&modalidad=simple&bloque1=4y5y6y23y36y15&bloque2=1y2y3y22y25y16&reintegro=4


Ejemplo consulta apuesta LNAC
https://www.loteriasyapuestas.es/es/resultados/loteria-nacional/comprobar?decimo=23450
https://www.loteriasyapuestas.es/es/resultados/loteria-nacional/comprobar?decimo=23450&importe=12
https://www.loteriasyapuestas.es/es/resultados/loteria-nacional/comprobar?decimo=23450&importe=12&serie=4&fraccion=9
https://www.loteriasyapuestas.es/es/resultados/loteria-nacional/comprobar?drawId=1026209062&decimo=23450&importe=12&serie=4&fraccion=9


 */