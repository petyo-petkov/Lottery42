package com.example.lottery42.boleto.data.network

import android.content.Context
import android.util.Log
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.network.getPremioURLS.urlPremioBONO
import com.example.lottery42.boleto.data.network.getPremioURLS.urlPremioEDMS
import com.example.lottery42.boleto.data.network.getPremioURLS.urlPremioELGR
import com.example.lottery42.boleto.data.network.getPremioURLS.urlPremioEMIL
import com.example.lottery42.boleto.data.network.getPremioURLS.urlPremioLAPR
import com.example.lottery42.boleto.data.network.getPremioURLS.urlPremioLNAC
import com.example.lottery42.boleto.data.network.models.LotteryModel
import com.example.lottery42.boleto.data.network.models.loteriaNacional.ResultadoPorNumeroLoteria
import com.example.lottery42.boleto.data.network.models.loteriaNacional.resultadoLNAC.ResultadosLoteriaNacional
import com.example.lottery42.boleto.domain.NetworkRepo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.io.IOException
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

class NetworkRepoImpl(private val context: Context) : NetworkRepo {

    private suspend inline fun <reified T> getInfoFromURL(url: String): List<T> {
        val json = Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
            isLenient = true
            allowSpecialFloatingPointValues = true
            prettyPrint = true
            useArrayPolymorphism = true
            allowStructuredMapKeys = true
        }
        try {
            val jsonString = fetchData(context, url, ::getInfo)
            Log.i("JSON", jsonString)
            return json.decodeFromString(jsonString)

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

    // Para Crear el boleto
    override suspend fun getInfoSorteo(numSorteo: String, gameId: String): InfoSorteo? {

        val urlProximos = GET_PROXIMOS_SORTEOS_TODOS

        val urlUltimos = when (gameId) {
            "LAPR" -> GET_ULTIMOS_CELEBRADOS_LAPR
            "BONO" -> GET_ULTIMOS_CELEBRADOS_BONO
            "EMIL" -> GET_ULTIMOS_CELEBRADOS_EMIL
            "ELGR" -> GET_ULTIMOS_CELEBRADOS_ELGR
            "LNAC" -> GET_ULTIMOS_CELEBRADOS_LNAC
            "EDMS" -> GET_ULTIMOS_CELEBRADOS_EDMS
            else -> ""
        }
        val urlUltimosTresMeses = urlUltimosTresMeses(gameId)

        Log.i("URL", urlUltimos)
        return try {
            coroutineScope {
                val (proximos, ultimos) = awaitAll(
                    async { findSorteo(urlProximos, gameId, numSorteo) },
                    async { findSorteo(urlUltimos, gameId, numSorteo) },
                )
                Log.i("proximos", proximos.toString())
                Log.i("ultimos", ultimos.toString())

                val jsonObject = proximos ?: ultimos ?: async {
                    findSorteo(
                        urlUltimosTresMeses,
                        gameId,
                        numSorteo
                    )
                }.await()

                getMissingInfoL(jsonObject!!)
            }

        } catch (e: Exception) {
            Log.e("ERROR getInfoSorteo", e.message.toString())
            InfoSorteo()
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
        } catch (e: Exception) {
            Log.e("ERROR fetchExtraInfoLNAC", e.message.toString())
            emptyList()
        }
    }

    override suspend fun getPremios(boleto: Boleto): String {
        val gameId = boleto.gameID
        val url = when (gameId) {
            "LAPR" -> urlPremioLAPR(boleto)
            "BONO" -> urlPremioBONO(boleto)
            "EMIL" -> urlPremioEMIL(boleto)
            "ELGR" -> urlPremioELGR(boleto)
            "EDMS" -> urlPremioEDMS(boleto)
            "LNAC" -> urlPremioLNAC(boleto)
            else -> ""
        }
        Log.i("URL", url)
        val premio = fetchData(context, url) { getPremio(gameId) }
        return premio

    }

    override suspend fun getPremioLNAC(boleto: Boleto): String {
        val urlLNAC = urlPremioLNACPorNumero(boleto.numeroLoteria!!, boleto.idSorteo)
        return (getInfoFromURL<ResultadoPorNumeroLoteria>(urlLNAC)[0]
            .premioEnCentimos / 100).toDouble().toString()


    }

    private suspend fun findSorteo(url: String, gameID: String, numSorteo: String): JsonObject? {
        return getInfoFromURL<JsonObject>(url)
            .find {
                it.get("id_sorteo").toString().substring(8..10) == numSorteo && it.get("game_id")
                    .toString().slice(1..4) == gameID
            }
    }

    private fun getMissingInfoL(info: JsonObject): InfoSorteo {
        fun JsonObject.getString(key: String) = this[key]?.jsonPrimitive?.content
        return InfoSorteo(
            fecha = info.getString("fecha") ?: info.getString("fecha_sorteo") ?: "",
            precio = info.getString("precio") ?: info.getString("precioDecimo") ?: "0.0",
            idSorteo = info.getString("id_sorteo") ?: "",
            apertura = info.getString("apertura") ?: info.getString("fecha_sorteo") ?: "",
            cierre = info.getString("cierre") ?: info.getString("fecha_sorteo") ?: ""
        )

    }

    private fun urlExtraInfo(boleto: Boleto): String {
        val gameId = boleto.gameID
        val fecha = boleto.fecha.replace("-", "")
        return urlResultadosPorFechas(gameId, fecha, fecha)
    }
}


@Serializable
data class InfoSorteo(
    val precio: String? = null,
    val idSorteo: String? = "",
    val fecha: String? = "",
    val apertura: String? = "",
    val cierre: String? = ""
)


