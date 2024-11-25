package com.example.lottery42.boleto.presentation.boleto_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lottery42.boleto.data.database.Boleto

@Composable
fun ListScreen(
    boletos: List<Boleto>,
    ganado: String,
    gastado: String,
    balance: String,
    onBoletoClick: (Boleto) -> Unit,
    onBorrarClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BalanceCard(
            ganado = ganado,
            gastado = gastado,
            balance = balance
        )

        LazyColumn {
            items(boletos, key = { it.id }) { boleto ->
                BoletoItem(boleto, onBoletoClick)
                Spacer(modifier = Modifier.height(4.dp))

            }
        }
        Button(onClick = { onBorrarClick() }
        ) {
            Text(text = "BORRAR")
        }

    }
}

