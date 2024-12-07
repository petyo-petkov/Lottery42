package com.example.lottery42.App

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lottery42.boleto.presentation.boleto_detail.DetailScreen
import com.example.lottery42.boleto.presentation.boleto_list.FAB
import com.example.lottery42.boleto.presentation.boleto_list.ListScreen
import com.example.lottery42.boleto.presentation.boleto_list.ListScreenActions.borrarBoleto
import com.example.lottery42.boleto.presentation.boleto_list.ListScreenActions.onBoletoClick
import com.example.lottery42.boleto.presentation.boleto_list.ListScreenActions.onBorrarAllClick
import com.example.lottery42.boleto.presentation.boleto_list.ListScreenActions.onFABClick
import com.example.lottery42.boleto.presentation.boleto_list.ListScreenViewModel
import com.example.lottery42.boleto.presentation.extra_info.ExtraInfoViewModel
import com.example.lottery42.boleto.presentation.extra_info.ExtraPaneScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun App() {
    val vm: ListScreenViewModel = koinViewModel()
    val vmExtra: ExtraInfoViewModel = koinViewModel()
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val listState by vm.listState.collectAsStateWithLifecycle()
    val infoState by vmExtra.infoState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        floatingActionButton = {
            AnimatedVisibility(
                visible = navigator.currentDestination?.pane == ListDetailPaneScaffoldRole.List,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FAB(
                    onFABClick = {
                        vm.onAction(onFABClick)
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.statusBars,

        ) { innerPadding ->
        val boleto = listState.boleto

        NavigableListDetailPaneScaffold(
            modifier = Modifier.padding(innerPadding),
            navigator = navigator,
            listPane = {
                AnimatedPane {
                    ListScreen(
                        state = listState,
                        onBoletoClick = {
                            vm.onAction(onBoletoClick(it))
                            navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                        },
                        onBorrarClick = {
                            vm.onAction(onBorrarAllClick)
                        }
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    if (boleto != null) {
                        DetailScreen(
                            boleto = boleto,
                            onDeleteBoleto = {
                                vm.onAction(borrarBoleto(boleto.id))
                                navigator.navigateBack()
                            },
                            onExtraInfoClick = {
                                vmExtra.infoSorteoCelebrado(boleto)
                                navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Extra)
                            },
                            onComprobarClick = { }
                        )

                    }else {
                        EmptyScreen()
                    }
                }
            },
            extraPane = {
                AnimatedPane {
                    ExtraPaneScreen(
                        infoState = infoState,
                        boleto = boleto!!
                    )
                }
            },
            defaultBackBehavior = BackNavigationBehavior.PopUntilContentChange,
        )
    }

}

@Composable
fun EmptyScreen() {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Selecione un boleto",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
