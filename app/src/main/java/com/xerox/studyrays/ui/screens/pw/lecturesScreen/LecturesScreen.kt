package com.xerox.studyrays.ui.screens.pw.lecturesScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.xerox.studyrays.R
import com.xerox.studyrays.downloadManager.AndroidDownloader
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoViewModel
import com.xerox.studyrays.ui.theme.MainPurple
import com.xerox.studyrays.utils.CategoryTabRow2
import com.xerox.studyrays.utils.CompletedIconButton
import com.xerox.studyrays.utils.SpacerWidth
import com.xerox.studyrays.utils.WatchLaterButton
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun LecturesScreen(
    vm: MainViewModel = hiltViewModel(),
    topicSlug: String,
    subjectSlug: String,
    batchId: String,
    name: String,
    onVideoClicked: (String, String, String, String, String, String, String, String, String) -> Unit,
    onPdfViewClicked: (String, String) -> Unit,
    onBackClicked: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getAllVideos(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug
        )
    }

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
                        ActualLecturesScreen(
                            onBackClicked = { onBackClicked() },
                            snackbarHostState = snackbarHostState,
                            batchId = batchId,
                            subjectSlug = subjectSlug,
                            topicSlug = topicSlug,
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
                        NotesScreen(
                            batchId = batchId,
                            subjectSlug = subjectSlug,
                            topicSlug = topicSlug,
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
                        DppScreen(
                            batchId = batchId,
                            subjectSlug = subjectSlug,
                            topicSlug = topicSlug,
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
                        DppSolutionScreen(
                            batchId = batchId,
                            subjectSlug = subjectSlug,
                            topicSlug = topicSlug,
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

@Composable
fun EachCardForVideo(
    imageUrl: String?,
    viewModel: VideoViewModel = hiltViewModel(),
    title: String?,
    dateCreated: String?,
    duration: String?,
    videoId: String,
    isSaved: Boolean,
    onStarClick: () -> Unit,
    checkIfSaved: (String) -> Unit,
    onVideoClicked: () -> Unit,

    ) {

    var progress by rememberSaveable { mutableFloatStateOf(0f) }
    LaunchedEffect(key1 = Unit) {
        checkIfSaved(videoId)
        val videoEntity = viewModel.getVideoProgressById(videoId)
        progress = videoEntity?.progress ?: 0f
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .height(120.dp)
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

                    WatchLaterButton(isSaved = isSaved, onClick = {
                        onStarClick()
                    })

                    SpacerWidth(dp = 5.dp)
                }


                Text(
                    text = title ?: "", fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
//                    color = MainPurple,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                )

                Row(modifier = Modifier.weight(1f)) {
                    IconAndText(
                        icon = Icons.Default.DateRange, text = dateCreated ?: "",
                        modifier = Modifier.weight(1f),
                        isImageVector = true,
                        drawableRes = R.drawable.baseline_access_time_24
                    )
                    IconAndText(
                        icon = Icons.Default.DateRange,
                        text = duration ?: "",
                        modifier = Modifier.weight(1f),
                        isImageVector = false,
                        drawableRes = R.drawable.baseline_access_time_24
                    )


                }

                LinearProgressIndicator(
                    progress = {
                        progress
                    },
                    modifier = Modifier.height(1.5.dp)
                )


            }
        }

    }
}

@Composable
fun EachCardForVideo3(
    imageUrl: String?,
    viewModel: VideoViewModel = hiltViewModel(),
    title: String?,
    dateCreated: String?,
    duration: String?,
    onVideoClicked: () -> Unit,
    videoId: String,
) {

    var progress by rememberSaveable { mutableFloatStateOf(0f) }
    LaunchedEffect(key1 = Unit) {
        val videoEntity = viewModel.getVideoProgressById(videoId)
        progress = videoEntity?.progress ?: 0f
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(110.dp)
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
    )

    {
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

                Text(
                    text = title ?: "", fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    lineHeight = 17.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(8.dp)
                )

                Row(modifier = Modifier.weight(1f)) {
                    IconAndText(
                        icon = Icons.Default.DateRange, text = dateCreated ?: "",
                        modifier = Modifier.weight(1f),
                        isImageVector = true,
                        drawableRes = R.drawable.baseline_access_time_24
                    )
                    IconAndText(
                        icon = Icons.Default.DateRange,
                        text = duration ?: "",
                        modifier = Modifier.weight(1f),
                        isImageVector = false,
                        drawableRes = R.drawable.baseline_access_time_24
                    )
                }

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.height(2.5.dp)
                )
            }
        }
    }
}


@Composable
fun EachCardForVideo2(
    imageUrl: String?,
    title: String?,
    dateCreated: String?,
    duration: String?,
    videoId: String,
    onVideoClicked: () -> Unit,
    ) {

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
                        modifier = Modifier.weight(1f),
                        isImageVector = true,
                        drawableRes = R.drawable.baseline_access_time_24
                    )
                    IconAndText(
                        icon = Icons.Default.DateRange,
                        text = duration ?: "",
                        modifier = Modifier.weight(1f),
                        isImageVector = false,
                        drawableRes = R.drawable.baseline_access_time_24
                    )


                }


            }
        }

    }
}


@Composable
fun IconAndText(
    icon: ImageVector,
    isImageVector: Boolean,
    drawableRes: Int,
    text: String,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (isImageVector) {
            Icon(
                imageVector = icon, contentDescription = null, modifier = Modifier
                    .size(15.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = drawableRes),
                contentDescription = null,
                modifier = Modifier
                    .size(15.dp)
            )
        }

        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = text,
            fontSize = 10.sp,
            maxLines = 1,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis
        )

    }

}