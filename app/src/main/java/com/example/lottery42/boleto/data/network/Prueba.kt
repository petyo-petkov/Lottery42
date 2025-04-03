package com.example.lottery42.boleto.data.network


import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun extraerDatosConKtor(url: String): Flow<String> = flow {
    val client = HttpClient(CIO) {  // Usamos el engine CIO
        install(ContentNegotiation) {
            json()
        }
    }

    try {
        val response: HttpResponse = client.get(url) {
            headers {
                // Puedes agregar headers si es necesario, ej: para simular un navegador
                append(
                    HttpHeaders.UserAgent,
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                )
            }
        }

        if (response.status.isSuccess()) {
            val htmlContent: String = response.bodyAsText()
            val doc: Document = Jsoup.parse(htmlContent)

            val infoElement = doc.getElementById("qa_ultResult-EMIL")
            val moreInfoElement = doc.getElementById("more-info-EMIL")

            val infoText = infoElement?.text() ?: "no hay texto"
            val moreInfoText = moreInfoElement?.text() ?: "no hay texto"

            val resultado = listOf(infoText, moreInfoText).joinToString("'")

            emit(resultado)
        } else {
            emit("Error en la solicitud: ${response.status}")  // Manejo de errores
        }
    } catch (e: Exception) {
        emit("Error: ${e.message}")  // Manejo de errores generales
    } finally {
        client.close()
    }
}

fun main() = runBlocking {
    val url =
        "https://www.loteriasyapuestas.es/es/resultados/euromillones/comprobar?drawId=1254702089&modalidad=simple&bloque1=08y20y24y01y29&estrellas1=08y09&bloque2=07y11y22y36y44&estrellas2=07y08&bloque3=30y50y14y36y43&estrellas3=01y05&bloque4=03y14y16y04y05&estrellas4=01y10&bloque5=50y03y14y36y44&estrellas5=11y10\n"
    extraerDatosConKtor(url).collect { resultado ->
        println("Datos extraídos: $resultado")
    }
}