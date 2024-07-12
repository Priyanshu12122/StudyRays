package com.xerox.studyrays.ui.screens.ak.akVideosAndNotes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.ak.AkViewModel
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForNotes
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AkNotes(
    vm: AkViewModel = hiltViewModel(),
    sid: Int,
    bid: Int,
    tid: Int,
    onRetryClicked: () -> Unit,
    snackBarHostState: SnackbarHostState,
    onPdfViewClicked: (String, String) -> Unit,
    onPdfDownloadClick: (String?, String?) -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getAkNotes(sid = sid, bid = bid, tid = tid)
    }

    val notesState by vm.akNotes.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        when (val result = notesState) {
            is Response.Error -> {

                DataNotFoundScreen(
                    errorMsg = result.msg,
                    state = rememberMessageBarState(),
                    shouldShowBackButton = false,
                    onRetryClicked = { onRetryClicked() }) {

                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = PaddingValues(0.dp, 0.dp))

            }

            is Response.Success -> {


                if (result.data.data.notesDetails.isEmpty()) {
                    NoFilesFoundScreen()
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {

                        PullToRefreshLazyColumn(
                            items = result.data.data.notesDetails.sortedBy { it.notesno },
                            content = {
                                EachCardForNotes(title = it.docTitle,
                                    onViewPdfClicked = {
                                        onPdfViewClicked(it.docUrl ?: "", it.docTitle ?: "")
                                    }) {
                                    onPdfDownloadClick(it.docUrl, it.docTitle)
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            onRefresh = {
                                vm.refreshAkNote(
                                    bid = bid, sid = sid, tid = tid
                                ) {
                                    vm.showSnackBar(snackBarHostState)
                                }
                            })
//                        LazyColumn {
//
//                            items(result.data.data.notesDetails.sortedBy { it.notesno }) {
//                                EachCardForNotes(title = it.docTitle,
//                                    onViewPdfClicked = {
//                                        onPdfViewClicked(it.docUrl ?: "",it.docTitle ?: "")
//                                    }) {
//                                    onPdfDownloadClick(it.docUrl, it.docTitle)
//                                }
//
//                            }
//
//                        }

                    }
                }


            }
        }

    }

}