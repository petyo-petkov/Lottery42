package com.example.lottery42.boleto.presentation.boleto_list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun FAB(
    onFABClick : () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = { onFABClick() },
        containerColor = Color(0xFFFFE082),
        contentColor = Color.Black,
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Localized description"
        )

    }

}