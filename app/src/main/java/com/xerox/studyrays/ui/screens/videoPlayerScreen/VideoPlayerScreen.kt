package com.xerox.studyrays.ui.screens.videoPlayerScreen

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.util.Log
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.xerox.studyrays.downloadManager.idm.Util1DM
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.AlertSection
import com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen.PlayerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(
    vm: VideoViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    title: String,
    embedCode: String,
    url: String,
    topicSlug: String,
    videoId: String,
    id: String,
    imageUrl: String,
    createdAt: String,
    duration: String,
    bname: String,
    isWhat: String,
    onNavigateToTaskScreen: (String, String) -> Unit,
    onBackClicked: () -> Unit,
) {


    val state = vm.state.collectAsState()
    val scope = rememberCoroutineScope()

//    val url = "https://d1d34p8vz63oiq.cloudfront.net/e6dc3dc2-71c8-447d-a5d1-48a9d1e7ccf9/master.mpd"
    val context = LocalContext.current

    var downloadId by rememberSaveable { mutableStateOf("") }
    try {
        val startIndex = url.indexOf(".net/") + 5
        val endIndex = url.indexOf("/master.mpd")
        downloadId = url.substring(startIndex, endIndex)
    } catch (e: Exception) {
        mainViewModel.showToast(context, "Error occurred, error = ${e.localizedMessage}")
    }
    val qualityList: ArrayList<Pair<String, TrackSelectionOverrides.Builder>> =
        state.value.qualityList

//    val baseLink = "https://extractapi.xyz/app/drm.php?v="
//    val baseLink = "https://extractapi.xyz/master.php?v=$url"
    var baseLink by rememberSaveable {
        mutableStateOf("")
    }
//    val licenseUrl = baseLink + url

    var lifecycle by rememberSaveable {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    var position by rememberSaveable {
        mutableLongStateOf(0L)
    }


    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var isTaskOpen by rememberSaveable { mutableStateOf(false) }

    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val isVisible = state.value.isVisible
    val selectedIndex = state.value.selectedIndex
    val configuration = LocalConfiguration.current
    val isLandscape by remember(configuration.orientation) {
        mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
    }
    var isPlayerReady by rememberSaveable { mutableStateOf(false) }
    var isAddingNotes by rememberSaveable { mutableStateOf(false) }
    var downloadUrl by rememberSaveable { mutableStateOf("") }


    var taskUrl by rememberSaveable { mutableStateOf("") }
    var shortenedUrl by rememberSaveable { mutableStateOf("") }



    BackHandler {
        if (state.value.isFullScreen) {
            context.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            vm.onBackButtonClicked()
        } else {
            onBackClicked()
            vm.releasePlayer()
        }
    }

//    "https://extractapi.xyz/master.php?v=https://d1d34p8vz63oiq.cloudfront.net/8a165053-745c-42a9-8567-fd2de29948fa/master.mpd"

    LaunchedEffect(key1 = Unit) {
//        vm.initialisePlayer(
//            context,
//            baseLink,
//            videoId
//        )
//        isPlayerReady = true
        Log.d("TAG", "VideoPlayerScreen: $id  $topicSlug")
        mainViewModel.getAlertItem()
        mainViewModel.observeInternetAccessibility()
        mainViewModel.checkIfWatchLaterSaved(videoId)
        mainViewModel.getDownloadLinks()
        mainViewModel.getNavItems()
        mainViewModel.checkStartDestination()
        val video = vm.getVideoProgressById(videoId)
        position = video?.position ?: 0L
    }
    val isInternetAccessible by mainViewModel.isInternetAccessible.collectAsState()

    LaunchedEffect(key1 = isInternetAccessible) {
        mainViewModel.getAlertItem()
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

        if (isTaskOpen) {
            VideoPlayerTaskAlertDialog(
                lifecycleOwner = lifecycleOwner,
                onDismissRequest = { isTaskOpen = false },
                onOkClicked = {
                    onNavigateToTaskScreen(taskUrl, shortenedUrl)
                    isTaskOpen = false
                })
        }

        if (isBottomSheetOpen) {

            if (downloadId == "") {
                mainViewModel.showToast(
                    context,
                    "Sorry this video can't be downloaded, try downloading other videos"
                )
            } else {
                VideoQualityBottomSheet(
                    sheetState = rememberModalBottomSheetState(),
                    onClick = {
                        Util1DM.downloadFile(
                            activity = context as Activity,
                            url = "$downloadUrl$downloadId&quality=$it",
                            secureUri = true,
                            askUserToInstall1DMIfNotInstalled = true,
                            fileName = title,
                            referer = null,
                            userAgent = null,
                            cookies = null

                        )
                        isBottomSheetOpen = false
                    },
                    onDismissRequest = {
                        isBottomSheetOpen = false
                    }
                )

            }

        }

        ConstraintLayout(
            modifier = Modifier
                .background(if (isSystemInDarkTheme()) Color.Black else Color.Black),
        ) {
            val (videoPlayer) = createRefs()
            Box(
                modifier = if (isLandscape) Modifier
                    .constrainAs(videoPlayer) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                    } else Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .background(if (isSystemInDarkTheme()) Color.Black else Color.Black),
            ) {
                if (isPlayerReady) {
                    if (url != "") {
//                        AndroidView(
//                            factory = { ctx ->
////                                LayoutInflater.from(ctx).inflate(R.layout.layout_exoplayer_control_views, null, false) as ConstraintLayout
//                                vm.playerViewBuilder(ctx)
//                            },
//                            update = {
//                                when (lifecycle) {
//                                    Lifecycle.Event.ON_PAUSE -> {
//                                        it.onPause()
//                                        it.player?.pause()
//                                    }
//
//                                    Lifecycle.Event.ON_RESUME -> {
//                                        it.onResume()
//                                    }
//
//                                    else -> Unit
//                                }
//                            }
//                        )


                        PlayerScreen(isPlayerReady = isPlayerReady, onSettingsClicked = {
                            expanded = true
                        }, position = position,
                            onBackClick = {
                                if (state.value.isFullScreen) {
                                    context.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                    vm.onBackButtonClicked()
                                } else {
                                    onBackClicked()
                                    vm.releasePlayer()
                                }
                            })

                    } else {
                        val code = embedCode.substringAfter("/embed/")
                        AndroidView(
                            factory = { ctx ->
                                YouTubePlayerView(ctx)
                                    .apply {
                                        lifecycleOwner.lifecycle.addObserver(this)

                                        addYouTubePlayerListener(object :
                                            AbstractYouTubePlayerListener() {
                                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                                youTubePlayer.loadVideo(code, 0f)
                                            }
                                        }
                                        )
                                        addFullscreenListener(object : FullscreenListener {
                                            override fun onEnterFullscreen(
                                                fullscreenView: View,
                                                exitFullscreen: () -> Unit,
                                            ) {
                                            }

                                            override fun onExitFullscreen() {
                                            }

                                        })
                                    }
                            }
                        )
                    }
                } else {
                    Text(text = "")
                }

//                if (isVisible) {
//                    Row(
//                        modifier = Modifier
//                            .padding(5.dp)
//                            .align(Alignment.TopStart)
//                            .offset(y = translationY)
//                    ) {
//                        IconButton(onClick = {
//                            if (state.value.isFullScreen) {
//                                context.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                                vm.onBackButtonClicked()
//                            } else {
//                                onBackClicked()
//                                vm.releasePlayer()
//                            }
//                        }
//                        ) {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                                contentDescription = "",
//                                tint = Color.White
//                            )
//                        }
////                        Spacer(modifier = Modifier.width(10.dp))
////
////                        if (state.value.isFullScreen) {
////                            Text(
////                                text = title,
////                                modifier = Modifier.padding(top = 10.dp, start = 5.dp),
////                                maxLines = 2,
////                                overflow = TextOverflow.Ellipsis,
////                                color = Color.White
////                            )
////                        }
//
//                    }
//                }

                PlayerAlertDialog(
                    isOpen = expanded,
                    onDismiss = { expanded = false },
                    selectedIndex = selectedIndex,
                    qualityList = qualityList
                )
            }
        }

//        if (isAddingNotes) {
//            NotesScreen(
//                onBackClicked = {
//                    isAddingNotes = false
//                },
//                onCrossClicked = {
//                    isAddingNotes = false
//                },
//                position = position,
//                videoId = videoId
//            )
//        } else {
        ContentsScreen(
            title = title,
            imageUrl = imageUrl,
            createdAt = createdAt,
            duration = duration,
            videoId = videoId,
            id = id,
            url = url,
            embedCode = embedCode,
            isWhat = isWhat,
            topicSlug = topicSlug,
            position = position,
            onTaskUrlChanged = {
                taskUrl = it
            },
            onShortenedUrlChanged = {
                shortenedUrl = it
            },
            onDownloadUrlChanged = {
                downloadUrl = it
            },
            onIsTaskOpenChanged = {
                isTaskOpen = it
            },
            onIsPlayerReadyChanged = {
                isPlayerReady = it
            },
            onBottomSheetChanged = {
                isBottomSheetOpen = it
            },
            onAddNoteClicked = {
                isAddingNotes = true
            },
            onNoteClick = {
                vm.seekTo(it)
            },
            onSlideClicked = {
                vm.seekTo(it)
            }
        )

//        }
    }
}