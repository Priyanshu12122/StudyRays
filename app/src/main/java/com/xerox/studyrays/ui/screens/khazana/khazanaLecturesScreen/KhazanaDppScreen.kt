package com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForNotes
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForNotesLoading
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForVideo3Loading
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.LoadingTemplate
import com.xerox.studyrays.utils.NoFilesFoundScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun KhazanaDppScreen(
    vm: KhazanaViewModel = hiltViewModel(),
    subjectId: String,
    chapterId: String,
    topicId: String,
    topicName: String,
    snackBarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    onPdfViewClicked: (String, String) -> Unit,
    onPdfDownloadClicked: (String?, String?) -> Unit
) {

    val dppState by vm.khazanaDpp.collectAsState()
    val dppResult = dppState

    LaunchedEffect(key1 = Unit) {
        if (dppResult !is Response.Success) {
            vm.getKhazanaDpp(subjectId, chapterId, topicId, topicName = topicName)
        }
    }



    Column(modifier = Modifier.fillMaxSize()) {
        when (dppResult) {
            is Response.Error -> {

                val messageState = rememberMessageBarState()

                DataNotFoundScreen(
                    errorMsg = dppResult.msg,
                    paddingValues = paddingValues,
                    state = messageState,
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getKhazanaDpp(subjectId, chapterId, topicId, topicName = topicName)
                    }) {

                }
            }

            is Response.Loading -> {

                LoadingTemplate() {
                    EachCardForNotesLoading()
                }

//                LoadingScreen(paddingValues = PaddingValues())

            }

            is Response.Success -> {


                if (dppResult.data?.dpps?.isEmpty() == true && dppResult.data.notes.isEmpty()) {
                    NoFilesFoundScreen()
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val list = remember { dppResult.data?.dpps?.distinctBy { it.url } ?: emptyList() }
                        val list2 = remember { dppResult.data?.notes?.distinctBy { it.url } ?: emptyList() }
                        LazyColumn {
//                            Dpps
                            items(list) {
                                EachCardForNotes(title = it.title,
                                    onViewPdfClicked = {
                                        onPdfViewClicked(it.url, it.title)
                                    }) {
                                    onPdfDownloadClicked(it.url, it.title)
                                }
                            }
//                            Notes
                            items(list2) {
                                EachCardForNotes(title = it.title,
                                    onViewPdfClicked = {
                                        onPdfViewClicked(it.url ?: "", it.title ?: "")
                                    }) {
                                    onPdfDownloadClicked(it.url, it.title)
                                }
                            }
                        }
                    }

                }
            }
//            }

            else -> {
                NoFilesFoundScreen()
            }
        }
    }
}