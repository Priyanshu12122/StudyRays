package com.xerox.studyrays.ui.screens.pw.chaptersScreen.old

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
import com.xerox.studyrays.ui.screens.pw.chaptersScreen.EachCardForChapters
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChaptersScreenOld(
    vm: MainViewModel = hiltViewModel(),
    subjectId: String,
    subject: String,
    onBackClicked: () -> Unit,
    onNavigateToKeyScreen: () -> Unit,
    onClick: (String, String) -> Unit,
) {

    val state by vm.lessonsOld.collectAsState()
    val result = state

    LaunchedEffect(key1 = Unit) {
        if (result !is Response.Success) {
            vm.getAllLessonsOld(subjectId)
        }

        vm.checkStartDestinationDuringNavigation(
            onNavigate = {
                onNavigateToKeyScreen()
            }
        )

    }


    val snackbarHostState = remember { SnackbarHostState() }


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(text = subject) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClicked()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }) { paddingValues ->

        when (result) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()

                DataNotFoundScreen(
                    errorMsg = result.msg,
                    paddingValues = paddingValues,
                    state = messageState,
                    shouldShowBackButton = true,
                    onRetryClicked = {
                        vm.getAllLessonsOld(subjectId)
                    }) {
                    onBackClicked()
                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = paddingValues)
            }

            is Response.Success -> {

                val list = remember { result.data.sortedBy { it.id } }

                if (list.isEmpty()) {
                    NoFilesFoundScreen()
                } else {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    ) {

                        PullToRefreshLazyColumn(
                            items = list,
                            content = {
                                EachCardForChapters(
                                    lessonName = it.name,
                                    notes = it.notes,
                                    exercises = it.exercises,
                                    videos = it.videos
                                ) {
                                    onClick(it.slug, it.name)
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            onRefresh = {
                                vm.refreshAllLessonsOld(subjectId) {
                                    vm.showSnackBar(snackbarHostState)
                                }
                            })

                    }

                }

            }

        }
    }

}
