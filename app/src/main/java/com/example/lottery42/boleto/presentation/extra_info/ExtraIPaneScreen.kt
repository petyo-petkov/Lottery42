package com.example.lottery42.boleto.presentation.extra_info

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.network.models.LotteryModel
import com.example.lottery42.boleto.data.network.models.loteriaNacional.resultadoLNAC.ResultadosLoteriaNacional
import com.example.lottery42.boleto.presentation.CustomLoadingIndicator
import com.example.lottery42.boleto.presentation.extra_info.ExtraInfoViewModel.InfoSorteoState

@Composable
fun ExtraPaneScreen(
    infoState: InfoSorteoState<Any>,
    boleto: Boleto
) {
    Surface(modifier = Modifier) {
        when (val currentResult = infoState) {
            is InfoSorteoState.Loading -> CustomLoadingIndicator(200.dp)
            is InfoSorteoState.Empty -> EmptyInfo()
            is InfoSorteoState.Error -> Text(text = "Error al obtener los resultados")
            is InfoSorteoState.Success<*> -> {
                when (val info = currentResult.info) {
                    is ResultadosLoteriaNacional -> {
                        ExtraInfoLNAC(boleto = boleto, resultado = info)
                    }

                    is LotteryModel -> {
                        ExtraInfo(boleto = boleto, resultado = info)
                    }

                }

            }

        }
    }

}


@Composable
fun EmptyInfo() {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Sorteo no selebrado",
            style = MaterialTheme.typography.headlineMedium
        )

    }
}

/*
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingInfo() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator(
            modifier = Modifier
                .size(96.dp)
                .align(Alignment.Center),
            color = Color(0xFFFFE082),
            polygons = LoadingIndicatorDefaults.DeterminateIndicatorPolygons

        )
    }
}

 */
