package com.example.lottery42.boleto.data.network

import com.example.lottery42.boleto.data.network.models.LotteryModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

suspend fun get(url: String): List<LotteryModel> {

    val client = HttpClient(CIO)
    val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    val response: HttpResponse = client.get(url)
    val dataString = response.bodyAsText()
    return json.decodeFromString(dataString)


}

suspend fun main(){
    val url = "https://www.loteriasyapuestas.es/servicios/buscadorSorteos?game_id=EDMS&celebrados=&fechaInicioInclusiva=20241010&fechaFinInclusiva=20241010"

    val response = get(url)
    println(response)

}