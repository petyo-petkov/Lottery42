package com.example.lottery42.boleto.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject


//suspend fun main() {
//
//    val url = "https://www.loteriasyapuestas.es/servicios/proximosv3?game_id=TODOS&num=2"
//
//    val cliente = HttpClient(CIO)
//
//    val response: HttpResponse = cliente.get(url)
//    val dataString = response.bodyAsText()
//    val info = Json.decodeFromString<JsonObject>(dataString)
//
//
//    println(dataString)
//
//}




