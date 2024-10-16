package com.xerox.studyrays.ui.screens.pw.lecturesScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn
import com.xerox.studyrays.utils.SearchTextField


//DPP
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DppScreen(
    vm: MainViewModel = hiltViewModel(),
    topicSlug: String,
    subjectSlug: String,
    batchId: String,
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    onPdfViewClicked: (String, String) -> Unit,
    onPdfDownloadClicked: (String?, String?) -> Unit,
) {

    val state by vm.dpp.collectAsState()
    val dppResult = state

    LaunchedEffect(key1 = Unit) {
        if(dppResult !is Response.Success){
            vm.getAllDpp(
                batchId = batchId,
                subjectSlug = subjectSlug,
                topicSlug = topicSlug
            )
        }
    }



    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when (dppResult) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()
                DataNotFoundScreen(
                    errorMsg = dppResult.msg,
                    state = messageState,
                    shouldShowBackButton = false,
                    paddingValues = paddingValues,
                    onRetryClicked = {
                        vm.getAllDpp(
                            batchId = batchId,
                            subjectSlug = subjectSlug,
                            topicSlug = topicSlug
                        )
                    }) {

                }
            }

            is Response.Loading -> {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    LazyColumn {
                        items(10) {
                            EachCardForNotesLoading()
                        }
                    }
                }
            }

            is Response.Success -> {
                if (dppResult.data.isEmpty()) {
                    NoFilesFoundScreen()
                } else {
                    val list = dppResult.data.sortedBy { it.homework_topic_name }
                    val filteredList = list.filter {
                        it.homework_topic_name?.contains(searchText, ignoreCase = true)
                            ?: false
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        PullToRefreshLazyColumn(
                            item = {
                                SearchTextField(
                                    searchText = searchText,
                                    onSearchTextChanged = { searchText = it },
                                    text = "Search Dpp's",
                                    onCrossIconClicked = {
                                        searchText = ""
                                    },
                                )
                            },
                            items = if (searchText == "") list else filteredList,
                            content = {
                                EachCardForNotes(
                                    title = it.homework_topic_name,
                                    onViewPdfClicked = {
                                        onPdfViewClicked(
                                            it.attachment_url,
                                            it.homework_topic_name
                                        )
                                    }) {
                                    onPdfDownloadClicked(
                                        it.attachment_url,
                                        it.homework_topic_name
                                    )
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            onRefresh = {
                                vm.refreshAllDpp(
                                    batchId = batchId,
                                    subjectSlug = subjectSlug,
                                    topicSlug = topicSlug
                                ) {
                                    vm.showSnackBar(snackbarHostState)
                                }
                            })

                    }

                }
            }
        }
    }
}