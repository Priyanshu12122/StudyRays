package com.xerox.studyrays.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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