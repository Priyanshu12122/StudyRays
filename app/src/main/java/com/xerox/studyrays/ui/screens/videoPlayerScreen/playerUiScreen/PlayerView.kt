package com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoViewModel

@Composable
fun PlayerView(
    mediaPlayerState: MediaPlayerManager,
    onPlayerViewClick: () -> Unit,
    modifier: Modifier = Modifier,
    position: Long,
    vm: VideoViewModel = hiltViewModel(),
) {
//    val playerView = remember {
//        PlayerView(context).apply {
//            this.useController = false
//            this.player = exoPlayer
//        }
//    }

    val state by vm.state.collectAsState()

    AndroidView(
        factory = {
            vm.playerViewBuilder(it, position)
        },
        update = {
            it.resizeMode = mediaPlayerState.playerState.resizeMode.value
        },
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF111318))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onPlayerViewClick.invoke() }
    )

    ExoPlayerLifecycleController(
        playerView = state.playerView,
        onStartOrResume = {
            if (mediaPlayerState.playerState.isPlaying) {
                vm.play()
            }
        },
        onPauseOrStop = {
            vm.pause()
        },
        onDispose = {
            mediaPlayerState.releasePlayer()
        }
    )
}