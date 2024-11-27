package com.example.lottery42.boleto.presentation.boleto_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lottery42.R
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.presentation.boleto_list.loadImage


@Composable
fun DetailScreen(boleto: Boleto?) {

    Box(modifier = Modifier) {
        Image(
            painter = loadImage(
                when (boleto?.gameID) {
                    "LAPR" -> R.drawable.la_primitiva
                    "BONO" -> R.drawable.bonoloto
                    "EMIL" -> R.drawable.euromillones
                    "ELGR" -> R.drawable.el_godo
                    "EDMS" -> R.drawable.euro_dreams
                    "LNAC" -> R.drawable.loteria_nacional
                    else -> R.drawable.logo
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = boleto?.tipo ?: "",
                modifier = Modifier,
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                boleto?.fecha ?: "",
                modifier = Modifier,
                style = MaterialTheme.typography.headlineSmall
            )
            HorizontalDivider(modifier = Modifier, color = Color.White)

            // Contenido desplazable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                boleto?.combinaciones?.forEachIndexed { index, combi ->
                    Text(
                        "${index + 1}: $combi",

                        )
                }
                Text(
                    "Reintegro: ${boleto?.reintegro}",

                )

                Text(
                    "Joker: ${boleto?.joker}",

                )
            }
        }

    }

}

