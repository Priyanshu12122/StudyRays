package com.xerox.studyrays.ui.screens.pw.lecturesScreen.old

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.xerox.studyrays.downloadManager.AndroidDownloader
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.CategoryTabRow2
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun LecturesScreenOld(
    vm: MainViewModel = hiltViewModel(),
    slug: String,
    name: String,
    onVideoClicked: (String, String, String, String, String, String, String, String, String) -> Unit,
    onPdfViewClicked: (String, String) -> Unit,
    onBackClicked: () -> Unit,
) {

    val context = LocalContext.current
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val downloader = AndroidDownloader(context)

    val showNotificationDialog = rememberSaveable { mutableStateOf(false) }
    val permissionState =
        rememberPermissionState(permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


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
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = name) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClicked()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            CategoryTabRow2(
                pagerState = pagerState,
                categories = tabListRow,
                onTabClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                })
            HorizontalPager(
//                        pageCount = tabListRow.size,
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> {
                        ActualLecturesScreenOld(
                            onBackClicked = { onBackClicked() },
                            snackbarHostState = snackbarHostState,
                            slug = slug,
                            paddingValues = paddingValues,
                            onVideoClicked = { videoUrl, name, externalId, embedCode, videoId, imageUrl, createdAt, duration, pw ->
                                onVideoClicked(
                                    videoUrl,
                                    name,
                                    externalId,
                                    embedCode,
                                    videoId,
                                    imageUrl,
                                    createdAt,
                                    duration,
                                    pw
                                )

                            }
                        )

                    }

                    1 -> {
                        NotesScreenOld(
                            slug = slug,
                            snackbarHostState = snackbarHostState,
                            paddingValues = paddingValues,
                            onPdfViewClicked = { id, name ->
                                onPdfViewClicked(id, name)
                            }
                        ) { downloadId, name ->

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                if (permissionState.hasPermission) {
                                    if (downloadId != null) {
                                        downloader.downLoadFile(downloadId, name ?: "Pdf")
                                    } else {
                                        vm.showToast(context, "Pdf Unavailable")
                                    }
                                } else {
                                    showNotificationDialog.value = true
                                }
                            } else {
                                if (downloadId != null) {
                                    downloader.downLoadFile(downloadId, name ?: "Pdf")
                                } else {
                                    vm.showToast(context, "Pdf Unavailable")
                                }
                            }
                        }
                    }

                    2 -> {
                        DppScreenOld(
                            slug = slug,
                            snackbarHostState = snackbarHostState,
                            paddingValues = paddingValues,
                            onPdfViewClicked = { id, name ->
                                onPdfViewClicked(id, name)
                            }
                        ) { downloadId, name ->
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                if (permissionState.hasPermission) {
                                    if (downloadId != null) {
                                        downloader.downLoadFile(downloadId, name ?: "Pdf")
                                    } else {
                                        vm.showToast(context, "Pdf Unavailable")
                                    }
                                } else {
                                    showNotificationDialog.value = true
                                }
                            } else {
                                if (downloadId != null) {
                                    downloader.downLoadFile(downloadId, name ?: "Pdf")
                                } else {
                                    vm.showToast(context, "Pdf Unavailable")
                                }
                            }

                        }

                    }

                    3 -> {
                        DppSolutionScreenOld(
                            slug = slug,
                            paddingValues = paddingValues
                        ) { name, url, id, videoId, imageUrl, createdAt, duration, pw ->
                            onVideoClicked(
                                name,
                                url,
                                id,
                                "",
                                videoId,
                                imageUrl,
                                createdAt,
                                duration,
                                pw
                            )
                        }
                    }

                }
            }
        }

    }

}

