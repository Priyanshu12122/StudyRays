package com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xerox.studyrays.R
import com.xerox.studyrays.utils.SpacerWidth

@Composable
fun ProgressContent(
    currentPlaybackPosition: Long,
    currentBufferedPercentage: Int,
    videoDuration: Long,
    isLandscapeMode: Boolean,
    onPlayerAction: (PlayerAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var isChangingPosition by remember { mutableStateOf(false) }
    var temporaryPlaybackPosition by remember { mutableLongStateOf(0L) }
    val sliderValue = remember(currentPlaybackPosition, isChangingPosition) {
        if (isChangingPosition) temporaryPlaybackPosition else currentPlaybackPosition
    }

    val formattedCurrentVideoTime = remember(sliderValue) {
        Utils.formatVideoDuration(sliderValue)
    }
    val formattedVideoDuration = remember(videoDuration) {
        Utils.formatVideoDuration(videoDuration)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
    ) {
        Text(
            text = formattedCurrentVideoTime,
            color = Color.White,
            fontSize = 14.sp,
        )
        Spacer(Modifier.width(6.dp))
        Box(modifier = Modifier.weight(8.5f)) {
            Slider(
                value = currentBufferedPercentage.toFloat(),
                enabled = false,
                onValueChange = {},
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    disabledThumbColor = Color.Transparent,
                    disabledActiveTrackColor = MaterialTheme.colorScheme.primaryContainer.copy(0.6f)
                )
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = sliderValue.toFloat(),
                onValueChange = { value ->
                    temporaryPlaybackPosition = value.toLong()
                    isChangingPosition = true
                    onPlayerAction.invoke(
                        PlayerAction.ChangeCurrentPlaybackPosition(temporaryPlaybackPosition)
                    )
                },
                onValueChangeFinished = {
                    isChangingPosition = false
                    onPlayerAction.invoke(PlayerAction.SeekTo(temporaryPlaybackPosition))
                    temporaryPlaybackPosition = 0
                },
                valueRange = 0f..videoDuration.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primaryContainer,
                    activeTickColor = MaterialTheme.colorScheme.primaryContainer,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
        Text(
            text = formattedVideoDuration,
            color = Color.White,
            fontSize = 14.sp,
        )
        Spacer(Modifier.width(1.dp))
        CustomIconButton2(
            iconResId = if (isLandscapeMode) R.drawable.round_fullscreen_exit_24 else R.drawable.round_fullscreen_24,
            onClick = {
                onPlayerAction.invoke(PlayerAction.ChangeIsLandscapeMode(!isLandscapeMode))

            },
        )
        SpacerWidth(dp = 1.dp)

    }
}