package com.example.lottery42.boleto.data.network

import android.content.Context
import android.util.Log
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.network.getPremioURLS.urlPremioBONO
import com.example.lottery42.boleto.data.network.getPremioURLS.urlPremioEDMS
import com.example.lottery42.boleto.data.network.getPremioURLS.urlPremioELGR
import com.example.lottery42.boleto.data.network.getPremioURLS.urlPremioEMIL
import com.example.lottery42.boleto.data.network.getPremioURLS.urlPremioLAPR
import com.example.lottery42.boleto.data.network.models.LotteryModel
import com.example.lottery42.boleto.data.network.models.loteriaNacional.ResultadoPorNumeroLoteria
import com.example.lottery42.boleto.data.network.models.loteriaNacional.resultadoLNAC.ResultadosLoteriaNacional
import com.example.lottery42.boleto.domain.NetworkRepo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NetworkRepoImpl(
    val httpClient: HttpClient,
    val context: Context
) : NetworkRepo {

    private suspend inline fun <reified T> getInfoFromURL(url: String): List<T> {
        val json = Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
        try {
            val response: HttpResponse = httpClient.get(url)
            val dataString = response.bodyAsText()
            return json.decodeFromString(dataString)
        } catch (e: IOException) {
            Log.e("NetworkError getInfoFromURL $url", e.message.toString())
            return emptyList()
        } catch (e: SerializationException) {
            Log.e("JSONError getInfoFromURL $url", e.message.toString())
            return emptyList()
        } catch (e: Exception) {
            Log.e("Error getInfoFromURL $url", e.message.toString())
            return emptyList()
        }
    }

    private suspend fun findSorteo(url: String, gameID: String, numSorteo: String): JsonObject? {
        return getInfoFromURL<JsonObject>(url)
            .find {
                it.get("id_sorteo").toString().substring(8..10) == numSorteo && it.get("game_id")
                    .toString().slice(1..4) == gameID
            }
    }

    // Para Crear el boleto
    override suspend fun getInfoSorteo(numSorteo: String, gameId: String): InfoSorteo? {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val fechaFin = LocalDate.now()
        val fechaInicio = fechaFin.minusMonths(3)

        val urlProximos = GET_PROXIMOS_SORTEOS_TODOS
        val urlUltimos = urlResultadosPorFechas(
            gameId = gameId,
            fechaInicio = fechaInicio.format(formatter),
            fechaFin = fechaFin.format(formatter)
        )
        Log.i("urlUltimos", urlUltimos)
        Log.i("urlProximos", urlProximos)

        return try {
            coroutineScope {
                val (proximos, ultimos) = awaitAll(
                    async { findSorteo(urlProximos, gameId, numSorteo) },
                    async { findSorteo(urlUltimos, gameId, numSorteo) }
                )
                Log.i("proximos", proximos.toString())
                Log.i("ultimos", ultimos.toString())

                crearInfoSorteo(proximos, ultimos)
            }

        } catch (e: Exception) {
            Log.e("ERROR getInfoSorteo", e.message.toString())
            throw e
        }
    }


    // Para extra info
    override suspend fun fetchExtraInfo(boleto: Boleto): List<LotteryModel> {
        return try {
            val response = getInfoFromURL<LotteryModel>(urlExtraInfo(boleto))
            response
        } catch (e: Exception) {
            Log.e("ERROR fetchExtraInfo", e.message.toString())
            emptyList()
        }

    }

    override suspend fun fetchExtraInfoLNAC(boleto: Boleto): List<ResultadosLoteriaNacional> {
        return try {
            val response = getInfoFromURL<ResultadosLoteriaNacional>(urlExtraInfo(boleto))
            response
        }catch (e: Exception) {
            Log.e("ERROR fetchExtraInfoLNAC", e.message.toString())
            emptyList()
        }
    }

    override suspend fun getPremios(boleto: Boleto): Flow<String> {
        val gameId = boleto.gameID
        val url = when (gameId) {
            "LAPR" -> urlPremioLAPR(boleto)
            "BONO" -> urlPremioBONO(boleto)
            "EMIL" -> urlPremioEMIL(boleto)
            "ELGR" -> urlPremioELGR(boleto)
            "EDMS" -> urlPremioEDMS(boleto)
            else -> ""
        }
        return getPremiosFlow(context, url, gameId)
    }

    override suspend fun getPremioLNAC(boleto: Boleto): String {
        val urlLNAC = urlPremioLNACPorNumero(boleto.numeroLoteria!!, boleto.idSorteo)
        return (getInfoFromURL<ResultadoPorNumeroLoteria>(urlLNAC)[0]
            .premioEnCentimos / 100).toDouble().toString()


    }

}

@Serializable
data class InfoSorteo(
    val precio: String?,
    val idSorteo: String,
    val fecha: String,
    val apertura: String,
    val cierre: String
)

private fun crearInfoSorteo(proximos: JsonObject?, ultimos: JsonObject?): InfoSorteo? {
    fun JsonObject?.getString(key: String) = this?.get(key)?.jsonPrimitive?.content

    return when {
        proximos != null -> InfoSorteo(
            fecha = proximos.getString("fecha") ?: "error fecha proximos",
            idSorteo = proximos.getString("id_sorteo") ?: "error ID proximos",
            apertura = proximos.getString("apertura") ?: "error apertura priximos",
            cierre = proximos.getString("cierre") ?: "error cierre proximos",
            precio = proximos.getString("precio") ?: "error precio proximos"
        )

        ultimos != null -> InfoSorteo(
            fecha = ultimos.getString("fecha_sorteo") ?: "error fecha ultimos",
            idSorteo = ultimos.getString("id_sorteo") ?: "error ID ultimos",
            apertura = ultimos.getString("apertura") ?: ultimos.getString("fecha_sorteo")
            ?: "error apertura ultimos",
            cierre = ultimos.getString("cierre") ?: ultimos.getString("fecha_sorteo")
            ?: "error cierre ultimos",
            precio = ultimos.getString("precioDecimo") ?: "error precio ultimos"
        )
        //else -> throw IllegalArgumentException("Ambos objetos JSON son nulos")
        else -> null
    }

}

private fun urlExtraInfo(boleto: Boleto): String {
    val gameId = boleto.gameID
    val fecha = boleto.fecha.replace("-", "")
    return urlResultadosPorFechas(gameId, fecha, fecha)
}


