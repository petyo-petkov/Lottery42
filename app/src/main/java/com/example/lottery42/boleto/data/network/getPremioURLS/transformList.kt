package com.example.lottery42.boleto.data.network.getPremioURLS

//fun transformList(list: List<String>): String {
//    return list.joinToString("&") { bloque ->
//        "bloque${list.indexOf(bloque) + 1}=${bloque.trim().replace(" ", "y")}"
//    }
//}


fun transformListToParams(combinaciones: List<String>): Map<String, String> {
    return combinaciones.mapIndexed { index, combinacion ->
        val key = "bloque${index + 1}"
        val value = combinacion.trim().replace(" ", "y")
        key to value
    }.toMap()
}