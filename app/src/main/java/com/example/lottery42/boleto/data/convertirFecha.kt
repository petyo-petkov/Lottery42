package com.example.lottery42.boleto.data

private val MESES = mapOf(
    "ENE" to " Enero ", "FEB" to " Febrero ", "MAR" to " Marzo ", "ABR" to " Abríl ",
    "MAY" to " Mayo ", "JUN" to " Junio ", "JUL" to " Julio ", "AGO" to " Agosto ",
    "SEP" to " Septiembre ", "OCT" to " Octubre ", "NOV" to " Noviembre ", "DIC" to " Diciembre "
)
fun fechaConvertida(fechaString: String): String {
    return fechaString.replace(Regex("[A-Z]{3}")) { MESES[it.value] ?: it.value }

}
