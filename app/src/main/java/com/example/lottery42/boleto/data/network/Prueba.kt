package com.example.lottery42.boleto.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject


val url =
    "https://www.loteriasyapuestas.es/servicios/buscadorSorteos?game_id=LAPR&celebrados=&fechaInicioInclusiva=20241226&fechaFinInclusiva=20241226"


suspend fun main(){

    val httpClient = HttpClient(CIO)
    val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    val response: HttpResponse = httpClient.get(url)
    val dataString = response.bodyAsText()
    val data = json.decodeFromString<List<JsonObject>>(dataString)

    println(data)

}
