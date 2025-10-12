@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.lottery42.boleto.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun CustomLoadingIndicator(tamano: Dp) {
    Box(
        modifier = Modifier.size(tamano),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
        ){
            LoadingIndicator(
                modifier = Modifier
                    .size(tamano * 0.46f),
                color = MaterialTheme.colorScheme.primary,
                polygons = listOf(
                    MaterialShapes.SoftBurst,
                    MaterialShapes.Cookie9Sided,
                )
            )

            LoadingIndicator(
                modifier = Modifier
                    .size(tamano * 0.33f)
                    .offset(x = tamano * 0.3f, y = tamano * 0.16f),
                color = MaterialTheme.colorScheme.secondary,
                polygons = listOf(
                    MaterialShapes.Pill,
                    MaterialShapes.Sunny,
                )
            )

            LoadingIndicator(
                modifier = Modifier
                    .size(tamano * 0.38f)
                    .offset(x = tamano * -0.1f, y = tamano * 0.26f),
                color = MaterialTheme.colorScheme.tertiary,
                polygons = listOf(
                    MaterialShapes.Cookie4Sided,
                    MaterialShapes.Oval
                )
            )
        }
    }
}