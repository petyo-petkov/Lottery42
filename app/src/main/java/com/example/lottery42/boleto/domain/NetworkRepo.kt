package com.example.lottery42.boleto.domain

import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.network.InfoSorteo
import com.example.lottery42.boleto.data.network.models.LotteryModel
import kotlinx.coroutines.flow.Flow

interface NetworkRepo {


    suspend fun getInfoSorteo(numSorteo: String, gameId: String): InfoSorteo

    suspend fun fetchExtraInfo(boleto: Boleto): List<LotteryModel>

    suspend fun getPremios(boleto: Boleto): Flow<String>

    suspend fun getPremioLNAC(boleto: Boleto): String
}