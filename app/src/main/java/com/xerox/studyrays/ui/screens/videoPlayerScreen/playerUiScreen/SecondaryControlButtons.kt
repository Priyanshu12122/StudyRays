package com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.R
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoViewModel

@Composable
fun SecondaryControlButtons(
    modifier: Modifier = Modifier,
    resizeMode: ResizeMode,
    vm: VideoViewModel = hiltViewModel(),
    onPlayerAction: (PlayerAction) -> Unit,
    onSettingsClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {

    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {

        IconButton(onClick = {
            onBackClicked()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                contentDescription = ""
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            CustomIconButton2(
                iconResId = R.drawable.baseline_aspect_ratio_24,
                onClick = {
                    val newResizeMode = ResizeMode.getNewResizeMode(resizeMode)
                    onPlayerAction.invoke(PlayerAction.ChangeResizeMode(newResizeMode))
                    vm.showToast(context, "Scale Mode: " + resizeMode.name)
                }
            )

            CustomIconButton2(
                iconResId = R.drawable.videosettings,
                onClick = {  onSettingsClicked() },
                modifier = Modifier
            )


            Spacer(Modifier.width(1.dp))
        }
    }


}

