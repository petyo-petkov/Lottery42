@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.lottery42.boleto.presentation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.example.lottery42.ui.theme.MiAmarillo
import com.example.lottery42.ui.theme.MiVerde

@Composable
fun CustomLoadingIndicator(tamano: Dp) {

    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    Box(
        modifier = Modifier.size(tamano),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
        ){
            // 3
//            LoadingIndicator(
//                modifier = Modifier
//                    .size(tamano * 0.38f)
//                    .offset(x = tamano * -0.1f, y = tamano * 0.26f)
//                    .graphicsLayer(
//                        rotationZ = rotation,
//                        transformOrigin = TransformOrigin(0.6f, 0.6f)
//                    ),
//                color = MaterialTheme.colorScheme.tertiary,
//                polygons = listOf(
//                    MaterialShapes.Cookie4Sided,
//                    MaterialShapes.Oval
//                )
//            )

            // 1
            LoadingIndicator(
                modifier = Modifier
                    .size(tamano * 0.46f)
                    .graphicsLayer(
                        rotationZ = rotation,
                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                    ),
                color = MiAmarillo,
                polygons = listOf(
                    MaterialShapes.Triangle,
                    MaterialShapes.Arrow,
                )
            )
            // 2
            LoadingIndicator(
                modifier = Modifier
                    .size(tamano * 0.33f)
                    .offset(x = tamano * 0.3f, y = tamano * 0.16f)
                    .graphicsLayer(
                        rotationZ = rotation,
                        transformOrigin = TransformOrigin(0f, 0f)
                    ),
                color = MiVerde,
                polygons = listOf(
                    MaterialShapes.Pentagon,
                    MaterialShapes.Diamond,
                )
            )

        }
    }
}