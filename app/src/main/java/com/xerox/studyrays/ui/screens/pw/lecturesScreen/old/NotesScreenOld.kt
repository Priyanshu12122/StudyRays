package com.xerox.studyrays.ui.screens.pw.lecturesScreen.old

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn
import com.xerox.studyrays.utils.SearchTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreenOld(
    vm: MainViewModel = hiltViewModel(),
    slug: String,
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    onPdfViewClicked: (String, String) -> Unit,
    onPdfDownloadClicked: (String?, String?) -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getAllNotesOld(slug)

    }

    val state by vm.notesOld.collectAsState()
    val notesResult = state
    var searchText by rememberSaveable { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        when (notesResult) {
            is Response.Error -> {

                val messageState = rememberMessageBarState()
                DataNotFoundScreen(
                    errorMsg = notesResult.msg,
                    state = messageState,
                    paddingValues = paddingValues,
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getAllNotesOld(slug)
                    }) {
                }

            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = PaddingValues())

            }

            is Response.Success -> {

                if (notesResult.data.isEmpty()) {
                    NoFilesFoundScreen()
                } else {
                    val list = notesResult.data.sortedBy { it.topic }
                    val filteredList = list.filter {
                        it.topic?.contains(searchText, ignoreCase = true)
                            ?: return
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
                                    text = "Search Notes",
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
                                vm.refreshAllNotesOld(slug) {
                                    vm.showSnackBar(snackbarHostState)
                                }
                            })
                    }

                }
            }
        }
    }

}


@Composable
fun EachCardForNotes(
    title: String?,
    onViewPdfClicked: () -> Unit,
    onPdfDownloadClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
            )
            .clip(RoundedCornerShape(10.dp))
            .background(if (!isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.background)
            .then(
                if (isSystemInDarkTheme()) {
                    Modifier.border(
                        1.dp,
                        Color.White.copy(0.6f),
                        RoundedCornerShape(10.dp)
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.pdf),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .weight(1f)
            )

            Text(text = title ?: "", modifier = Modifier.weight(7f))

            Image(
                painter = painterResource(id = R.drawable.eye),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .weight(1f)
                    .clickable {
                        onViewPdfClicked()
                    },
                colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            )

            Image(
                painter = painterResource(id = R.drawable.download),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .weight(1f)
                    .clickable {
                        onPdfDownloadClicked()
                    },
                colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            )
        }

    }

}