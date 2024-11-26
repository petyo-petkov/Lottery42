package com.example.lottery42.boleto.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val MESES = mapOf(
    "ENE" to "01", "FEB" to "02", "MAR" to "03", "ABR" to "04",
    "MAY" to "05", "JUN" to "06", "JUL" to "07", "AGO" to "08",
    "SEP" to "09", "OCT" to "10", "NOV" to "11", "DIC" to "12"
)

fun fechaParaGuardar(fechaString: String): String {
    val formatter = DateTimeFormatter.ofPattern("ddMMyy", Locale("es"))
    val fechaEng = fechaString.replace(Regex("[A-Z]{3}")) { MESES[it.value] ?: it.value }
    val fecha = LocalDate.parse(fechaEng, formatter)
    return fecha.toString()
}

fun fechaParaMostrar(fechaString: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es"))
    val fechaLocaldate = LocalDate.parse(fechaString)
    return formatter.format(fechaLocaldate)

}