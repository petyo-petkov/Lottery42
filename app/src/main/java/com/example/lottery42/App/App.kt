package com.example.lottery42.App

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
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

    val coroutine = rememberCoroutineScope()

    // launched
    LaunchedEffect(boletos.size) { boletos }

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
        contentWindowInsets = WindowInsets.safeDrawing

    ) { innerPadding ->
        NavigableListDetailPaneScaffold(
            modifier = Modifier.padding(innerPadding),
            navigator = navigator,
            listPane = {
                AnimatedPane(
                    modifier = Modifier,
                    enterTransition = scaleIn(),
                    exitTransition = fadeOut()
                ) {
                    ListScreen(
                        balanceState = balance,
                        onBoletoClick = {
                            vm.getBoletoByID(it.id)
                            coroutine.launch {
                                navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                            }
                        },
                        onBorrarClick = { vm.deleteAllBoletos() },
                        listaBoletos = boletos,
                        onOrdenar = { vm.getAllBoletos(it) },
                        onDateRangeSelected = {
                            vm.sortBoletosByDate(
                                startDate = it.first!!,
                                endDate = it.second!!
                            )
                        }

                    )
                }

            },
            detailPane = {
                AnimatedPane(
                    modifier = Modifier,
                    enterTransition = scaleIn(),
                    exitTransition = fadeOut()
                ) {
                    if (boleto != null) {
                        DetailScreen(
                            premioState = premioState,
                            boleto = boleto!!,
                            onDeleteBoleto = {
                                vm.deleteBoleto(boleto!!.id)
                                coroutine.launch {
                                    navigator.navigateBack()
                                }
                            },
                            onExtraInfoClick = {
                                vmExtra.infoSorteoCelebrado(boleto!!)
                                coroutine.launch {
                                    navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Extra)
                                }
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
                AnimatedPane(
                    modifier = Modifier,
                    enterTransition = scaleIn(),
                    exitTransition = fadeOut()
                ) {
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

