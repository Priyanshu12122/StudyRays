package com.xerox.studyrays.ui.screens.pw.lecturesScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.db.videoDb.Video
import com.xerox.studyrays.downloadManager.AndroidDownloader
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.theme.MainPurple
import com.xerox.studyrays.utils.BottomSheet
import com.xerox.studyrays.utils.CategoryTabRow2
import com.xerox.studyrays.utils.CompletedIconButton
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.PermissionDialog
import com.xerox.studyrays.utils.PullToRefreshLazyColumn
import com.xerox.studyrays.utils.SpacerWidth
import com.xerox.studyrays.videoDownloader.DemoDownloadService
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun LecturesScreen(
    vm: MainViewModel = hiltViewModel(),
    slug: String,
    name: String,
    onVideoClicked: (String, String, String, String) -> Unit,
    onPdfViewClicked: (String, String) -> Unit,
    onBackClicked: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getAllVideos(slug)
    }

    val context = LocalContext.current
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val downloader = AndroidDownloader(context)

    val showNotificationDialog = rememberSaveable { mutableStateOf(false) }
    val permissionState =
        rememberPermissionState(permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


    val videosState by vm.videos.collectAsState()
    val videosResult = videosState
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
                           snackbarHostState
                       )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = name) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClicked()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "")
                    }
                })
        }
    ) { paddingValues ->
        when (videosResult) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()
                DataNotFoundScreen(
                    errorMsg = videosResult.msg,
                    state = messageState,
                    shouldShowBackButton = true,
                    onRetryClicked = {
                        vm.getAllVideos(slug)
                    }) {
                    onBackClicked()
                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = paddingValues)

            }

            is Response.Success -> {

                if (showNotificationDialog.value) {
                    PermissionDialog(
                        showNotificationDialog = showNotificationDialog,
                        permissionState = permissionState,
                        text = "Storage permission is required to \n save pdf in your external storage."
                    )

                }

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

                                    var selectedVideoUrl by rememberSaveable {
                                        mutableStateOf("")
                                    }

                                    var isCompleted by rememberSaveable {
                                        mutableStateOf(false)
                                    }


                                    if (isOpen) {
                                        BottomSheet(
                                            sheetState = rememberModalBottomSheetState(),
                                            onMarkAsCompletedClicked = {
                                                vm.onMarkAsCompleteClicked(Video(selectedVideoId))
                                                savedStatusMap[selectedVideoId] = !isCompleted
                                                isOpen = false

                                            },
                                            onDownloadClicked = {

//                                                val downloadRequest: DownloadRequest =
//                                                    DownloadRequest.Builder(
//                                                        "https://live-par-2-cdn-alt.livepush.io/live/bigbuckbunnyclip/index.m3u8",
//                                                        "https://live-par-2-cdn-alt.livepush.io/live/bigbuckbunnyclip/index.m3u8".toUri()
//                                                    )
//                                                        .build()
//
//                                                DownloadService.sendAddDownload(
//                                                    context,
//                                                    DemoDownloadService::class.java,
//                                                    downloadRequest,
//                                                    false
//                                                )
                                            },
                                            isCompleted = isCompleted
                                        ) {
                                            isOpen = false
                                        }
                                    }

                                    PullToRefreshLazyColumn(
                                        items = videosResult.data.sortedBy { it.name },
                                        content = { video ->
                                            val isComplete = savedStatusMap[video.video_id] ?: false

                                            EachCardForVideo(
                                                imageUrl = video.image_url,
                                                title = video.name,
                                                dateCreated = video.createdAt.substringBefore("T"),
                                                isCompleted = isComplete,
                                                duration = video.duration,
                                                onMoreVertClicked = {
                                                    isOpen = true
                                                    selectedVideoId = video.video_id
                                                    selectedVideoUrl = video.video_url
                                                    isCompleted = isComplete
                                                },
                                                videoId = video.video_id,
                                                checkIfCompleted = {
                                                    scope.launch {
                                                        val saved =
                                                            vm.checkIfItemIsPresentInVideoDb(
                                                                Video(
                                                                    it
                                                                )
                                                            )
                                                        savedStatusMap[video.video_id] = saved
                                                    }
                                                }
                                            ) {
                                                onVideoClicked(
                                                    video.video_url,
                                                    video.name,
                                                    video.external_id,
                                                    video.embedCode
                                                )
                                            }
                                        },
                                        isRefreshing = vm.isRefreshing,
                                        onRefresh = {
                                            vm.refreshAllVideos(slug){
                                                vm.showSnackBar(snackbarHostState)
                                            }
                                        })
//                                    LazyColumn {
//                                        items(videosResult.data.sortedBy { it.name }) { video ->
//
//                                            val isComplete = savedStatusMap[video.video_id] ?: false
//
//                                            EachCardForVideo(
//                                                imageUrl = video.image_url,
//                                                title = video.name,
//                                                dateCreated = video.createdAt.substringBefore("T"),
//                                                isCompleted = isComplete,
//                                                duration = video.duration,
//                                                onMoreVertClicked = {
//                                                    isOpen = true
//                                                    selectedVideoId = video.video_id
//                                                    selectedVideoUrl = video.video_url
//                                                    isCompleted = isComplete
//                                                },
//                                                videoId = video.video_id,
//                                                checkIfCompleted = {
//                                                    scope.launch {
//                                                        val saved =
//                                                            vm.checkIfItemIsPresentInVideoDb(
//                                                                Video(
//                                                                    it
//                                                                )
//                                                            )
//                                                        savedStatusMap[video.video_id] = saved
//                                                    }
//                                                }
//                                            ) {
//                                                onVideoClicked(
//                                                    video.video_url,
//                                                    video.name,
//                                                    video.external_id,
//                                                    video.embedCode
//                                                )
//                                            }
//                                        }
//                                    }


                                }
                            }

                            1 -> {
                                NotesScreen(
                                    slug = slug,
                                    paddingValues = paddingValues,
                                    snackbarHostState = snackbarHostState,
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
                                DppScreen(
                                    slug = slug,
                                    paddingValues = paddingValues,
                                    snackbarHostState = snackbarHostState,
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
                                DppSolutionScreen(
                                    slug = slug,
                                    paddingValues = paddingValues
                                ) { name, url, id ->
                                    onVideoClicked(name, url, id, "")
                                }
                            }

                        }
                    }
                }

            }
        }

    }

}

@Composable
fun EachCardForVideo(
    imageUrl: String?,
    title: String?,
    isCompleted: Boolean,
    dateCreated: String?,
    duration: String?,
    videoId: String,
    checkIfCompleted: (String) -> Unit,
    onMoreVertClicked: () -> Unit,
    onVideoClicked: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        checkIfCompleted(videoId)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .height(130.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
            )
            .clip(RoundedCornerShape(10.dp))
            .background(if (!isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.background)
            .clickable {
                onVideoClicked()
            }
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
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            AsyncImage(
                model = imageUrl ?: "", contentDescription = "",
                modifier = Modifier
                    .padding(12.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp))
                    .weight(4f),
            )
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(6f)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    if (isCompleted) {
                        CompletedIconButton()
                    }

                    SpacerWidth(dp = 5.dp)

                    IconButton(onClick = {
                        onMoreVertClicked()

                    }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More vert")
                    }
                    SpacerWidth(dp = 5.dp)
                }


                Text(
                    text = title ?: "", fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MainPurple,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                )

                Row(modifier = Modifier.weight(1f)) {
                    IconAndText(
                        icon = Icons.Default.DateRange, text = dateCreated ?: "",
                        modifier = Modifier.weight(1f)
                    )
                    IconAndText(
                        icon = Icons.Default.DateRange,
                        text = duration ?: "",
                        modifier = Modifier.weight(1f)
                    )


                }


            }
        }

    }
}


@Composable
fun IconAndText(
    icon: ImageVector,
    text: String,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }

}