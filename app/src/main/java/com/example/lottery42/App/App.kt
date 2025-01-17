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
import com.example.lottery42.boleto.data.database.BalanceState
import com.example.lottery42.boleto.presentation.boleto_detail.DetailScreen
import com.example.lottery42.boleto.presentation.boleto_detail.DetailsViewModel
import com.example.lottery42.boleto.presentation.boleto_list.FAB
import com.example.lottery42.boleto.presentation.boleto_list.ListScreen
import com.example.lottery42.boleto.presentation.boleto_list.ListScreenViewModel
import com.example.lottery42.boleto.presentation.extra_info.ExtraInfoViewModel
import com.example.lottery42.boleto.presentation.extra_info.ExtraPaneScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun App() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()

    val vm: ListScreenViewModel = koinViewModel()
    val vmExtra: ExtraInfoViewModel = koinViewModel()
    val vmDetails: DetailsViewModel = koinViewModel()

    val boletos by vm.boletosState.collectAsStateWithLifecycle()
    val balance by vm.balance.collectAsStateWithLifecycle(BalanceState())
    val boleto by vm.boletoState.collectAsStateWithLifecycle()
    val infoState by vmExtra.infoState.collectAsStateWithLifecycle()
    val premioState by vmDetails.premioState.collectAsStateWithLifecycle()


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
                        vm.startScanning()
                    }
                )
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
                    ListScreen(
                        balanceState = balance,
                        onBoletoClick = {
                            vm.getBoletoByID(it.id)
                            navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                        },
                        onBorrarClick = { vm.deleteAllBoletos() },
                        boletos = boletos,
                        onOrdenar = { vm.ordenarBoletos(it) }
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    if (boleto != null) {
                        DetailScreen(
                            premioState = premioState,
                            boleto = boleto!!,
                            onDeleteBoleto = {
                                vm.deleteBoleto(boleto!!.id)
                                navigator.navigateBack()
                            },
                            onExtraInfoClick = {
                                vmExtra.infoSorteoCelebrado(boleto!!)
                                navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Extra)
                            },
                            onComprobarClick = {
                                vmDetails.getPremio(boleto!!)
                                vm.getBoletoByID(boleto!!.id)
                            }
                        )


                    } else {
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
