package com.example.lottery42.App

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.lottery42.boleto.presentation.boleto_list.FAB
import com.example.lottery42.boleto.presentation.boleto_list.ListScreen
import com.example.lottery42.boleto.presentation.boleto_list.ListScreenViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun App() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val vm: ListScreenViewModel = koinViewModel()
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        floatingActionButton = {
            AnimatedVisibility(
                visible = navigator.currentDestination?.pane == ListDetailPaneScaffoldRole.List,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FAB(vm)
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.statusBars,

        ) { innerPadding ->
        NavigableListDetailPaneScaffold(
            modifier = Modifier.padding(innerPadding),
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
