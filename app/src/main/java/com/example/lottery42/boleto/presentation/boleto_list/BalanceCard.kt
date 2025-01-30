package com.example.lottery42.boleto.presentation.boleto_list

import androidx.compose.foundation.BorderStroke
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
import com.example.lottery42.ui.theme.MiAmarillo
import com.example.lottery42.ui.theme.MiRojo
import com.example.lottery42.ui.theme.MiVerde


@Composable
fun BalanceCard(
    ganado: String,
    gastado: String,
    balance: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .size(420.dp, 180.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = color
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            balanceData(
                nombre = "GASTADO",
                color = MiRojo,
                data = gastado
            )
            VerticalDivider(color = MaterialTheme.colorScheme.background)
            balanceData(
                nombre = "BALANCE",
                color = MiAmarillo,
                data = balance
            )
            VerticalDivider(color = MaterialTheme.colorScheme.background)
            balanceData(
                nombre = "GANADO",
                color = MiVerde,
                data = ganado
            )

        }

    }
}

@Composable
fun balanceData(
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
            style = MaterialTheme.typography.headlineSmall

        )

    }

}