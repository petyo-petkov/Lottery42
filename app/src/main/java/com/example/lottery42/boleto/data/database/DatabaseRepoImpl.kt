package com.example.lottery42.boleto.data.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.lottery42.boleto.domain.DatabaseRepo
import com.example.sqldelight.AppDatabase
import com.lottery42.BoletoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class DatabaseRepoImpl(
    db: AppDatabase
) : DatabaseRepo {
    private val queries = db.boletoQueries
    private val context: CoroutineContext = Dispatchers.IO

    override suspend fun getBoletoByID(id: Long): BoletoEntity {
        return withContext(Dispatchers.IO) {
            queries.getBoletoById(id).executeAsOne()
        }
    }

    override fun getAllBoletos(): Flow<List<Boleto>> {
        return queries.getAllBoletos()
            .asFlow()
            .mapToList(context)
            .map { value ->
                value.map { it.toDomain() }
            }
    }

    override suspend fun insertBoleto(boleto: BoletoEntity) =
        withContext(Dispatchers.IO) {
            queries.insertBoleto(boleto)
        }

    override suspend fun updateBoleto(boleto: BoletoEntity) =
        withContext(Dispatchers.IO) {
            queries.updateBoleto(boleto.premio, boleto.id)
        }



    override suspend fun deleteBoletoById(id: Long) {
        return withContext(Dispatchers.IO) {
            queries.deleteBoletoById(id)
        }
    }

    override suspend fun deleteAllBoletos() {
        return withContext(Dispatchers.IO) {
            queries.deleteAllBoletos()

        }
    }

}