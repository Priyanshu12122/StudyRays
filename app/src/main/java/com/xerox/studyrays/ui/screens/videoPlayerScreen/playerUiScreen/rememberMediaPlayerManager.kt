package com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.exoplayer2.ExoPlayer
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoViewModel
import kotlinx.coroutines.delay

@Composable
fun rememberMediaPlayerManager(player: ExoPlayer,vm: VideoViewModel = hiltViewModel()): MediaPlayerManager {
    val playerStateHolder: PlayerStateHolder = viewModel {
        val savedStateHandle = createSavedStateHandle()
        PlayerStateHolder(savedStateHandle)
    }

    val mediaPlayerManager = remember {
        MediaPlayerManager(
            player = player,
            playerStateHolder = playerStateHolder,
            vm = vm
        )
    }
    val playerState = mediaPlayerManager.playerState

    // Change the current playback position only while the video is playing
    LaunchedEffect(playerState.isPlaying) {
        while (playerState.isPlaying) {
            mediaPlayerManager.onPlayerAction(
                PlayerAction.ChangeCurrentPlaybackPosition(player.currentPosition)
            )
            delay(300L)
        }
    }

    // Change the buffered percentage only while displaying the main UI
    LaunchedEffect(playerState.showMainUi) {
        while (playerState.showMainUi) {
            mediaPlayerManager.onPlayerAction(
                PlayerAction.ChangeBufferedPercentage(player.bufferedPercentage)
            )
            delay(300L)
        }
    }

    return mediaPlayerManager
}