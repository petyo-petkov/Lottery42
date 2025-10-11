package com.example.lottery42.boleto.data.network

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun Prueba() {

        LoadingIndicator(
            color = Color.Blue,
            polygons = listOf(
                MaterialShapes.SoftBurst,
                MaterialShapes.Cookie9Sided,
                MaterialShapes.Pentagon,
                MaterialShapes.Pill,
                MaterialShapes.Sunny,
                MaterialShapes.Cookie4Sided,
                MaterialShapes.Oval
            )
        )

}


