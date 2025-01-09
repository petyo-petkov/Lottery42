package com.example.lottery42.boleto.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("SetJavaScriptEnabled")
suspend fun FetchInfoSorteo(
    context: Context,
    url: String
): String {

    val deferredResult = CompletableDeferred<String>()

    withContext(Dispatchers.Main) {
        val webView = WebView(context).apply {
            settings.javaScriptEnabled = true
            visibility = View.INVISIBLE
            addJavascriptInterface(
                JavaScriptInterface { data ->
                    if (!deferredResult.isCompleted) {
                        deferredResult.complete(data) // Complete with the result
                    }
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
                    """
                       (function() {
                       setTimeout(function() {
                       var preContent = document.querySelector('pre')?.innerText
                       AndroidInterface.sendData(preContent);
                       }, 500); 
                       })(); 
                   """
                ) { }
            }
        }
    }

    return deferredResult.await() // Wait for the result and return it
}
