package com.example.lottery42.boleto.data

import com.example.lottery42.boleto.data.crearBoleto.crearBoletoFromBarcode
import com.example.lottery42.boleto.data.crearBoleto.crearBoletoFromQR
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.domain.NetworkRepo

suspend fun crearBoleto(data: String, networkRepo: NetworkRepo): Boleto {
    return when {
        data.startsWith("QR") -> crearBoletoFromQR(data, networkRepo)
        data.startsWith("BAR") -> crearBoletoFromBarcode(data, networkRepo)
        else -> {
            throw IllegalArgumentException("Barcode format desconocido: $data")

        }
    }
}





