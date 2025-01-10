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
