package com.example.lottery42.boleto.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.io.IOException

suspend fun getInfoFromURL2(url: String): List<JsonObject> {

    val httpClient = HttpClient()


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
    }catch (e: Exception) {
        Log.e("Error getInfoFromURL $url", e.message.toString())
        return emptyList()
    }

}

suspend fun main() {
    val gameID = "EMIL"
    val numSorteo = "101"
    val url = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosPrimitiva"
    val info = getInfoFromURL2(url)



    println(info)

}