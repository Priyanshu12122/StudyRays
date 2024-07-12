package com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xerox.studyrays.R

@Composable
fun MainControlButtons(
    isSeekForwardButtonAvailable: Boolean,
    isSeekBackButtonAvailable: Boolean,
    playbackState: PlaybackState,
    isPlaying: Boolean,
    onPlayerAction: (PlayerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        CustomIconButton(
            iconResId = R.drawable.baseline_replay_5_24,
            enabled = isSeekBackButtonAvailable,
            onClick = {
                onPlayerAction.invoke(PlayerAction.SeekBack)
            },
            bigIcon = true
        )
//        CustomIconButton(
//            iconResId = R.drawable.ic_exo_icon_rewind,
//            onClick = {
//                onPlayerAction.invoke(PlayerAction.Previous)
//            }
//        )

        val centerIcon = if (playbackState == PlaybackState.Ended) {
            R.drawable.baseline_replay_circle_filled_24
        } else if (isPlaying) {
            R.drawable.baseline_pause_circle_24
        } else {
            R.drawable.baseline_play_circle_24
        }
        CustomIconButton(
            iconResId = centerIcon,
            onClick = {
                onPlayerAction.invoke(PlayerAction.PlayOrPause)
            },
            bigIcon = true
        )

//        CustomIconButton(
//            iconResId = R.drawable.ic_exo_icon_play,
//            enabled = isNextButtonAvailable,
//            onClick = {
//                onPlayerAction.invoke(PlayerAction.Next)
//            }
//        )

        CustomIconButton(
            iconResId = R.drawable.round_forward_10_24,
            enabled = isSeekForwardButtonAvailable,
            onClick = {
                onPlayerAction.invoke(PlayerAction.SeekForward)
            },
            bigIcon = true
        )
    }
}