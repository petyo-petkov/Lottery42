package com.example.lottery42.boleto.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

@SuppressLint("SetJavaScriptEnabled")
suspend fun fetchData(
    context: Context,
    url: String,
    fetchFun: () -> String,
): String = withContext(Dispatchers.Main) {

    suspendCancellableCoroutine { continuation ->
        val webView = WebView(context)
        continuation.invokeOnCancellation {
            webView.post { webView.destroy() }
        }
        webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadsImagesAutomatically = false // Optimización: no cargar imágenes para scraping
                blockNetworkImage = true
            }
            visibility = View.INVISIBLE
            addJavascriptInterface(
                JavaScriptInterface { data ->
                    if (continuation.context.isActive) {
                        continuation.resume(data)
                    }
                    post {
                        destroy()
                    }
                }, "AndroidInterface",
            )

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    evaluateJavascript(fetchFun()) { }
                }

                @Deprecated("Deprecated in Java")
                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    if (continuation.context.isActive) {
                        continuation.resume("Error: $description")
                    }
                    post { destroy() }
                }
            }
            loadUrl(url)
        }
    }
}


fun getPremio(gameID: String): String {
    return """
              (function() {
                    var boton = document.getElementById('qa_comprobador-formulario-botonComprobar-$gameID');
                    if (boton) {
                        boton.click();
                        setTimeout(function() {
                            var premioElement = document.getElementById('qa_comprobador-cantidadPremio-$gameID-1');
                            var premioText = premioElement ? premioElement.innerText : "0.0";
                            AndroidInterface.sendData(premioText);
                        }, 1000);
                    } else {
                        AndroidInterface.sendData("Error Boton");
                    }
              }
              )();
              """
}

fun getInfo(): String {
    return """
              (function() {
              setTimeout(function() {
                 
                 var preContent = document.querySelector('pre')?.innerText
                 AndroidInterface.sendData(preContent);
          
             }, 500); 
             })(); 
           """
}
