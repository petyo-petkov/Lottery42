package com.example.lottery42.boleto.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.InternalAPI
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

suspend fun getInfoFromURL2(url: String): List<JsonObject> {

    val httpClient = HttpClient(CIO)
    val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    val response: HttpResponse = httpClient.get(url)
    val dataString = response.bodyAsText()
    return json.decodeFromString(dataString)

}

private suspend fun findSorteo2(url: String, gameID: String, numSorteo: String): JsonObject? {
    return getInfoFromURL2(url)
        .find {
            it.get("id_sorteo").toString().substring(8..10) == numSorteo && it.get("game_id")
                .toString().slice(1..4) == gameID
        }
}

@OptIn(InternalAPI::class)
suspend fun main() {
    val gameID = "EMIL"
    val numSorteo = "101"
    val url = "https://www.loteriasyapuestas.es/servicios/buscadorUltimosSorteosCelebradosPrimitiva"

    val httpClient = HttpClient(CIO)
    val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    val response: HttpResponse = httpClient.get(url)
    val dataString = response.bodyAsText()
    val string =  json.decodeFromString<JsonObject>(dataString)

    println(string)
}