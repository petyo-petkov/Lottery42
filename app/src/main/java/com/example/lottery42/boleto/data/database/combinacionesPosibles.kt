package com.example.lottery42.boleto.data.database

fun combinacionesPosibles(apuestasJugadas: Int, apuestasDelSorteo: Int): Int {
    if (apuestasJugadas < apuestasDelSorteo || apuestasDelSorteo < 0) {
        return 0 // No hay combinaciones válidas si k es mayor que n o negativo
    }

    fun factorial(num: Int): Long {
        var result = 1L
        for (i in 2..num) {
            result *= i
        }
        return result
    }

    val nFactorial = factorial(apuestasJugadas)
    val kFactorial = factorial(apuestasDelSorteo)
    val nkFactorial = factorial(apuestasJugadas - apuestasDelSorteo)

    return (nFactorial / (kFactorial * nkFactorial)).toInt()
}