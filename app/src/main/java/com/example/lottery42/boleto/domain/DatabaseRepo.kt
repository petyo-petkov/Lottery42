package com.example.lottery42.boleto.domain

import com.example.lottery42.boleto.data.database.Boleto
import com.lottery42.BoletoEntity
import kotlinx.coroutines.flow.Flow

interface DatabaseRepo {

    suspend fun getBoletoByID(id: Long): BoletoEntity

    fun getAllBoletos(): Flow<List<Boleto>>

    suspend fun insertBoleto(boleto: BoletoEntity)

    suspend fun updateBoleto(boleto: BoletoEntity)

    suspend fun deleteBoletoById(id: Long)

    suspend fun deleteAllBoletos()

}