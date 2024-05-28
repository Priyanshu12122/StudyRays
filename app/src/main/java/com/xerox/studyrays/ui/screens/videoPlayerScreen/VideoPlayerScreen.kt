package com.xerox.studyrays.ui.screens.videoPlayerScreen

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.xerox.studyrays.R
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.AlertSection

@Composable
fun VideoPlayerScreen(
    vm: VideoViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    title: String,
    embedCode: String,
    url: String,
    id: String,
    bname: String,
    onBackClicked: () -> Unit,
    ) {


    val state = vm.state.collectAsState()

//    val url = "https://d1d34p8vz63oiq.cloudfront.net/e6dc3dc2-71c8-447d-a5d1-48a9d1e7ccf9/master.mpd"
    val qualityList: ArrayList<Pair<String, TrackSelectionOverrides.Builder>> =
        state.value.qualityList

    val baseLink = "https://extractapi.xyz/app/drm.php?v="
    val licenseUrl = baseLink + url
    val context = LocalContext.current
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
    var isPlayerReady by rememberSaveable { mutableStateOf(false) }

    BackHandler {
        if (state.value.isFullScreen) {
            context.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            vm.onBackButtonClicked()
        } else {
            onBackClicked()
            vm.releasePlayer()
        }
    }

    LaunchedEffect(key1 = Unit) {
        vm.initialisePlayer(context, url, licenseUrl)
        isPlayerReady = true
        mainViewModel.getAlertItem()
        mainViewModel.observeInternetAccessibility()
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

        ConstraintLayout(
            modifier = Modifier
                .background(if (isSystemInDarkTheme()) Color.Black else Color.Black),
        ) {
            val (videoPlayer) = createRefs()
            Box(
                modifier = if (isLandscape) Modifier.constrainAs(videoPlayer) {
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
                                onBackClicked()
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

                PlayerAlertDialog(
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
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )

        }


//        if (id != "" || bname != "") {
//            CommentsScreen(id = id, bname = bname)
//        }
    }
}