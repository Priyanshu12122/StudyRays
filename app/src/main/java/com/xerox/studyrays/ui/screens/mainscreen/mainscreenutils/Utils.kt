package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.ui.graphics.Color

data class MainScreenItem(
    val title: String,
    val isRaw: Boolean,
    val raw: Int,
    val colors: List<Color>
)

data class DrawerItem(
    val icon: Int,
    val title: String,
    val header: String
)
