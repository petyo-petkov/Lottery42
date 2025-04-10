package com.example.lottery42.boleto.domain

import com.example.lottery42.boleto.data.database.BalanceState
import com.example.lottery42.boleto.data.database.Boleto
import com.lottery42.BoletoEntity
import kotlinx.coroutines.flow.Flow

interface DatabaseRepo {

    suspend fun getBoletoByID(id: Long): Flow<Boleto>

    fun getAllBoletos(): Flow<List<Boleto>>

    fun getBoletosByDateRange(startDate: String, endDate: String): Flow<List<Boleto>>

    fun getBalance(): Flow<BalanceState>

    suspend fun insertBoleto(boleto: BoletoEntity)

    suspend fun updateBoleto(boleto: BoletoEntity)

    suspend fun deleteBoletoById(id: Long)

    suspend fun deleteAllBoletos()

}