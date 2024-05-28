package com.xerox.studyrays.ui.screens.pw.lecturesScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn


//DPP
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DppScreen(
    vm: MainViewModel = hiltViewModel(),
    slug: String,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    onPdfViewClicked: (String, String) -> Unit,
    onPdfDownloadClicked: (String?, String?) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        vm.getAllDpp(slug)
    }

    val state by vm.dpp.collectAsState()
    val dppResult = state

    Column(modifier = Modifier.fillMaxSize()) {
        when (dppResult) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()
                DataNotFoundScreen(
                    paddingValues = paddingValues,
                    errorMsg = dppResult.msg,
                    state = messageState,
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getAllDpp(slug)
                    }) {

                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = paddingValues)

            }

            is Response.Success -> {
                if (dppResult.data.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val composition by rememberLottieComposition(
                            spec = LottieCompositionSpec.RawRes(
                                if (isSystemInDarkTheme()) R.raw.comingsoondarkmode else R.raw.comingsoon
                            )
                        )

                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier
                                .size(300.dp)
                                .align(Alignment.Center)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        PullToRefreshLazyColumn(
                            items = dppResult.data,
                            content = {
                                EachCardForNotes(title = it.topic,
                                    onViewPdfClicked = {
                                        onPdfViewClicked(
                                            it.baseUrl + it.attachmentKey,
                                            it.topic ?: ""
                                        )
                                    }) {
                                    onPdfDownloadClicked(
                                        it.baseUrl?.plus(it.attachmentKey),
                                        it.topic
                                    )
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            onRefresh = {
                                vm.showSnackBar(snackbarHostState)
                            })
//                        LazyColumn {
//                            items(dppResult.data) {
//                                EachCardForNotes(title = it.topic,
//                                    onViewPdfClicked = {
//                                        onPdfViewClicked(
//                                            it.baseUrl + it.attachmentKey,
//                                            it.topic ?: ""
//                                        )
//                                    }) {
//                                    onPdfDownloadClicked(
//                                        it.baseUrl?.plus(it.attachmentKey),
//                                        it.topic
//                                    )
//                                }
//                            }
//                        }


                    }

                }
            }
        }
    }
}