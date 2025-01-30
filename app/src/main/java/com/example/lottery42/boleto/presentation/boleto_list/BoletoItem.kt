package com.example.lottery42.boleto.presentation.boleto_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lottery42.R.drawable
import com.example.lottery42.boleto.data.database.Boleto
import com.example.lottery42.boleto.data.toFormattedDate
import com.example.lottery42.ui.theme.Bonoloto
import com.example.lottery42.ui.theme.ElGodo
import com.example.lottery42.ui.theme.EuroDreams
import com.example.lottery42.ui.theme.EuroMillones
import com.example.lottery42.ui.theme.LoteriaNacional
import com.example.lottery42.ui.theme.Primitiva
import java.lang.ref.WeakReference
import kotlin.text.Typography.euro


@Composable
fun BoletoItem(
    boleto: Boleto,
    onBoletoClick: (Boleto) -> Unit
) {
    val color = when (boleto.gameID) {
        "LAPR" -> Primitiva
        "BONO" -> Bonoloto
        "EMIL" -> EuroMillones
        "ELGR" -> ElGodo
        "EDMS" -> EuroDreams
        "LNAC" -> LoteriaNacional
        else -> MaterialTheme.colorScheme.error
    }
    ListItem(
        headlineContent = { Text(boleto.tipo) },
        modifier = Modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = color
                ),
                shape = RoundedCornerShape(16.dp)
                )
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable {
               onBoletoClick(boleto)
            },
        overlineContent = { Text(boleto.fecha.toFormattedDate()) },
        supportingContent = { Text("${boleto.precio} $euro") },
        leadingContent = {
            Image(
                painter = loadImage(
                    when (boleto.gameID) {
                        "LAPR" -> drawable.la_primitiva

                        "BONO" -> drawable.bonoloto

                        "EMIL" -> drawable.euromillones

                        "ELGR" -> drawable.el_godo

                        "EDMS" -> drawable.euro_dreams

                        "LNAC" -> drawable.loteria_nacional

                        else -> drawable.logo
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 3.dp)
                    .clip(shape = RoundedCornerShape(2.dp))
                    .size(24.dp)
                    .background(Color.Transparent),
                alignment = Alignment.BottomCenter
            )

        },
        trailingContent = {
            if (boleto.premio != "0.0") {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Paid,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Text("${boleto.premio} $euro ")
                }
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        )

    )
}


// Funcioón para carga de imagenes optimizada
@Composable
fun loadImage(imageResourceId: Int): Painter {
    val imageCache = remember { mutableMapOf<Int, WeakReference<Painter>>() }
    val cachedImage = imageCache[imageResourceId]?.get()
    when (cachedImage) {
        null -> {
            val image = painterResource(id = imageResourceId)
            imageCache[imageResourceId] = WeakReference(image)
            return image
        }

        else -> {
            return cachedImage
        }
    }
}