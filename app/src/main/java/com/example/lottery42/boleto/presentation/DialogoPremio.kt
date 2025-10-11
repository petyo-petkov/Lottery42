package com.example.lottery42.boleto.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lottery42.boleto.data.database.Boleto

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DialogoPremio(
    show: Boolean,
    onDismiss: () -> Unit,
    boleto: Boleto,
    premio: String
) {
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.textButtonColors(MaterialTheme.colorScheme.onSurface),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(
                        text = "OK",
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            modifier = Modifier,
            dismissButton = { },
            icon = { Icon(Icons.Default.Info, contentDescription = "Info") },
            title = {
                Text(
                    text = "Resultado del sorteo \n${boleto.tipo}: ",
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            text = {
                if (premio == "loading") {
                    LoadingIndicator(
                        modifier = Modifier
                            .size(48.dp),
                        color = Color(0xFFFFE082),
                        polygons = LoadingIndicatorDefaults.DeterminateIndicatorPolygons
                    )
                } else {
                    Text(
                        text = premio,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }


            },
            containerColor = MaterialTheme.colorScheme.surfaceBright
        )
    }

}