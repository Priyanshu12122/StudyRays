package com.xerox.studyrays.ui.screens.khazana.khazanaScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyVerticalGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KhazanaScreen(
    vm: KhazanaViewModel = hiltViewModel(),
    shouldShow: Boolean,
    onClick: (String) -> Unit,
    onBackClicked: () -> Unit,
) {

    val state by vm.khazana.collectAsState()
    val result = state

    LaunchedEffect(key1 = Unit) {
        if (result !is ResponseTwo.Success) {
            vm.getKhazana()
        }
    }


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        },
        topBar = {

            if (shouldShow) {
                TopAppBar(
                    title = { Text(text = "Khazana") },
                    navigationIcon = {


                        IconButton(onClick = {
                            onBackClicked()
                        }) {

                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")

                        }


                    },
                    scrollBehavior = scrollBehavior
                )

            } else {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Khazana") },
                    scrollBehavior = scrollBehavior
                )
            }
        }
    ) { paddingValues ->

        when (result) {
            is ResponseTwo.Error -> {
                val messageState = rememberMessageBarState()

                DataNotFoundScreen(
                    errorMsg = result.msg,
                    state = messageState,
                    shouldShowBackButton = true,
                    onRetryClicked = {
                        vm.getKhazana()
                    }) {
                    onBackClicked()

                }
            }

            is ResponseTwo.Loading -> {

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    LazyVerticalGrid(GridCells.Fixed(2)) {
                        items(9) {
                            EachCardForKhazanaLoading()
                        }
                    }
                }

            }

            is ResponseTwo.Nothing -> {

            }

            is ResponseTwo.Success -> {

                if (result.data.isEmpty()) {
                    NoFilesFoundScreen()
                } else {
                    val list = result.data.sortedBy { it.showorder }
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        PullToRefreshLazyVerticalGrid(
                            items = list,
                            content = {
                                EachCardForKhazana(item = it) {
                                    onClick(it.slug)
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            gridCells = 2,
                            onRefresh = {
                                vm.refreshKhazana(onComplete = {
                                    vm.showSnackBar(snackBarHostState)
                                })
                            })
                    }

                }


            }
        }
    }
}
