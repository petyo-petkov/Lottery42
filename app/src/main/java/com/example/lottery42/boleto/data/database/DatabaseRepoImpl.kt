package com.example.lottery42.boleto.data.database

import androidx.compose.ui.graphics.Color
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.lottery42.boleto.domain.DatabaseRepo
import com.example.lottery42.ui.theme.MiRojo
import com.example.lottery42.ui.theme.MiVerde
import com.example.sqldelight.AppDatabase
import com.lottery42.BoletoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.math.round
import kotlin.text.Typography.euro

class DatabaseRepoImpl(
    db: AppDatabase
) : DatabaseRepo {
    private val queries = db.boletoQueries
    private val context: CoroutineContext = Dispatchers.IO

    override suspend fun getBoletoByID(id: Long): Flow<Boleto> {
        return queries.getBoletoById(id)
            .asFlow()
            .map { value ->
                value.executeAsOneOrNull()?.toDomain() ?: Boleto()
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

    override fun getBoletosByDateRange(startDate: String, endDate: String): Flow<List<Boleto>> {
        return queries.getBoletosByDateRange(startDate, endDate)
            .asFlow()
            .mapToList(context)
            .map { value ->
                value.map { it.toDomain() }
            }
    }


    override fun getBalance(): Flow<BalanceState> {
        return getAllBoletos().map { boletos ->
            val ganado = boletos.sumOf { it.premio.toDouble() }
            val gastado = boletos.sumOf { it.precio.toDouble() }
            val balance = ganado - gastado
            BalanceState(
                ganado = "${redondear(ganado)} $euro",
                gastado = "${redondear(gastado)} $euro",
                balance = if (balance < 0) {
                    "${redondear(balance)} $euro"
                } else {
                    "${redondear(balance)} $euro"
                },
                color = if (balance >= 0.0) MiVerde else MiRojo

            )
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

data class BalanceState(
    val ganado: String = "",
    val gastado: String = "",
    val balance: String = "",
    val color: Color = Color.Unspecified
)


// redondea hasta las dos decimas ganado, gastado y balance
private fun redondear(dato: Double): Double {
    return round(dato * 100) / 100
}