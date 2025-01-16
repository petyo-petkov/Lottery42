package com.example.lottery42.boleto.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@SuppressLint("SetJavaScriptEnabled")
suspend fun fetchData(
    context: Context,
    url: String,
    fetchFun: () -> String,
): String = withContext(Dispatchers.Main) {

    suspendCoroutine { continuation ->
       val webView = WebView(context).apply {
            settings.javaScriptEnabled = true
            visibility = View.INVISIBLE
            addJavascriptInterface(
                JavaScriptInterface { data ->
                    continuation.resume(data) // Resume the coroutine with the result
                    post {
                        destroy()
                    }
                }, "AndroidInterface"
            )
            loadUrl(url)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                webView.evaluateJavascript(
                    fetchFun()
                ) { }
            }
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
