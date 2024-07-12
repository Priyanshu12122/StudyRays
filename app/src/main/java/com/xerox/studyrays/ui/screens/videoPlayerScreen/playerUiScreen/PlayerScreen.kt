package com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoViewModel
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    vm: VideoViewModel = hiltViewModel(),
    isPlayerReady: Boolean,
    position: Long,
    onSettingsClicked: () -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current

    if (isPlayerReady) {

        val exoPlayer = vm.getPlayer()!!

        val mediaPlayerManager = rememberMediaPlayerManager(player = exoPlayer)
        val playerState = mediaPlayerManager.playerState
        // Just to restart the delay to hide the main UI if the user performs some action (play/pause, for example)
        var lastPlayerActionTimeMillis by remember { mutableLongStateOf(0L) }
        // Hides the main UI after the time
        LaunchedEffect(
            key1 = playerState.showMainUi,
            key2 = lastPlayerActionTimeMillis
        ) {
            delay(5000L)
            mediaPlayerManager.onPlayerAction(PlayerAction.ChangeShowMainUi(false))
        }
        Box(modifier = Modifier.fillMaxSize()) {
            PlayerView(
                mediaPlayerState = mediaPlayerManager,
                position = position,
                onPlayerViewClick = {
                    val showMainUi = !playerState.showMainUi
                    if (showMainUi) {
                        Utils.showSystemBars(context)
                    } else {
                        Utils.hideSystemBars(context)
                    }
                    mediaPlayerManager.onPlayerAction(PlayerAction.ChangeShowMainUi(showMainUi))
                }
            )

            PlayerLayout(
                playerState = playerState,
                onPlayerAction = { action ->
                    mediaPlayerManager.onPlayerAction(action)
                    lastPlayerActionTimeMillis = System.currentTimeMillis()
                },
                showMainUi = playerState.showMainUi,
                onSettingsClicked = {
                    onSettingsClicked()
                },
                onBackClick = {
                    onBackClick()
                }
            )
        }
        LockScreenOrientation(isLandscapeMode = playerState.isLandscapeMode)
    }


}

@Composable
private fun PlayerLayout(
    playerState: PlayerState,
    onPlayerAction: (PlayerAction) -> Unit,
    showMainUi: Boolean,
    onSettingsClicked: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()
        .background(if (showMainUi)  Color.Black.copy(alpha = 0.6f) else Color.Transparent )) {
        if (playerState.isStateBuffering) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(60.dp),
            )
        }

        AnimatedVisibility(
            visible = showMainUi,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            SecondaryControlButtons(
                resizeMode = playerState.resizeMode,
                onPlayerAction = onPlayerAction,
                onSettingsClicked = {
                    onSettingsClicked()
                },
                onBackClicked = {
                    onBackClick()
                }
            )
        }

        AnimatedVisibility(
            visible = showMainUi,
            modifier = Modifier.align(Alignment.Center)
        ) {
            MainControlButtons(
                isSeekForwardButtonAvailable = playerState.isSeekForwardButtonAvailable,
                isSeekBackButtonAvailable = playerState.isSeekBackButtonAvailable,
                playbackState = playerState.playbackState,
                isPlaying = playerState.isPlaying,
                onPlayerAction = onPlayerAction,
            )
        }

        AnimatedVisibility(
            visible = showMainUi,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomContent(
                playerState = playerState,
                onPlayerAction = onPlayerAction
            )
        }
    }
}


@Composable
private fun BottomContent(
    playerState: PlayerState,
    onPlayerAction: (PlayerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.safeDrawingPadding()) {
        ProgressContent(
            currentPlaybackPosition = playerState.currentPlaybackPosition,
            currentBufferedPercentage = playerState.bufferedPercentage,
            videoDuration = playerState.videoDuration,
            onPlayerAction = onPlayerAction,
            isLandscapeMode = playerState.isLandscapeMode
        )
//        Spacer(Modifier.height(4.dp))

//        Spacer(Modifier.height(4.dp))
    }
}