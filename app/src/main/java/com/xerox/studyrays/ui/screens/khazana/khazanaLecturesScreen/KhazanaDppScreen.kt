package com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen

import android.os.Build
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
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForNotes
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
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
    onPdfDownloadClicked: (String?,String?) -> Unit
) {

    LaunchedEffect(key1 = Unit){
        vm.getKhazanaDpp(subjectId, chapterId, topicId, topicName = topicName)
    }

    val dppState by vm.khazanaDpp.collectAsState()
    val dppResult = dppState

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
                LoadingScreen(paddingValues = PaddingValues())

            }

            is Response.Success -> {
                if (dppResult.data?.dpps?.isEmpty() == true && dppResult.data.notes.isEmpty()){
                    NoFilesFoundScreen()
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LazyColumn {
//                            Dpps
                            items(dppResult.data?.dpps ?: emptyList()) {
                                EachCardForNotes(title = it.title,
                                    onViewPdfClicked = {
                                        onPdfViewClicked(it.url,it.title)
                                    }){
                                    onPdfDownloadClicked(it.url,it.title)
                                }
                            }
//                            Notes
                            items(dppResult.data?.notes ?: emptyList()) {
                                EachCardForNotes(title = it.title,
                                    onViewPdfClicked = {
                                        onPdfViewClicked(it.url ?: "",it.title ?: "")
                                    }){
                                    onPdfDownloadClicked(it.url,it.title)
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