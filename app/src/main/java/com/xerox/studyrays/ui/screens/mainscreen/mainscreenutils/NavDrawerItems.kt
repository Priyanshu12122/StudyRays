package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun NavDrawerItems(
    drawerItem: DrawerItem,
    index: Int,
    onOpenGmailClicked: () -> Unit,
    onOpenUrlClicked: () -> Unit,
    onOpenTelegramClicked: () -> Unit,
    onShareAppClicked: () -> Unit,

    ) {

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = drawerItem.header,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        NavigationDrawerItem(
            label = { Text(text = drawerItem.title) },
            selected = false,
            onClick = {
                when (index) {
                    0 -> {

                        onOpenGmailClicked()

                    }

                    1 -> {
                        onOpenUrlClicked()

                    }

                    2 -> {
                        onOpenTelegramClicked()
                    }

                    3 -> {
                        onShareAppClicked()

                    }
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = drawerItem.icon),
                    contentDescription = "",
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    modifier = Modifier.size(25.dp)
                )
            }
        )

        Divider()

    }
}