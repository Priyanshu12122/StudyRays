package com.xerox.studyrays.ui.screens.ak.akVideosAndNotes

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.xerox.studyrays.ui.screens.ak.AkViewModel
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForVideo
import com.xerox.studyrays.utils.BottomSheet
import com.xerox.studyrays.utils.CategoryTabRow2
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
fun AkVideosAndNotes(
    vm: AkViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    sid: Int,
    tid: Int,
    bid: Int,
    onClick: (String, Int, Int, Int, Int, String) -> Unit,
    onPdfViewClicked: (String, String) -> Unit,
    onBackClick: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getAkVideo(sid = sid, bid = bid, tid = tid)

    }

    val showNotificationDialog = rememberSaveable { mutableStateOf(false) }
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val videoState by vm.akVideo.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val tabRowList = listOf(
        "Videos",
        "Notes"
    )
    val pagerState = rememberPagerState(pageCount = {
        tabRowList.size
    })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val downloader = AndroidDownloader(context)
    val snackBarHostState = remember { SnackbarHostState() }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        topBar = {
            TopAppBar(title = {
                Text(text = "Videos and Notes")
            }, navigationIcon = {
                IconButton(onClick = {
                    onBackClick()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->

        when (val result = videoState) {
            is Response.Error -> {

                DataNotFoundScreen(
                    paddingValues = paddingValues,
                    errorMsg = result.msg,
                    state = rememberMessageBarState(),
                    shouldShowBackButton = false,
                    onRetryClicked = { vm.getAkVideo(sid = sid, bid = bid, tid = tid) }) {

                }

            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = paddingValues)
            }

            is Response.Success -> {

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {

                    CategoryTabRow2(
                        pagerState = pagerState,
                        categories = tabRowList,
                        onTabClicked = {
                            scope.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        }
                    )
                    HorizontalPager(state = pagerState) { page ->
                        when (page) {
                            0 -> {

                                if (result.data.data.class_list.classes.isEmpty()) {
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
                                        items = result.data.data.class_list.classes,
                                        content = {
                                            val isComplete = savedStatusMap[it.uniqueId] ?: false

                                            EachCardForVideo(
                                                imageUrl = it.posterUrl,
                                                title = it.lessonName,
                                                dateCreated = it.startDateTime,
                                                duration = it.timeDuration,
                                                isCompleted = isComplete,
                                                onMoreVertClicked = {
                                                    isOpen = true
                                                    selectedVideoId = it.uniqueId
                                                    isCompleted = isComplete
                                                },
                                                videoId = it.uniqueId,
                                                checkIfCompleted = { id ->
                                                    scope.launch {
                                                        val saved =
                                                            mainViewModel.checkIfItemIsPresentInVideoDb(
                                                                Video(id)
                                                            )
                                                        savedStatusMap[it.uniqueId] = saved
                                                    }

                                                }
                                            ) {
                                                onClick(
                                                    it.lessonUrl,
                                                    sid,
                                                    bid,
                                                    tid,
                                                    it.id,
                                                    it.lessonName
                                                )

                                            }
                                        },
                                        isRefreshing = vm.isRefreshing,
                                        onRefresh = {
                                            vm.refreshAkVideo(
                                                bid = bid, sid = sid, tid = tid
                                            ) {
                                                vm.showSnackBar(snackBarHostState)
                                            }
                                        })

//                                    LazyColumn {
//                                        items(result.data.data.class_list.classes) {
//
//                                            val isComplete = savedStatusMap[it.uniqueId] ?: false
//
//                                            EachCardForVideo(
//                                                imageUrl = it.posterUrl,
//                                                title = it.lessonName,
//                                                dateCreated = it.startDateTime,
//                                                duration = it.timeDuration,
//                                                isCompleted = isComplete,
//                                                onMoreVertClicked = {
//                                                    isOpen = true
//                                                    selectedVideoId = it.uniqueId
//                                                    isCompleted = isComplete
//                                                },
//                                                videoId = it.uniqueId,
//                                                checkIfCompleted = { id ->
//                                                    scope.launch {
//                                                        val saved =
//                                                            mainViewModel.checkIfItemIsPresentInVideoDb(
//                                                                Video(id)
//                                                            )
//                                                        savedStatusMap[it.uniqueId] = saved
//                                                    }
//
//                                                }
//                                            ) {
//                                                onClick(
//                                                    it.lessonUrl,
//                                                    sid,
//                                                    bid,
//                                                    tid,
//                                                    it.id,
//                                                    it.lessonName
//                                                )
//
//                                            }
//                                        }
//                                    }
                                }
                            }

                            1 -> {
                                AkNotes(
                                    sid = sid,
                                    bid = bid,
                                    tid = tid,
                                    onRetryClicked = {
                                        vm.getAkNotes(sid = sid, bid = bid, tid = tid)
                                    },
                                    snackBarHostState = snackBarHostState,
                                    onPdfDownloadClick = { downloadId, name ->

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
                                    onPdfViewClicked = { id, name ->
                                        onPdfViewClicked(id, name)
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