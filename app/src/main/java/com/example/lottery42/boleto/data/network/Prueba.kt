package com.example.lottery42.boleto.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

suspend fun get(url: String): List<JsonObject> {

    val client = HttpClient(CIO)
    val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    val response: HttpResponse = client.get(url)
    val dataString = response.bodyAsText()
    return json.decodeFromString(dataString)


}

//suspend fun main(){
//    val url = "https://www.loteriasyapuestas.es/servicios/buscadorSorteos?game_id=LNAC&celebrados=&fechaInicioInclusiva=20241109&fechaFinInclusiva=20241109"
//
//    val response = get(url)
//    println(response)
//
//}


//fun main() {
//    val combinaciones = listOf("06 15 19 33 41", "06 15 37 48 53")
//    val numeroClave = listOf("00", "08")
//
//    println(transformListELGR(combinaciones, numeroClave))
//}
//
//private fun transformListELGR(combinaciones: List<String>, numeroClave: List<String>): String {
//    return combinaciones.mapIndexed { index, combinacion ->
//        val bloque = combinacion.split(" ").joinToString("y")
//        val reintegro = numeroClave[index]
//        "bloque${index + 1}=$bloque&reintegro${index + 1}=$reintegro"
//    }.joinToString("&")
//
//}

