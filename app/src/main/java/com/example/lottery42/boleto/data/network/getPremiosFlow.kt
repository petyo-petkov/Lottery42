package com.example.lottery42.boleto.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext


@SuppressLint("SetJavaScriptEnabled")
fun getPremiosFlow(
    context: Context,
    url: String,
    gameID: String,

): Flow<String> = callbackFlow {

    withContext(Dispatchers.Main) {
        val webView = WebView(context).apply {
            settings.javaScriptEnabled = true
            visibility = View.INVISIBLE
            addJavascriptInterface(
                JavaScriptInterface { data ->
                    trySend(data) // Emite el premio en el flujo
                    Log.i("data", data)
                    post {
                        destroy()
                        close()
                    }
                }, "AndroidInterface"
            )
            loadUrl(url)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                webView.evaluateJavascript(
                    """
               (function() {
                    var boton = document.getElementById('qa_comprobador-formulario-botonComprobar-$gameID');
                    if (boton) {
                        boton.click();
                        setTimeout(function() {
                            var premioElement = document.getElementById('qa_comprobador-cantidadPremio-$gameID-1');
                            var premioText = premioElement ? premioElement.innerText : "0.0";
                            AndroidInterface.sendData(premioText);
                        }, 500);
                    } else {
                        AndroidInterface.sendData("Botón no encontrado");
                    }
                })();
                """
                ) { }
            }

        }

    }
    awaitClose()
}

fun fetchJson(): String {
   return          """
                       (function() {
                       setTimeout(function() {
                       var preContent = document.querySelector('pre')?.innerText
                       AndroidInterface.sendData(preContent);
                       }, 1000); 
                       })(); 
                   """
}

fun fetchPremio(gameID: String): String {
    return when(gameID) {
        "LNAC" -> {
            """
               (function() {
                    var boton = document.getElementById('qa_comprobador-formulario-botonComprobar-LNAC');
                    if (boton) {
                        boton.click();
                        setTimeout(function() {
                            var premioElement = document.getElementById('qa_comprobador-cantidadPremio-LNAC-0');
                            var premioText = premioElement ? premioElement.innerText : "0.0";
                            AndroidInterface.sendData(premioText);
                        }, 5000);
                    } else {
                        AndroidInterface.sendData("Botón no encontrado");
                    }
                })();
                """
        }
        else -> {
            """
               (function() {
                    var boton = document.getElementById('qa_comprobador-formulario-botonComprobar-$gameID');
                    if (boton) {
                        boton.click();
                        setTimeout(function() {

                            var premioElement = document.getElementById('qa_comprobador-cantidadPremio-$gameID-1');
                            var premioText = premioElement ? premioElement.innerText : "0.0";
                            AndroidInterface.sendData(premioText);
                        }, 5000);
                    } else {
                        AndroidInterface.sendData("Botón no encontrado");
                    }
                })();
                """
        }


    }
}
