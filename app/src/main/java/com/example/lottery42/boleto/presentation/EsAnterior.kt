package com.example.lottery42.boleto.presentation

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun esAnterior(fechaCierre: String?): Boolean {
    if (fechaCierre.isNullOrEmpty()) return false
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formatterCorto = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var esAnterior: Boolean

    if (fechaCierre.length < 13) {
        val fecha = LocalDate.parse(fechaCierre, formatterCorto)
        val hoy = LocalDate.now()
        esAnterior = fecha.isBefore(hoy)

    } else {
        val fecha = LocalDateTime.parse(fechaCierre, formatter)
        val hoy = LocalDateTime.now()
        esAnterior = fecha.isBefore(hoy)
    }

    return esAnterior

}