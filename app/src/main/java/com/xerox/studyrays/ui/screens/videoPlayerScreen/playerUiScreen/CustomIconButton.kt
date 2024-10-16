package com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomIconButton(
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    reducedIconSize: Boolean = false,
    bigIcon: Boolean = false,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .size(54.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(if(bigIcon) 45.dp else 32.dp)
                .padding(if (reducedIconSize) 2.dp else 0.dp),
            tint = Color.White
        )
    }
}

@Composable
fun CustomIconButton2(
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    reducedIconSize: Boolean = false,
    bigIcon: Boolean = false,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
//            .padding(horizontal = 8.dp)
            .size(54.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(if(bigIcon) 45.dp else 32.dp)
                .padding(if (reducedIconSize) 2.dp else 0.dp),
            tint = Color.White
        )
    }
}