package com.example.lottery42.boleto.presentation.boleto_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lottery42.R.drawable
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.toFormattedDate
import com.example.lottery42.ui.theme.Bonoloto
import com.example.lottery42.ui.theme.ElGodo
import com.example.lottery42.ui.theme.EuroDreams
import com.example.lottery42.ui.theme.EuroMillones
import com.example.lottery42.ui.theme.LoteriaNacional
import com.example.lottery42.ui.theme.MiAmarillo
import com.example.lottery42.ui.theme.MiRojo
import com.example.lottery42.ui.theme.MiVerde
import com.example.lottery42.ui.theme.Primitiva
import kotlin.text.Typography.euro

@Composable
fun BoletoList(
    listaBoletos: List<Boleto>,
    onBoletoClick: (Boleto) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listaBoletos.size) {
        if (listaBoletos.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    LazyColumn(
        modifier = Modifier,
        state = listState,
    )
    {
        items(listaBoletos, key = { it.id }) { boleto ->
            val color = remember(boleto.gameID) {
                when (boleto.gameID) {
                    "LAPR" -> Primitiva
                    "BONO" -> Bonoloto
                    "EMIL" -> EuroMillones
                    "ELGR" -> ElGodo
                    "EDMS" -> EuroDreams
                    "LNAC" -> LoteriaNacional
                    else -> Color.Gray
                }
            }

            val iconRes = remember(boleto.gameID) {
                when (boleto.gameID) {
                    "LAPR" -> drawable.la_primitiva
                    "BONO" -> drawable.bonoloto
                    "EMIL" -> drawable.euromillones
                    "ELGR" -> drawable.el_godo
                    "EDMS" -> drawable.euro_dreams
                    "LNAC" -> drawable.loteria_nacional
                    else -> drawable.logo
                }
            }

            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .border(
                        border = BorderStroke(1.dp, color),
                        shape = RoundedCornerShape(16.dp),
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onBoletoClick(boleto) },
                leadingContent = {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(4.dp)
                    )
                },
                trailingContent = {
                    BoletoStatusIcon(boleto)
                },
                overlineContent = { Text(boleto.fecha.toFormattedDate()) },
                supportingContent = { Text("${boleto.precio} $euro") },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                content = { Text(boleto.tipo, style = MaterialTheme.typography.bodyLarge) },
            )
        }
    }
}

@Composable
private fun BoletoStatusIcon(boleto: Boleto) {
    when {
        boleto.premio == "-0.0" -> Icon(
            imageVector = Icons.Default.QuestionMark,
            contentDescription = null,
            modifier = Modifier.padding(8.dp),
            tint = MiAmarillo
        )

        (boleto.premio.toDoubleOrNull() ?: 0.0) > 0.0 ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = null,
                    tint = MiVerde
                )
                Text("${boleto.premio} $euro", style = MaterialTheme.typography.labelSmall)
            }

        else -> Icon(
            imageVector = Icons.Default.ThumbDown,
            contentDescription = null,
            modifier = Modifier.padding(8.dp),
            tint = MiRojo
        )
    }
}
