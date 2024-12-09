package com.example.lottery42.boleto.data.network

import android.webkit.JavascriptInterface

class JavaScriptInterface(private val onDataReceived: (String) -> Unit) {
    @JavascriptInterface
    fun sendData(data: String) {
        onDataReceived(data)
    }
}