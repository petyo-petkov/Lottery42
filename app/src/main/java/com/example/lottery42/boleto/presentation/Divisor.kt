package com.example.lottery42.boleto.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Divisor() {
    HorizontalDivider(
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
        color = MaterialTheme.colorScheme.onSurface
    )
}