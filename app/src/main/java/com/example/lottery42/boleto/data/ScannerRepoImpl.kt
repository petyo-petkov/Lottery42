package com.example.lottery42.boleto.data

import android.util.Log
import com.example.lottery42.boleto.domain.ScannerRepo
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class ScannerRepoImpl(private val scanner: GmsBarcodeScanner) : ScannerRepo {
    override fun startScanning(): Flow<String?> {
        return callbackFlow {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    launch {
                        send(getTypes(barcode))
                    }
                }.addOnFailureListener {
                    Log.e("ScannerRepo", it.message.toString())
                }
            awaitClose { }
        }
    }

    private fun getTypes(barcode: Barcode): String {
        return when (barcode.format) {
            Barcode.FORMAT_QR_CODE -> "QR|${barcode.rawValue}"
            Barcode.FORMAT_CODE_128-> "BAR|${barcode.rawValue}"
            else -> "Unknown"
        }

    }

}