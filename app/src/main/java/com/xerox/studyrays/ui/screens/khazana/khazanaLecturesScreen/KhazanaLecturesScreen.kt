package com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.db.videoDb.Video
import com.xerox.studyrays.downloadManager.AndroidDownloader
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForVideo
import com.xerox.studyrays.utils.BottomSheet
import com.xerox.studyrays.utils.CategoryTabRow2
import com.xerox.studyrays.utils.CategoryTabRow3
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun KhazanaLecturesScreen(
    vm: KhazanaViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    subjectId: String,
    chapterId: String,
    topicId: String,
    topicName: String,
    onPdfViewClicked: (String, String) -> Unit,
    onVideoClicked: (String, String) -> Unit,
    onBackClicked: () -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        vm.getKhazanaVideos(subjectId, chapterId, topicId, topicName = topicName)
    }


    val videoState by vm.khazanaVideo.collectAsState()

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

        when (val videosResult = videoState) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()

                DataNotFoundScreen(
                    paddingValues = paddingValues,
                    errorMsg = videosResult.msg,
                    state = messageState,
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getKhazanaVideos(subjectId, chapterId, topicId, topicName = topicName)
                    }) {

                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = paddingValues)
            }

            is Response.Success -> {

                val pullToRefreshState = rememberPullToRefreshState()
                Column(
                    modifier = Modifier
                        .nestedScroll(pullToRefreshState.nestedScrollConnection)
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    
//
//                    CategoryTabRow3(
//                        pagerState = pagerState,
//                        categories = tabListRow,
//                        onPageClick = {page->
//                              when(page){
//                                  0 -> {
//                                      Text(text = "Hiiii")
//                                  }
//
//                                  1 -> {
//                                      Text(text = "Hiiidlkfgdsnkglknsgnli")
//                                  }
//                              }
//                        },
//                        onTabClicked = {
//                            scope.launch {
//                                pagerState.animateScrollToPage(it)
//                            }
//                        }
//                    )
                    
                    CategoryTabRow2(
                        pagerState = pagerState,
                        categories = tabListRow,
                        onTabClicked = {
                            scope.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        })


                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.padding(top = 8.dp)
                    ) { page ->
                        when (page) {

                            0 -> {
                                if (videosResult.data.isEmpty()) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        val composition by rememberLottieComposition(
                                            spec = LottieCompositionSpec.RawRes(
                                                if (isSystemInDarkTheme()) R.raw.comingsoondarkmode else R.raw.comingsoon
                                            )
                                        )

                                        LottieAnimation(
                                            composition = composition,
                                            iterations = LottieConstants.IterateForever,
                                            modifier = Modifier
                                                .size(300.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                } else {

                                    val savedStatusMap =
                                        remember { mutableStateMapOf<String, Boolean>() }

                                    var isOpen by rememberSaveable {
                                        mutableStateOf(false)
                                    }

                                    var selectedVideoId by rememberSaveable {
                                        mutableStateOf("")
                                    }

                                    var isCompleted by rememberSaveable {
                                        mutableStateOf(false)
                                    }

                                    if (isOpen) {
                                        BottomSheet(
                                            sheetState = rememberModalBottomSheetState(),
                                            onMarkAsCompletedClicked = {
                                                mainViewModel.onMarkAsCompleteClicked(
                                                    Video(
                                                        selectedVideoId
                                                    )
                                                )
                                                savedStatusMap[selectedVideoId] = !isCompleted
                                                isOpen = false

                                            },
                                            onDownloadClicked = {
                                                // Perform download action
                                            },
                                            isCompleted = isCompleted
                                        ) {
                                            isOpen = false
                                        }
                                    }

                                    PullToRefreshLazyColumn(
                                        pullToRefreshState = pullToRefreshState,
                                        items = videosResult.data.filter {
                                            it.video_image != null || it.video_name != null || it.video_created_at != null || it.video_duration != null || it.video_url != null
                                        }.sortedBy { it.chapter_name },
                                        content = {
                                            val isComplete = savedStatusMap[it.video_id] ?: false

                                            EachCardForVideo(
                                                imageUrl = it.video_image,
                                                title = it.video_name,
                                                dateCreated = it.video_created_at?.substring(11)
                                                    ?: "no data",
                                                duration = it.video_duration,
                                                isCompleted = isComplete,
                                                onMoreVertClicked = {
                                                    isOpen = true
                                                    selectedVideoId = it.video_id
                                                    isCompleted = isComplete
                                                },
                                                videoId = it.video_id,
                                                checkIfCompleted = { id ->
                                                    scope.launch {
                                                        val saved =
                                                            mainViewModel.checkIfItemIsPresentInVideoDb(
                                                                Video(id)
                                                            )
                                                        savedStatusMap[it.video_id] = saved
                                                    }
                                                }
                                            ) {
                                                onVideoClicked(
                                                    it.video_url ?: "",
                                                    it.video_name ?: ""
                                                )
                                            }
                                        },
                                        isRefreshing = vm.isRefreshing,
                                        onRefresh = {
                                            vm.refreshKhazanaVideos(
                                                subjectId = subjectId,
                                                chapterId = chapterId,
                                                topicId = topicId,
                                                topicName = topicName
                                            ) {
                                                vm.showSnackBar(snackBarHostState)
                                            }
                                        })


//                                            LazyColumn {
//                                                items(videosResult.data.filter {
//                                                    it.video_image != null || it.video_name != null || it.video_created_at != null || it.video_duration != null || it.video_url != null
//                                                }.sortedBy { it.chapter_name }) {
//
//                                                    val isComplete =
//                                                        savedStatusMap[it.video_id] ?: false
//
//                                                    EachCardForVideo(
//                                                        imageUrl = it.video_image,
//                                                        title = it.video_name,
//                                                        dateCreated = it.video_created_at?.substring(11)
//                                                            ?: "no data",
//                                                        duration = it.video_duration,
//                                                        isCompleted = isComplete,
//                                                        onMoreVertClicked = {
//                                                            isOpen = true
//                                                            selectedVideoId = it.video_id
//                                                            isCompleted = isComplete
//                                                        },
//                                                        videoId = it.video_id,
//                                                        checkIfCompleted = { id ->
//                                                            scope.launch {
//                                                                val saved =
//                                                                    mainViewModel.checkIfItemIsPresentInVideoDb(
//                                                                        Video(id)
//                                                                    )
//                                                                savedStatusMap[it.video_id] = saved
//                                                            }
//                                                        }
//                                                    ) {
//                                                        onVideoClicked(
//                                                            it.video_url ?: "",
//                                                            it.video_name ?: ""
//                                                        )
//                                                    }
//                                                }
//                                            }

//                                    }
                                }

                            }

                            1 -> {
                                KhazanaNotesScreen(
                                    subjectId = subjectId,
                                    chapterId = chapterId,
                                    topicId = topicId,
                                    topicName = topicName,
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
                                    onVideoClicked = { url, title ->
                                        onVideoClicked(url, title)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}