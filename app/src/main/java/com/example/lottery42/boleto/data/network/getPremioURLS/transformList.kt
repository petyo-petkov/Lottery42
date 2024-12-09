package com.example.lottery42.boleto.data.network.getPremioURLS

fun transformList(list: List<String>): String {
    return list.joinToString("&") { bloque ->
        "bloque${list.indexOf(bloque) + 1}=${bloque.trim().replace(" ", "y")}"
    }
}