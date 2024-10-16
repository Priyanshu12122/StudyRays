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
import com.xerox.studyrays.utils.SpacerWidth

@Composable
fun MainControlButtons(
    isSeekForwardButtonAvailable: Boolean,
    isSeekBackButtonAvailable: Boolean,
    playbackState: PlaybackState,
    isPlaying: Boolean,
    onPlayerAction: (PlayerAction) -> Unit,
    modifier: Modifier = Modifier,
) {

    val padding = 20.dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        CustomIconButton(
            iconResId = R.drawable.back,
            enabled = isSeekBackButtonAvailable,
            onClick = {
                onPlayerAction.invoke(PlayerAction.SeekBack)
            },
            bigIcon = true
        )

        SpacerWidth(dp = padding)
//        CustomIconButton(
//            iconResId = R.drawable.ic_exo_icon_rewind,
//            onClick = {
//                onPlayerAction.invoke(PlayerAction.Previous)
//            }
//        )

        val centerIcon = if (playbackState == PlaybackState.Ended) {
            R.drawable.baseline_replay_24
        } else if (isPlaying) {
            R.drawable.baseline_pause_24
        } else {
            R.drawable.baseline_play_arrow_24
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

        SpacerWidth(dp = padding)

        CustomIconButton(
            iconResId = R.drawable.next,
            enabled = isSeekForwardButtonAvailable,
            onClick = {
                onPlayerAction.invoke(PlayerAction.SeekForward)
            },
            bigIcon = true
        )
    }
}