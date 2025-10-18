package com.example.lottery42.boleto.presentation.boleto_list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.lottery42.ui.theme.MiAmarillo

@Composable
fun FAB(
    onFABClick : () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = { onFABClick() },
        containerColor = MiAmarillo,
        contentColor = Color.Black,
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Localized description"
        )

    }

}