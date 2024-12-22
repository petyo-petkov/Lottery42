package com.example.lottery42.boleto.presentation

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun esAnterior(fechaCierre: String?): Boolean {
    if (fechaCierre.isNullOrEmpty()) return false
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val fechaCierreParseada = LocalDateTime.parse(fechaCierre, formatter)
    val hoy = LocalDateTime.now()
    return fechaCierreParseada.plusHours(1).isBefore(hoy)
}