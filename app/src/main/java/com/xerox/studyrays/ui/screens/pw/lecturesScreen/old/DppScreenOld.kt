package com.xerox.studyrays.ui.screens.pw.lecturesScreen.old

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
fun DppScreenOld(
    vm: MainViewModel = hiltViewModel(),
    slug: String,
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    onPdfViewClicked: (String, String) -> Unit,
    onPdfDownloadClicked: (String?, String?) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        vm.getAllDppOld(slug)
    }

    val state by vm.dppOld.collectAsState()
    val dppResult = state

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
                        vm.getAllDppOld(slug)
                    }) {

                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = PaddingValues())
            }

            is Response.Success -> {
                if (dppResult.data.isEmpty()) {
                    NoFilesFoundScreen()
                } else {
                    val list = dppResult.data.sortedBy { it.topic }
                    val filteredList = list.filter {
                        it.topic?.contains(searchText, ignoreCase = true)
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
                                EachCardForNotes(title = it.topic,
                                    onViewPdfClicked = {
                                        onPdfViewClicked(
                                            it.baseUrl + it.attachmentKey,
                                            it.topic ?: ""
                                        )
                                    }) {
                                    onPdfDownloadClicked(
                                        it.baseUrl + it.attachmentKey,
                                        it.topic ?: ""
                                    )
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            onRefresh = {
                                vm.refreshAllDppOld(slug) {
                                    vm.showSnackBar(snackbarHostState)
                                }
                            })

                    }

                }
            }
        }
    }
}