package com.example.lottery42.boleto.presentation.boleto_list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lottery42.boleto.data.database.BalanceState
import com.example.lottery42.boleto.data.database.Boleto

@Composable
fun ListScreen(
    balanceState: BalanceState,
    listaBoletos: List<Boleto>,
    onBoletoClick: (Boleto) -> Unit,
    onBorrarClick: () -> Unit,
    onOrdenar: (order: String) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (showBottomSheet) 180f else 0f, label = ""
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
        //.statusBarsPadding()
        ,
        verticalArrangement = Arrangement.spacedBy(8.dp), // 64.dp by default
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BalanceCard(
            ganado = balanceState.ganado,
            gastado = balanceState.gastado,
            balance = balanceState.balance,
            color = balanceState.color
        )

        IconButton(
            onClick = { showBottomSheet = true },
            modifier = Modifier
        ) {
            Icon(
                Icons.Default.ArrowDropUp,
                contentDescription = null,
                modifier = Modifier.rotate(rotationState),
                tint = Color(0xFFFFE082)
            )
        }
        LazyColumn(
            modifier = Modifier,
        ) {
            items(listaBoletos, key = { it.id }) { boleto ->
                BoletoItem(
                    boleto,
                    onBoletoClick
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }

        }

        BottomSheet(
            onDismiss = { showBottomSheet = false },
            showBottomSheet = showBottomSheet,
            onBorrarClick = onBorrarClick,
            order = onOrdenar


        )


    }


}

