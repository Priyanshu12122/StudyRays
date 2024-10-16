package com.xerox.studyrays.ui.screens.khazana.khazanaChaptersScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.ui.screens.pw.chaptersScreen.EachCardForChapterLoading
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingTemplate
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn2

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KhazanaChaptersScreen(
    vm: KhazanaViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    subjectId: String,
    chapterId: String,
    imageUrl: String,
    courseName: String,
    onBackClick: () -> Unit,
    onNavigateToKeyScreen: () -> Unit,
    onClick: (subjectId: String, chapterId: String, topicId: String, topicName: String) -> Unit,
) {

    val state by vm.khazanaChapters.collectAsState()
    val result = state

    LaunchedEffect(key1 = Unit) {
        if (result !is Response.Success) {
            vm.getKhazanaChapters(subjectId = subjectId, chapterId = chapterId)
        }

        mainViewModel.checkStartDestinationDuringNavigation(
            onNavigate = {
                onNavigateToKeyScreen()
            }
        )

    }


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }


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
            TopAppBar(
                title = { Text(text = courseName.substringBefore(" By")) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        when (result) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()

                DataNotFoundScreen(
                    paddingValues = paddingValues,
                    errorMsg = result.msg,
                    state = messageState,
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getKhazanaChapters(subjectId = subjectId, chapterId = chapterId)
                    }) {

                }
            }

            is Response.Loading -> {

                LoadingTemplate(paddingValues = paddingValues) {
                    EachCardForChapterLoading()
                }

            }

            is Response.Success -> {

                if (result.data.isEmpty()) {
                    NoFilesFoundScreen()
                } else {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    ) {

                        PullToRefreshLazyColumn2(
                            items = result.data.sortedBy { it.name },
                            content = {
                                EachCardForKhazanaChapter(item = it) {
                                    onClick(
                                        it.subjectId,
                                        it.chapter_id,
                                        it.external_id,
                                        it.name
                                    )
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            imageUrl = imageUrl,
                            courseName = courseName,
                            onRefresh = {
                                vm.refreshKhazanaChapters(
                                    subjectId = subjectId,
                                    chapterId = chapterId
                                ) {
                                    vm.showSnackBar(snackBarHostState)
                                }
                            }
                        )
                    }

                }

            }
        }
    }
}