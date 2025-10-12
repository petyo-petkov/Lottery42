package com.example.lottery42.boleto.presentation.extra_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.network.models.Escrutinio
import com.example.lottery42.boleto.data.network.models.EscrutinioJoker
import com.example.lottery42.boleto.data.network.models.Lluvia
import com.example.lottery42.boleto.data.network.models.LotteryModel
import com.example.lottery42.boleto.data.toFormattedDate
import com.example.lottery42.boleto.presentation.Divisor
import com.example.lottery42.boleto.presentation.boleto_detail.DetallesBoleto
import kotlin.text.Typography.euro

@Composable
fun ExtraInfo(
    resultado: LotteryModel,
    boleto: Boleto
) {

    val style = MaterialTheme.typography.titleMedium

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = boleto.tipo, style = MaterialTheme.typography.displaySmall)

        Divisor()

        Text("Fecha:", style = style)

        Text(
            text = boleto.fecha.toFormattedDate(),
            style = style
        )

        Divisor()

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Text("Mis combinaciones:", style = style)
            DetallesBoleto(boleto, style = style)

            Divisor()

            Text("Combinación ganadora:", style = style)

            Text(
                text = resultado.combinacion,
                style = style
            )

            Divisor()

            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                when (boleto.gameID) {

                    "LAPR" -> {

                        Text(
                            "Joker ganador: ${resultado.joker?.combinacion}",
                            style = style
                        )

                        Divisor()

                        Escrutionio(
                            resultado.escrutinio,
                        )

                        Divisor()

                        Text("Escrutinio Joker:", style = style)

                        EscrutinioJoker(resultado.escrutinioJoker!!)

                    }

                    "BONO" -> {
                        Escrutionio(
                            resultado.escrutinio,
                        )
                    }

                    "EDMS" -> {
                        Escrutionio(
                            resultado.escrutinio,
                        )
                    }

                    "EMIL" -> {

                        Text(
                            "Numero millon ganador: ${resultado.millon?.combinacion}",
                            style = style
                        )

                        Divisor()

                        Escrutionio(
                            resultado.escrutinio
                        )

                        if (resultado.lluvia != null) {

                            Divisor()

                            Text(
                                "Lluvia de millones:",
                                style = style
                            )

                            Lluvia(resultado.lluvia)
                        }
                    }

                    "ELGR" -> {
                        Escrutionio(
                            resultado.escrutinio
                        )
                    }

                }
            }


        }


    }

}

@Composable
fun Escrutionio(escrutinio: List<Escrutinio>) {

    val styleSmall = MaterialTheme.typography.titleSmall
    val styleMedium = MaterialTheme.typography.titleMedium

    Text("Escrutinio:", style = styleMedium)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = "Categoria:", style = styleMedium)

            escrutinio.forEach { escrutinioItem ->
                Text(
                    " ${escrutinioItem.tipo}",
                    style = styleSmall
                )
            }
        }
        Column {
            Text(text = "Ganadores:", style = styleMedium)
            escrutinio.forEach { escrutinioItem ->
                Text(
                    escrutinioItem.ganadores.toString(),
                    style = styleSmall
                )
            }
        }
        Column(
            modifier = Modifier.padding(end = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "Premio:", style = styleMedium)
            escrutinio.forEach { escrutinioItem ->
                Text(
                    "${escrutinioItem.premio} $euro ",
                    style = styleSmall
                )
            }
        }
    }
}

@Composable
fun EscrutinioJoker(escrutinio: List<EscrutinioJoker>) {

    val styleSmall = MaterialTheme.typography.titleSmall
    val styleMedium = MaterialTheme.typography.titleMedium

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = "Categoria:", style = styleMedium)
            escrutinio.forEach { escrutinioItem ->
                Text(
                    " ${escrutinioItem.tipo}",
                    style = styleSmall
                )
            }
        }
        Column(modifier = Modifier.padding(end = 8.dp)) {
            Text(text = "Premio:", style = styleMedium)
            escrutinio.forEach { escrutinioItem ->
                Text(
                    "${escrutinioItem.premio} $euro ",
                    style = styleSmall
                )
            }
        }
    }
}

@Composable
fun Lluvia(lluvia: Lluvia) {
    val list = lluvia.combinacion.split(",")
    Column(
        modifier = Modifier.padding(8.dp),
    ) {
        val chunks = list.chunked(3)
        chunks.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                row.forEach { item ->
                    Text(text = item, style = MaterialTheme.typography.titleSmall)
                }
            }
        }

    }
}