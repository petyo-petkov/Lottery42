package com.example.lottery42.boleto.presentation.boleto_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
    porcentaje: String,
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
            modifier = Modifier.fillMaxSize().padding(top = 56.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BalanceData(
                nombre = "GASTADO",
                color = MiRojo,
                data = gastado
            )
            VerticalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.background
            )
            BalanceData(
                nombre = "BALANCE",
                color = MiAmarillo,
                data = balance
            )
            VerticalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.background
            )
            BalanceData(
                nombre = "GANADO",
                color = MiVerde,
                data = ganado,
                extraData = porcentaje
            )

        }

    }
}

@Composable
fun BalanceData(
    nombre: String,
    data: String,
    color: Color,
    extraData: String? = null
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
        if (extraData != null) {
            Text(
                text = extraData,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }

    }

}