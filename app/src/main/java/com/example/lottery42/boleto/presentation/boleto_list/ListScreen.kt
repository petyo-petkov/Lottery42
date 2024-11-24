package com.example.lottery42.boleto.presentation.boleto_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
@Composable
fun ListScreen(
    listScreenViewModel: ListScreenViewModel = koinViewModel(),
) {
    val state by listScreenViewModel.boletoState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),

    ) {
        Column(
            modifier = Modifier.statusBarsPadding().padding(48.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                items(state, key = { it.id }) { boleto ->
                    BoletoItem(boleto)

                }
            }

            Button(onClick = {
                listScreenViewModel.startScanning()
            }
            ) {
                Text(text = "CLICK_ME")
            }
            Button(onClick = {
                listScreenViewModel.deleteAllBoletos()
            }
            ) {
                Text(text = "BORRAR")
            }

        }
    }

}