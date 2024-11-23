package com.example.lottery42.boleto.domain

import kotlinx.coroutines.flow.Flow

interface ScannerRepo {
    fun startScanning(): Flow<String?>
}