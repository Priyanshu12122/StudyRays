package com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn
import com.xerox.studyrays.utils.SearchTextField

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun KhazanaNotesScreen(
    vm: KhazanaViewModel = hiltViewModel(),
    subjectId: String,
    chapterId: String,
    topicId: String,
    topicName: String,
    paddingValues: PaddingValues,
    snackBarHostState: SnackbarHostState,
    onPdfViewClicked: (String, String) -> Unit,
    onPdfDownloadClicked: (url: String?, title: String?) -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getKhazanaNotes(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId,
            topicName = topicName
        )
    }

    val notesState by vm.khazanaNotes.collectAsState()
    val notesResult = notesState
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when (notesResult) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()

                DataNotFoundScreen(
                    errorMsg = notesResult.msg,
                    paddingValues = paddingValues,
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
                    NoFilesFoundScreen()
                } else {
                    val list = notesResult.data
                    val filteredList =
                        list.filter { it?.title?.contains(searchText, true) ?: false }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        PullToRefreshLazyColumn(
                            item = {
                                SearchTextField(
                                    searchText = searchText,
                                    onSearchTextChanged = { searchText = it },
                                    onCrossIconClicked = { searchText = "" },
                                    text = "Search Notes"
                                )
                            },
                            items = if (searchText == "") list else filteredList,
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
                                ) {
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