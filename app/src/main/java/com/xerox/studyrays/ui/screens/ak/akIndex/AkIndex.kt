package com.xerox.studyrays.ui.screens.ak.akIndex

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.ak.AkViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AkIndex(
    vm: AkViewModel = hiltViewModel(),
    shouldShow: Boolean,
    onClick: (Int) -> Unit,
    onBackClick: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getIndex()
    }

    val state by vm.index.collectAsState()
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

        when (val result = state) {

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

                    if (result.data.data.batchData.isEmpty()) {
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