package com.example.lottery42.boleto.presentation.boleto_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lottery42.boleto.presentation.DialogoBorrar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onBorrarClick: () -> Unit,
    onDismiss: () -> Unit,
    showBottomSheet: Boolean,
    order: (String) -> Unit,
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit

) {
    var showDialogoBorrar by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

   var showDatePicker by remember { mutableStateOf(false) }

    // Segmented button
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Ultimos", "Tipo", "Premiado", "Fechas")

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = sheetState
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.padding(12.dp)
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = {
                            selectedIndex = index
                            when (selectedIndex) {

                                0 -> {
                                    order("fecha")
                                }

                                1 -> {
                                    order("tipo")
                                }

                                2 -> {
                                    order("premio")
                                }

                                3 -> {
                                    showDatePicker = true
                                }

                            }

                        },
                        selected = index == selectedIndex,
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.inversePrimary
                        )
                    ) {
                        Text(label)
                    }
                }
            }
            HorizontalDivider()

            // Boton Borrar Todos los boletos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { showDialogoBorrar = true },
                    modifier = Modifier
                        .padding(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                    )

                ) {
                    Text(
                        text = "Borrar todo",
                        color = MaterialTheme.colorScheme.onError
                    )

                }

            }
        }
    }

    DialogoBorrar(
        onDismiss = { showDialogoBorrar = false },
        onConfirm = {
            onBorrarClick()
            showDialogoBorrar = false
            onDismiss()
        },
        show = showDialogoBorrar,
        mensaje = "Borrar todos los boletos?"
    )

    SortByDate(
        showDatePicker = showDatePicker,
        onDateRangeSelected = { onDateRangeSelected(it) },
        onDismiss = { showDatePicker = false }
    )
}