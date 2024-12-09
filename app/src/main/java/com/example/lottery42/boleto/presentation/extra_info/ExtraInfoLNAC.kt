package com.example.lottery42.boleto.presentation.extra_info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.network.models.loteriaNacional.resultadoLNAC.ResultadosLoteriaNacional
import com.example.lottery42.boleto.data.toFormattedDate

@Composable
fun ExtraInfoLNAC(boleto: Boleto, resultado: ResultadosLoteriaNacional) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(top = 32.dp))

        Text(text = boleto.tipo, style = MaterialTheme.typography.displaySmall)

        HorizontalDivider(modifier = Modifier, color = Color.White)

        Text("Fecha:", style = MaterialTheme.typography.headlineMedium)

        Text(
            text = boleto.fecha.toFormattedDate(),
            style = MaterialTheme.typography.headlineMedium
        )

        HorizontalDivider(modifier = Modifier, color = Color.White)

        Text("Mi numero:", style = MaterialTheme.typography.headlineMedium)

        Text(
            text = boleto.numeroLoteria!!,
            style = MaterialTheme.typography.headlineMedium
        )

        HorizontalDivider(modifier = Modifier, color = Color.White)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Primer premio:", style = MaterialTheme.typography.titleMedium)
                Text(resultado.primerPremio.decimo, style = MaterialTheme.typography.headlineMedium)

            }
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Segundo premio:", style = MaterialTheme.typography.titleMedium)
                Text(
                    resultado.segundoPremio.decimo,
                    style = MaterialTheme.typography.headlineMedium
                )

            }
        }
        HorizontalDivider(modifier = Modifier, color = Color.White)

        Text("Reintegros:", style = MaterialTheme.typography.headlineMedium)

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Reintegro(resultado.reintegros[0].decimo)

            Spacer(modifier = Modifier)

            Reintegro(resultado.reintegros[1].decimo)

            Spacer(modifier = Modifier)

            Reintegro(resultado.reintegros[2].decimo)

        }


    }

}

@Composable
fun Reintegro(numero: String) {
    Card(
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                numero,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}