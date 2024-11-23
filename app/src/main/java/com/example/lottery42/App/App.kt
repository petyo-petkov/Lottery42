package com.example.lottery42.App

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.lottery42.boleto.presentation.boleto_list.ListScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun App() {
    MaterialTheme {
        val navigator = rememberListDetailPaneScaffoldNavigator<Any>()

        NavigableListDetailPaneScaffold(
            modifier = Modifier.padding(),
            navigator = navigator,
            listPane = {
                AnimatedPane {
                    ListScreen()
                }
            },
            detailPane = {
                AnimatedPane {
                }
            },
            extraPane = {
                AnimatedPane {
                }
            },
            defaultBackBehavior = BackNavigationBehavior.PopUntilContentChange,

            )


    }


}
