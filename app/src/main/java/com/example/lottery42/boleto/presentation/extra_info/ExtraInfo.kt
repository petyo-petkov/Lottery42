package com.example.lottery42.boleto.presentation.extra_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.network.models.Escrutinio
import com.example.lottery42.boleto.data.network.models.EscrutinioJoker
import com.example.lottery42.boleto.data.network.models.Lluvia
import com.example.lottery42.boleto.data.network.models.LotteryModel
import com.example.lottery42.boleto.data.toFormattedDate
import com.example.lottery42.boleto.presentation.boleto_detail.DetallesBoleto
import kotlin.text.Typography.euro

@Composable
fun ExtraInfo(
    resultado: LotteryModel,
    boleto: Boleto
) {

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = boleto.tipo, style = MaterialTheme.typography.displaySmall)

        HorizontalDivider(modifier = Modifier, color = Color.White)

        Text("Fecha:", style = MaterialTheme.typography.titleSmall)

        Text(
            text = boleto.fecha.toFormattedDate(),
            style = MaterialTheme.typography.titleMedium
        )

        HorizontalDivider(modifier = Modifier, color = Color.White)

        LazyColumn(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            item {
                Text("Mis combinaciones:", style = MaterialTheme.typography.titleMedium)

                DetallesBoleto(boleto, style = MaterialTheme.typography.titleSmall)
                HorizontalDivider(modifier = Modifier, color = Color.White)

                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (boleto.gameID) {

                        "LAPR" -> {

                            Text(
                                "Joker ganador: ${resultado.joker?.combinacion}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            HorizontalDivider(modifier = Modifier, color = Color.White)

                            Escrutionio(
                                resultado.escrutinio,
                                resultado.combinacion
                            )

                            HorizontalDivider(modifier = Modifier, color = Color.White)

                            Text("Escrutinio Joker:", style = MaterialTheme.typography.titleMedium)

                            EscrutinioJoker(resultado.escrutinioJoker!!)

                        }

                        "BONO" -> {
                            Escrutionio(
                                resultado.escrutinio,
                                resultado.combinacion
                            )
                        }

                        "EDMS" -> {
                            Escrutionio(
                                resultado.escrutinio,
                                resultado.combinacion
                            )
                        }

                        "EMIL" -> {

                            Text(
                                "Numero millon ganador: ${resultado.millon?.combinacion}",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Escrutionio(
                                resultado.escrutinio,
                                resultado.combinacion
                            )

                            if (resultado.lluvia != null) {
                                HorizontalDivider(modifier = Modifier, color = Color.White)

                                Text(
                                    "Lluvia de millones:",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Lluvia(resultado.lluvia)
                            }
                        }

                        "ELGR" -> {
                            Escrutionio(
                                resultado.escrutinio,
                                resultado.combinacion
                            )
                        }

                    }
                }

            }

        }


    }

}

@Composable
fun Escrutionio(escrutinio: List<Escrutinio>, combiGanadora: String) {
    Text("Combinación ganadora:", style = MaterialTheme.typography.titleMedium)

    Text(
        text = combiGanadora,
        style = MaterialTheme.typography.titleSmall
    )

    HorizontalDivider(modifier = Modifier, color = Color.White)

    Text("Escrutinio:", style = MaterialTheme.typography.titleMedium)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = "Categoria:", style = MaterialTheme.typography.titleSmall)

            escrutinio.forEach { escrutinioItem ->
                Text(
                    " ${escrutinioItem.tipo}",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
        Column {
            Text(text = "Ganadores:", style = MaterialTheme.typography.titleSmall)
            escrutinio.forEach { escrutinioItem ->
                Text(
                    escrutinioItem.ganadores,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
        Column(
            modifier = Modifier.padding(end = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "Premio:", style = MaterialTheme.typography.titleSmall)
            escrutinio.forEach { escrutinioItem ->
                Text(
                    "${escrutinioItem.premio} $euro ",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun EscrutinioJoker(escrutinio: List<EscrutinioJoker>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = "Categoria:", style = MaterialTheme.typography.titleSmall)
            escrutinio.forEach { escrutinioItem ->
                Text(
                    " ${escrutinioItem.tipo}",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
        Column(modifier = Modifier.padding(end = 8.dp)) {
            Text(text = "Premio:", style = MaterialTheme.typography.titleSmall)
            escrutinio.forEach { escrutinioItem ->
                Text(
                    "${escrutinioItem.premio} $euro ",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun Lluvia(lluvia: Lluvia) {
    val list = lluvia.combinacion.split(",")
    Column(
        modifier = Modifier.padding(16.dp),
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