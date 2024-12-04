package com.example.lottery42.boleto.presentation.extra_info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lottery42.boleto.data.network.models.LotteryModel
import com.example.lottery42.boleto.presentation.extra_info.ExtraInfoViewModel.InfoSorteoState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExtraInfoScreen(
    vmExtra: ExtraInfoViewModel
){
    val result = vmExtra.infoState.collectAsStateWithLifecycle()

    Surface(modifier = Modifier) {
        when (val currentResult = result.value) {
            is InfoSorteoState.Loading -> LoadingInfo()
            is InfoSorteoState.Empty -> EmptyInfo()
            is InfoSorteoState.Error -> Text(text = "Error al obtener los resultados")
            is InfoSorteoState.Success-> infoScreen(info = currentResult.info)


        }

    }

}

@Composable
fun infoScreen(info: LotteryModel){
    Box {
       Text(text = info.toString())
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

@Composable
fun LoadingInfo() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(96.dp)
                .align(Alignment.Center),
            color = Color(0xFFFFE082),

            )
    }
}