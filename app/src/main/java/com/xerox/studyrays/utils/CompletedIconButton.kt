package com.xerox.studyrays.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xerox.studyrays.R
import com.xerox.studyrays.ui.theme.LightGreen4

@Composable
fun CompletedIconButton() {
    Box(
        modifier = Modifier
            .size(15.dp)
            .clip(CircleShape)
            .background(LightGreen4),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = Icons.Default.Check, contentDescription = "", tint = Color.Black)
    }
}

@Composable
fun WatchLaterButton(
    isSaved: Boolean,
    onClick: () -> Unit,
) {
    IconButton(onClick = {
        onClick()
    }) {
        Image(
            painter = painterResource(id = if (isSaved) R.drawable.starfilled else R.drawable.staroutlined),
            contentDescription = null,
            modifier = Modifier.size(15.dp),
            colorFilter = (if (!isSaved && isSystemInDarkTheme()) Color.White else  null)?.let { ColorFilter.tint(color = it) }
        )
    }
}