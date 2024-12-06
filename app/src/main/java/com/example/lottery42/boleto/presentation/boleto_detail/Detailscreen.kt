package com.example.lottery42.boleto.presentation.boleto_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lottery42.R
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.toFormattedDate
import com.example.lottery42.boleto.presentation.DialogoBorrar
import com.example.lottery42.boleto.presentation.boleto_list.loadImage
import kotlin.text.Typography.euro


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun DetailScreen(
    boleto: Boleto?,
    onDeleteBoleto: () -> Unit,
    onExtraInfoClick: () -> Unit,
    navigator: ThreePaneScaffoldNavigator<Any>
) {
    var showDialogoBorrar by remember { mutableStateOf(false) }
    val smallStyle = MaterialTheme.typography.headlineSmall

    Box(modifier = Modifier) {
        Image(
            painter = loadImage(
                when (boleto?.gameID) {
                    "LAPR" -> R.drawable.la_primitiva
                    "BONO" -> R.drawable.bonoloto
                    "EMIL" -> R.drawable.euromillones
                    "ELGR" -> R.drawable.el_godo
                    "EDMS" -> R.drawable.euro_dreams
                    "LNAC" -> R.drawable.loteria_nacional
                    else -> R.drawable.logo
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = boleto?.tipo ?: "",
                modifier = Modifier,
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = boleto?.fecha?.toFormattedDate() ?: "",
                modifier = Modifier,
                style = smallStyle
            )
            Divisor()

            // Contenido desplazable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Numero de sorteo: ${boleto?.numSorteo}", style = smallStyle)

                Divisor()
                Text("Combinaciones:", style = MaterialTheme.typography.titleLarge)
                DetallesBoleto(boleto, smallStyle)
                Divisor()

                Text(
                    "Precio: ${boleto?.precio} $euro",
                    style = smallStyle
                )
                Text(
                    text = if (boleto?.premio == "0.0") "No Premiado" else "${boleto?.premio} $euro",
                    style = smallStyle
                )
                Divisor()

                Text(
                    text = if ("${boleto?.apuestaMultiple}" == "true") "Apuesta Multiple"
                    else "Apuesta Simple",
                    style = smallStyle
                )
                Divisor()
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {

                            showDialogoBorrar = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text(text = "Borrar")
                    }

                    Button(
                        onClick = {
//                            resultadosViewModel.Premio(boleto)
//                            showDialogResultado = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFE082),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "Comprobar")
                    }
                    Button(
                        onClick = {
                            onExtraInfoClick()
//                            navigator.navigateTo(
//                                pane = ListDetailPaneScaffoldRole.Extra,
//                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFE082),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "Info Sorteo")
                    }


                }

            }
        }

    }
    DialogoBorrar(
        onDismiss = { showDialogoBorrar = false },
        onConfirm = {
            onDeleteBoleto()
            showDialogoBorrar = false
        },
        show = showDialogoBorrar,
        mensaje = "Borrar boleto?"
    )

}

@Composable
fun Divisor() {
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
}
