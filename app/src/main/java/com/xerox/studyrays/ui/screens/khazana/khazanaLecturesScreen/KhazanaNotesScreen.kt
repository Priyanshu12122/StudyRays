package com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.remember
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
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForNotes
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun KhazanaNotesScreen(
    vm: KhazanaViewModel = hiltViewModel(),
    subjectId: String,
    chapterId: String,
    topicId: String,
    topicName: String,
    snackBarHostState: SnackbarHostState,
    onPdfViewClicked: (String, String) -> Unit,
    onPdfDownloadClicked: (url: String?, title: String?) -> Unit,
    ) {

    LaunchedEffect(key1 = Unit) {
        vm.getKhazanaNotes(subjectId = subjectId, chapterId = chapterId, topicId = topicId, topicName = topicName)
    }

    val notesState by vm.khazanaNotes.collectAsState()
    val notesResult = notesState

    Column(modifier = Modifier.fillMaxSize()) {
        when (notesResult) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()

                DataNotFoundScreen(
                    errorMsg = notesResult.msg,
                    state = messageState,
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getKhazanaNotes(subjectId, chapterId, topicId, topicName = topicName)
                    }) {

                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = PaddingValues())

            }

            is Response.Success -> {
                if (notesResult.data.isNullOrEmpty() || notesResult.data.isEmpty()) {
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
                            items = notesResult.data,
                            content = {
                                EachCardForNotes(title = it?.title,
                                    onViewPdfClicked = {
                                        onPdfViewClicked(it?.url ?: "", it?.title ?: "")
                                    }) {
                                    onPdfDownloadClicked(
                                        it?.url,
                                        it?.title
                                    )
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            onRefresh = {
                                vm.refreshKhazanaNotes(
                                    subjectId = subjectId,
                                    chapterId = chapterId,
                                    topicId = topicId,
                                    topicName = topicName
                                ){
                                    vm.showSnackBar(snackBarHostState)
                                }
                            })

//                        LazyColumn {
//                            items(notesResult.data) {
//                                EachCardForNotes(title = it?.title,
//                                    onViewPdfClicked = {
//                                        onPdfViewClicked(it?.url ?: "", it?.title ?: "")
//                                    }) {
//                                    onPdfDownloadClicked(
//                                        it?.url,
//                                        it?.title
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