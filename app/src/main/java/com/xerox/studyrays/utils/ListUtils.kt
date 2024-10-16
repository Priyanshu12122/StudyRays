package com.xerox.studyrays.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.xerox.studyrays.R
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.DrawerItem
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.MainScreenItem
import com.xerox.studyrays.ui.theme.AquaBlue
import com.xerox.studyrays.ui.theme.Beige1
import com.xerox.studyrays.ui.theme.Beige3
import com.xerox.studyrays.ui.theme.BlueViolet3
import com.xerox.studyrays.ui.theme.ButtonBlue
import com.xerox.studyrays.ui.theme.DarkerButtonBlue
import com.xerox.studyrays.ui.theme.LightGreen1
import com.xerox.studyrays.ui.theme.LightGreen3
import com.xerox.studyrays.ui.theme.LightRed
import com.xerox.studyrays.ui.theme.OrangeYellow2

val navBarList = listOf(
    DrawerItem(
        R.drawable.message,
        "Report Bugs",
        "Feedback"
    ),
    DrawerItem(
        R.drawable.supportus,
        "Official Website",
        "Support Us"
    ),
    DrawerItem(
        R.drawable.telegram,
        "Telegram",
        "Join Telegram"
    ),
    DrawerItem(
        R.drawable.share,
        header = "Share App ",
        title = "Share"
    )
)

@Composable
fun mainScreenItemsList(): List<MainScreenItem> {
    val list2 = listOf(
        MainScreenItem(
            isRaw = false,
            title = "Physics Wallah",
            raw = if (isSystemInDarkTheme()) R.drawable.pwdarkk else R.drawable.pwlightt,
            colors = listOf(
                LightGreen1, BlueViolet3
            )
        ),
        MainScreenItem(
            isRaw = true,
            title = "Khazana",
            raw = R.raw.khazanaaa,
            colors = listOf(
                BlueViolet3, LightGreen3
            )
        ),
        MainScreenItem(
            isRaw = false,
            title = "Apni Kaksha",
            raw = if (isSystemInDarkTheme()) R.drawable.akdarkkk else R.drawable.aklightt,
            colors = listOf(OrangeYellow2, AquaBlue)
        ),
        MainScreenItem(
            isRaw = true,
            title = "Favourite Batches",
            raw = R.raw.favourite,
            colors = listOf(
                AquaBlue, OrangeYellow2
            )
        ),
        MainScreenItem(
            isRaw = true,
            title = "Update/add batches",
            raw = R.raw.update,
            colors = listOf(
                Beige1, DarkerButtonBlue
            )
        ),
        MainScreenItem(
            isRaw = true,
            title = "Follow on Telegram",
            raw = R.raw.telegramm,
            colors = listOf(
                DarkerButtonBlue, Beige3
            )
        )
    )
    return list2
}