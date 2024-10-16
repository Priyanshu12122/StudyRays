package com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.xerox.studyrays.downloadManager.AndroidDownloader
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.MatchTabs
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun KhazanaLecturesScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    subjectId: String,
    chapterId: String,
    topicId: String,
    topicName: String,
    onPdfViewClicked: (String, String) -> Unit,
    onVideoClicked: (String, String, String, String, String, String, String) -> Unit,
    onNavigateToKeyScreen: () -> Unit,
    onBackClicked: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        mainViewModel.checkStartDestinationDuringNavigation {
            onNavigateToKeyScreen()
        }
    }

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val downloader = AndroidDownloader(context)

    val showNotificationDialog = rememberSaveable { mutableStateOf(false) }
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val tabListRow = listOf(
        "Lectures",
        "Notes",
        "DPPs",
        "Solutions"
    )
    val pagerState = rememberPagerState(pageCount = { tabListRow.size })
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = topicName) },
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
                })
        }
    ) { paddingValues ->

        val pullToRefreshState = rememberPullToRefreshState()
        Column(
            modifier = Modifier
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
                .padding(paddingValues)
                .fillMaxSize()
        ) {
//                    CategoryTabRow2(
//                        pagerState = pagerState,
//                        categories = tabListRow,
//                        onTabClicked = {
//                            scope.launch {
//                                pagerState.animateScrollToPage(it)
//                            }
//                        })

            MatchTabs(
                tabs = tabListRow,
                pagerState = pagerState,
                onTabSelected = {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            )


            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(top = 8.dp)
            ) { page ->
                when (page) {

                    0 -> {
                        ActualKhazanaLecturesScreen(
                            snackbarHostState = snackBarHostState,
                            subjectId = subjectId,
                            chapterId = chapterId,
                            topicId = topicId,
                            topicName = topicName,
                            pullToRefreshState = pullToRefreshState,
                            paddingValues = paddingValues,
                            onVideoClicked = { url, name, id, imageUrl, createdAt, duration, khazana ->
                                onVideoClicked(
                                    url,
                                    name,
                                    id,
                                    imageUrl,
                                    createdAt,
                                    duration,
                                    khazana
                                )

                            }
                        )

                    }

                    1 -> {
                        KhazanaNotesScreen(
                            subjectId = subjectId,
                            chapterId = chapterId,
                            topicId = topicId,
                            topicName = topicName,
                            paddingValues = paddingValues,
                            snackBarHostState = snackBarHostState,
                            onPdfDownloadClicked = { url, title ->

                                if (permissionState.hasPermission) {

                                    if (url != null) {
                                        downloader.downLoadFile(url, title ?: "Pdf")
                                    } else {
                                        mainViewModel.showToast(context, "Pdf Unavailable")
                                    }
                                } else {
                                    showNotificationDialog.value = true
                                }
                            },
                            onPdfViewClicked = { id, name ->
                                onPdfViewClicked(id, name)
                            }
                        )

                    }

                    2 -> {
                        KhazanaDppScreen(
                            subjectId = subjectId,
                            chapterId = chapterId,
                            topicId = topicId,
                            topicName = topicName,
                            onPdfDownloadClicked = { downloadId, name ->

                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                    if (permissionState.hasPermission) {
                                        if (downloadId != null) {
                                            downloader.downLoadFile(
                                                downloadId,
                                                name ?: "Pdf"
                                            )
                                        } else {
                                            mainViewModel.showToast(
                                                context,
                                                "Pdf Unavailable"
                                            )
                                        }
                                    } else {
                                        showNotificationDialog.value = true
                                    }
                                } else {
                                    if (downloadId != null) {
                                        downloader.downLoadFile(downloadId, name ?: "Pdf")
                                    } else {
                                        mainViewModel.showToast(context, "Pdf Unavailable")
                                    }
                                }

                            },
                            snackBarHostState = snackBarHostState,
                            paddingValues = paddingValues,
                            onPdfViewClicked = { id, name ->
                                onPdfViewClicked(id, name)
                            }
                        )

                    }

                    3 -> {

                        KhazanaSolutionsScreen(
                            subjectId = subjectId,
                            chapterId = chapterId,
                            topicId = topicId,
                            topicName = topicName,
                            snackBarHostState = snackBarHostState,
                            paddingValues = paddingValues,
                            onVideoClicked = { url, title, videoId, imageUrl, createdAt, duration, khazana ->
                                onVideoClicked(
                                    url,
                                    title,
                                    videoId,
                                    imageUrl,
                                    createdAt,
                                    duration,
                                    khazana
                                )
                            }
                        )
                    }
                }
            }
        }


    }
}