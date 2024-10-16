package com.xerox.studyrays.ui.screens.ak.akIndex

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.ak.AkViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AkIndex(
    vm: AkViewModel = hiltViewModel(),
    shouldShow: Boolean,
    onClick: (Int) -> Unit,
    onBackClick: () -> Unit,
) {

    val state by vm.index.collectAsState()
    val result = state

    LaunchedEffect(key1 = Unit) {
        if (result !is Response.Success) {
            vm.getIndex()
        }
    }


    val snackBarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
                TopAppBar(title = { Text(text = "Apni Kaksha") }, scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(onClick = {
                            onBackClick()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    })

            } else {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Apni Kaksha") },
                    scrollBehavior = scrollBehavior
                )
            }
        }
    ) { paddingValues ->

        when (result) {

            is Response.Error -> {

                DataNotFoundScreen(
                    paddingValues = paddingValues,
                    errorMsg = result.msg,
                    state = rememberMessageBarState(),
                    shouldShowBackButton = false,
                    onRetryClicked = { vm.getIndex() }) {
                }

            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = paddingValues)
            }

            is Response.Success -> {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {

                    if (result.data.data == null || result.data.data.batchData.isNullOrEmpty()) {
                        NoFilesFoundScreen()
                    } else {

                        PullToRefreshLazyColumn(
                            items = result.data.data.batchData,
                            content = {
                                EachCardForAkIndex(item = it) {
                                    onClick(it.id)
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            onRefresh = {
                                vm.refreshIndex(
                                    onComplete = {
                                        vm.showSnackBar(snackBarHostState)
                                    }
                                )

                            })
                    }
                }
            }
        }
    }
}