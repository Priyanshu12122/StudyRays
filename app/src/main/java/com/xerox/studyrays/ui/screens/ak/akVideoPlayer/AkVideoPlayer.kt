package com.xerox.studyrays.ui.screens.ak.akVideoPlayer

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.db.downloadsDb.DownloadNumberEntity
import com.xerox.studyrays.downloadManager.idm.Util1DM
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.ak.AkViewModel
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.AlertSection
import com.xerox.studyrays.ui.screens.videoPlayerScreen.TextWithIcon
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoPlayerTaskAlertDialog
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoViewModel
import com.xerox.studyrays.ui.screens.videoPlayerScreen.setScreenOrientation
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.SpacerHeight
import com.xerox.studyrays.utils.SpacerWidth
import com.xerox.studyrays.utils.toReadableDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AkVideoPlayer(
    vm: AkViewModel = hiltViewModel(),
    videoViewModel: VideoViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    sid: Int,
    tid: Int,
    bid: Int,
    key: Int,
    lid: String,
    title: String,
    videoId: String,
    onNavigateToTaskScreen: (String, String) -> Unit,
    onBackClick: () -> Unit,
) {

    val downloadState by mainViewModel.downloads.collectAsState()
    val downloadResultState = downloadState
    val scope = rememberCoroutineScope()
    var taskUrl by rememberSaveable { mutableStateOf("") }
    var shortenedUrl by rememberSaveable { mutableStateOf("") }

    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }

    val navBar by mainViewModel.navBar.collectAsState()

    var isTaskOpen by rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(key1 = Unit) {
        vm.getAkVideoU(sid = sid, bid = bid, tid = tid, key = key, lid = lid)

    }

    val videoState by vm.akVideoU.collectAsState()


    when (val result = videoState) {
        is Response.Error -> {

            DataNotFoundScreen(
                errorMsg = result.msg,
                state = rememberMessageBarState(),
                shouldShowBackButton = true,
                onRetryClicked = {
                    vm.getAkVideoU(
                        sid = sid,
                        bid = bid,
                        tid = tid,
                        key = key,
                        lid = lid
                    )
                }) {
                onBackClick()
            }
        }

        is Response.Loading -> {
            LoadingScreen(paddingValues = PaddingValues(0.dp, 0.dp))
        }

        is Response.Success -> {
            val state = vm.state.collectAsState()
            val context = LocalContext.current
            var isPlayerReady by rememberSaveable { mutableStateOf(false) }
            val qualityList: ArrayList<Pair<String, TrackSelectionOverrides.Builder>> =
                state.value.qualityList.distinctBy { it.first }.toCollection(ArrayList())


            var lifecycle by rememberSaveable {
                mutableStateOf(Lifecycle.Event.ON_CREATE)
            }
            val lifecycleOwner = LocalLifecycleOwner.current
            var expanded by rememberSaveable {
                mutableStateOf(false)
            }
            val isVisible = state.value.isVisible
            val selectedIndex = state.value.selectedIndex
            val configuration = LocalConfiguration.current
            val isLandscape by remember(configuration.orientation) {
                mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            }
            val isInternetAccessible by mainViewModel.isInternetAccessible.collectAsState()


            BackHandler {
                if (state.value.isFullScreen) {
                    context.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    vm.onBackButtonClicked()
                } else {
                    onBackClick()
                    vm.releasePlayer()
                }
            }

            LaunchedEffect(key1 = isInternetAccessible) {
                mainViewModel.getAlertItem()
                mainViewModel.getDownloadLinks()
                mainViewModel.getNavItems()
            }

            LaunchedEffect(key1 = Unit) {
                vm.initialisePlayer(context, result.data.url)
                isPlayerReady = true
                mainViewModel.getAlertItem()
                mainViewModel.observeInternetAccessibility()
            }

            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    lifecycle = event
                }
                lifecycleOwner.lifecycle.addObserver(observer)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
            val translationY by animateDpAsState(if (isVisible) 5.dp else (-40).dp, label = "")

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                AlertSection()

                ConstraintLayout(
                    modifier = Modifier
                        .background(Color.Black),
                ) {
                    val (videoPlayer) = createRefs()
                    Box(
                        modifier = if (isLandscape) Modifier
                            .background(Color.Black)
                            .constrainAs(videoPlayer) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            } else Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
                            .background(Color.Black),
                    ) {
                        if (isPlayerReady) {
                            AndroidView(
                                factory = { ctx ->
                                    vm.playerViewBuilder(ctx)
                                },
                                update = {
                                    when (lifecycle) {
                                        Lifecycle.Event.ON_PAUSE -> {
                                            it.onPause()
                                            it.player?.pause()
                                        }

                                        Lifecycle.Event.ON_RESUME -> {
                                            it.onResume()
                                        }

                                        else -> Unit
                                    }
                                },
                            )
                        } else {
                            Text(text = "")
                        }
                        if (isVisible) {

                            IconButton(
                                onClick = {
                                    expanded = true
                                }, modifier = Modifier
                                    .padding(5.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(y = translationY)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.videosettings),
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                        }

                        if (isVisible) {
                            Row(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .align(Alignment.TopStart)
                                    .offset(y = translationY)
                            ) {
                                IconButton(onClick = {
                                    if (state.value.isFullScreen) {
                                        context.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                        vm.onBackButtonClicked()
                                    } else {
                                        onBackClick()
                                        vm.releasePlayer()
                                    }
                                }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))

                                if (state.value.isFullScreen) {
                                    Text(
                                        text = title,
                                        modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        AkPlayerAlertDialog(
                            isOpen = expanded,
                            onDismiss = { expanded = false },
                            selectedIndex = selectedIndex,
                            qualityList = qualityList
                        )
                    }
                }


                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
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
                        ),
                ) {


                    if (isTaskOpen) {
                        VideoPlayerTaskAlertDialog(
                            lifecycleOwner = lifecycleOwner,
                            onDismissRequest = { isTaskOpen = false },
                            onOkClicked = {
                                onNavigateToTaskScreen(taskUrl, shortenedUrl)
                                isTaskOpen = false
                            })
                    }


                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(8.dp)
                    )

                    SpacerHeight(dp = 10.dp)





                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp),

                        ) {

                        SpacerWidth(dp = 2.dp)

                        when (downloadResultState) {
                            is Response.Error -> {
                                Log.d(
                                    "TAG",
                                    "VideoPlayerScreen: Error in down load error = ${downloadResultState.msg}"
                                )
                            }

                            is Response.Loading -> {}
                            is Response.Success -> {

                                if (downloadResultState.data.visible == "1") {
                                    TextWithIcon(
                                        onClick = {
                                            scope.launch {
                                                val task = videoViewModel.getTask(1)
                                                val alarmItem = videoViewModel.getAlarmById(1)
                                                val ip =
                                                    withContext(Dispatchers.IO) {
                                                        videoViewModel.getIPAddress(
                                                            true
                                                        )
                                                    }

                                                if (ip == null || task == null) {
                                                    taskUrl = downloadResultState.data.task_url
                                                    shortenedUrl =
                                                        downloadResultState.data.task_final_url
                                                    isTaskOpen = true
                                                } else if (task.ipAddress != ip) {
                                                    taskUrl = downloadResultState.data.task_url
                                                    shortenedUrl =
                                                        downloadResultState.data.task_final_url
                                                    isTaskOpen = true
                                                } else {
                                                    val downloadNumber =
                                                        videoViewModel.getDownloadNumberById(1)

                                                    when {
                                                        downloadNumber == null -> {
                                                            videoViewModel.insertDownloadNumber(
                                                                DownloadNumberEntity(
                                                                    id = 1,
                                                                    numberOfDownloads = 1
                                                                )
                                                            )
                                                            Util1DM.downloadFile(
                                                                activity = context as Activity,
                                                                url = result.data.url,
                                                                secureUri = true,
                                                                askUserToInstall1DMIfNotInstalled = true,
                                                                fileName = title,
                                                                referer = null,
                                                                userAgent = null,
                                                                cookies = null

                                                            )
                                                        }

                                                        downloadNumber.numberOfDownloads < downloadResultState.data.download_limit.toInt() -> {
                                                            videoViewModel.insertDownloadNumber(
                                                                DownloadNumberEntity(
                                                                    id = 1,
                                                                    numberOfDownloads = downloadNumber.numberOfDownloads + 1
                                                                )
                                                            )
                                                            Util1DM.downloadFile(
                                                                activity = context as Activity,
                                                                url = result.data.url,
                                                                secureUri = true,
                                                                askUserToInstall1DMIfNotInstalled = true,
                                                                fileName = title,
                                                                referer = null,
                                                                userAgent = null,
                                                                cookies = null

                                                            )
                                                        }

                                                        alarmItem != null && alarmItem.timeToTriggerAt > System.currentTimeMillis() -> {
                                                            mainViewModel.showToast(
                                                                context,
                                                                "You have reached today's download limit, you can download again after ${alarmItem.timeToTriggerAt.toReadableDate()}"
                                                            )
                                                        }

                                                        else -> {
                                                            if (alarmItem != null) {
                                                                videoViewModel.deleteAlarmItem(
                                                                    alarmItem
                                                                )
                                                            }
                                                            videoViewModel.deleteDownloadNumber(
                                                                downloadNumber
                                                            )
                                                            videoViewModel.deleteTask(task)
                                                            isTaskOpen = true
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        text = "Download",
                                        icon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_download_24),
                                                contentDescription = "",
                                                modifier = Modifier.size(90.dp)
                                            )
                                        }
                                    )


                                    when (val navResult = navBar) {
                                        is ResponseTwo.Error -> {}
                                        is ResponseTwo.Loading -> {}
                                        is ResponseTwo.Nothing -> {}
                                        is ResponseTwo.Success -> {

                                            TextWithIcon(
                                                onClick = {
                                                    mainViewModel.openTelegram(
                                                        context,
                                                        url = navResult.data.telegram
                                                    )
                                                },
                                                text = "Telegram",
                                                icon = {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.telegram),
                                                        contentDescription = "",
                                                        modifier = Modifier.size(90.dp)
                                                    )
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
        }
    }
}