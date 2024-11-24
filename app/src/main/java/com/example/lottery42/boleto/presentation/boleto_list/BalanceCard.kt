package com.example.lottery42.boleto.presentation.boleto_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun BalanceCard(
    ganado: String,
    gastado: String,
    balance: String
) {

    val rojo = Color(0xFFF44336)
    val amarillo = Color(0xFFFDD835)
    val verde = Color(0xFF4CAF50)

    Card(
        modifier = Modifier
            .size(390.dp, 80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            balanceDate(
                nombre = "GASTADO",
                color = rojo,
                data = gastado
            )
            VerticalDivider(color = MaterialTheme.colorScheme.background)
            balanceDate(
                nombre = "BALANCE",
                color = amarillo,
                data = balance
            )
            VerticalDivider(color = MaterialTheme.colorScheme.background)
            balanceDate(
                nombre = "GANADO",
                color = verde,
                data = ganado
            )

        }

    }
}

@Composable
fun balanceDate(
    nombre: String,
    data: String,
    color: Color
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = nombre,
            color = color,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = data,
            style = MaterialTheme.typography.headlineLarge

        )

    }

}