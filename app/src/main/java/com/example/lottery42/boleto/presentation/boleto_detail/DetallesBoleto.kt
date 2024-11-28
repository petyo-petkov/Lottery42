package com.example.lottery42.boleto.presentation.boleto_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.lottery42.boleto.data.database.Boleto

@Composable
fun DetallesBoleto(boleto: Boleto?, style: TextStyle) {

    when (boleto?.gameID) {
        "LAPR" -> {
            boleto.combinaciones.forEachIndexed { index, combi ->
                Text(
                    "${index + 1}: $combi",
                    style = style
                )
            }
            Text(
                "Reintegro: ${boleto.reintegro}",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                "Joker: ${boleto.joker}",
                style = MaterialTheme.typography.titleLarge
            )

        }

        "BONO" -> {
            boleto.combinaciones.forEachIndexed { index, combi ->
                Text(
                    "${index + 1}: $combi",
                    style = style
                )
            }
            Text(
                "Reintegro: ${boleto.reintegro}",
                style = MaterialTheme.typography.titleLarge
            )
        }

        "EMIL" -> {
            boleto.combinaciones.zip(boleto.estrellas)
                .withIndex()
                .forEach { (index, pair) ->
                    Text("${index + 1}: ${pair.first} \u2605 ${pair.second}", style = style)
                }
            HorizontalDivider(color = Color.White)
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Numeros El Millon:", style = MaterialTheme.typography.titleLarge)
                boleto.numeroElMillon.forEach {
                    Text(it, style = style)
                }
            }

//            if (!boleto.lluvia.isNullOrEmpty()){
//
//                Text("Lluvia de millones:", style = style)
//                Text(boleto.lluvia, style = style)
//            }

        }

        "ELGR" -> {
            boleto.combinaciones.zip(boleto.numeroClave)
                .withIndex()
                .forEach { (index, pair) ->
                    Text("${index + 1}: ${pair.first} + ${pair.second}", style = style)
                }

        }

        "EDMS" -> {
            boleto.combinaciones.zip(boleto.dreams)
                .withIndex()
                .forEach { (index, pair) ->
                    Text("${index + 1}: ${pair.first} + ${pair.second}", style = style)
                }

        }

        "LNAC" -> {
            Text(
                "${boleto.numeroLoteria}",
                letterSpacing = 6.sp,
                style = style
            )

        }

        else -> {
            Text(text = "")
        }
    }

}
