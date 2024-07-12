package com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoViewModel
import com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen.PlaybackState.Companion.toPlaybackState

class MediaPlayerManager(
    private val player: ExoPlayer,
    private val playerStateHolder: PlayerStateHolder,
    private val vm: VideoViewModel,
) {
    val playerState: PlayerState
        get() = playerStateHolder.playerState

    private val listener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            playerStateHolder.onPlayerStateChange(
                playerState.copy(
                    playbackState = player.playbackState.toPlaybackState(),
                    isNextButtonAvailable = isNextButtonAvailable(),
                    isSeekForwardButtonAvailable = isSeekButtonAvailable(isSeekForward = true),
                    isSeekBackButtonAvailable = isSeekButtonAvailable(isSeekForward = false),
                )
            )
            val videoDuration = player.duration.coerceAtLeast(0L)
            if (videoDuration != 0L) {
                playerStateHolder.onPlayerStateChange(playerState.copy(videoDuration = videoDuration))
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            if (playerState.currentMediaItemIndex != player.currentMediaItemIndex) {
                playerStateHolder.onPlayerStateChange(
                    playerState.copy(
                        currentMediaItemIndex = player.currentMediaItemIndex,
                        videoDuration = player.duration.coerceAtLeast(0L)
                    )
                )
            }
        }

        override fun onPlayWhenReadyChanged(
            playWhenReady: Boolean,
            @Player.PlayWhenReadyChangeReason reason: Int,
        ) {
            playerStateHolder.onPlayerStateChange(playerState.copy(playWhenReady = playWhenReady))
        }
    }

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        player.addListener(listener)
        player.playWhenReady = playerState.playWhenReady
        player.repeatMode = playerState.repeatMode.value
        player.seekTo(playerState.currentMediaItemIndex, playerState.currentPlaybackPosition)
        player.setPlaybackSpeed(playerState.playbackSpeed)
    }

    fun releasePlayer() {
        player.release()
        player.removeListener(listener)
    }

    fun onPlayerAction(action: PlayerAction) {
        when (action) {
            is PlayerAction.PlayOrPause -> {
                playOrPause()
            }

            is PlayerAction.Next -> {
                restartCurrentPlaybackPosition()
                player.seekToNext()
            }

            is PlayerAction.Previous -> {
                restartCurrentPlaybackPosition()
                player.seekToPrevious()
            }

            is PlayerAction.SeekForward -> {
                val updatedPlaybackPosition = (player.currentPosition + playerState.seekIncrementMs)
                    .coerceAtMost(playerState.videoDuration)
                playerStateHolder.onPlayerStateChange(
                    playerState.copy(currentPlaybackPosition = updatedPlaybackPosition)
                )
                player.seekTo(updatedPlaybackPosition)
            }

            is PlayerAction.SeekBack -> {
                val updatedPlaybackPosition = (player.currentPosition - playerState.seekIncrementMs)
                    .coerceAtLeast(0L)
                playerStateHolder.onPlayerStateChange(
                    playerState.copy(currentPlaybackPosition = updatedPlaybackPosition)
                )
                player.seekTo(updatedPlaybackPosition)
            }

            is PlayerAction.SeekTo -> {
                val positionMs = action.positionMs
                player.seekTo(positionMs)
                playerStateHolder.onPlayerStateChange(playerState.copy(currentPlaybackPosition = positionMs))
            }

            is PlayerAction.ChangeShowMainUi -> {
                playerStateHolder.onPlayerStateChange(playerState.copy(showMainUi = action.showMainUi))
                vm.changeVisibleState(action.showMainUi)
            }

            is PlayerAction.ChangeCurrentPlaybackPosition -> {
                playerStateHolder.onPlayerStateChange(
                    playerState.copy(currentPlaybackPosition = action.currentPlaybackPosition)
                )
            }

            is PlayerAction.ChangeBufferedPercentage -> {
                playerStateHolder.onPlayerStateChange(
                    playerState.copy(bufferedPercentage = action.bufferedPercentage)
                )
            }

            is PlayerAction.ChangeResizeMode -> {
                playerStateHolder.onPlayerStateChange(playerState.copy(resizeMode = action.resizeMode))
            }

            is PlayerAction.ChangeRepeatMode -> {
                if (isRepeatModeAvailable()) {
                    val repeatMode = action.repeatMode
                    player.repeatMode = repeatMode.value
                    playerStateHolder.onPlayerStateChange(playerState.copy(repeatMode = repeatMode))
                }
            }

            is PlayerAction.ChangePlaybackSpeed -> {
                player.setPlaybackSpeed(action.playbackSpeed)
                playerStateHolder.onPlayerStateChange(
                    playerState.copy(playbackSpeed = action.playbackSpeed)
                )
            }

            is PlayerAction.ChangeIsLandscapeMode -> {
                playerStateHolder.onPlayerStateChange(
                    playerState.copy(isLandscapeMode = action.isLandscapeMode)
                )
                vm.changeFullScreenState(action.isLandscapeMode)
            }
        }
    }

    private fun playOrPause() {
        if (playerState.isPlaying && playerState.playbackState != PlaybackState.Ended) {
            player.pause()
            playerStateHolder.onPlayerStateChange(playerState.copy(isPlaying = false))
            return
        }
        if (playerState.playbackState == PlaybackState.Ended) {
            restartCurrentPlaybackPosition()
            player.seekTo(0L)
        }
        player.play()
        playerStateHolder.onPlayerStateChange(playerState.copy(isPlaying = true))
    }

    private fun restartCurrentPlaybackPosition() {
        playerStateHolder.onPlayerStateChange(playerState.copy(currentPlaybackPosition = 0L))
    }

    private fun isRepeatModeAvailable(): Boolean {
        return player.isCommandAvailable(Player.COMMAND_SET_REPEAT_MODE)
    }

    private fun isNextButtonAvailable(): Boolean {
        return player.isCommandAvailable(Player.COMMAND_SEEK_TO_NEXT)
    }

    private fun isSeekButtonAvailable(isSeekForward: Boolean): Boolean {
        val command = if (isSeekForward) {
            Player.COMMAND_SEEK_FORWARD
        } else {
            Player.COMMAND_SEEK_BACK
        }
        return player.isCommandAvailable(command)
    }
}