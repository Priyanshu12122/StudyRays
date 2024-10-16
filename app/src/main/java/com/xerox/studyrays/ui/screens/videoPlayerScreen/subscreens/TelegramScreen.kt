package com.xerox.studyrays.ui.screens.videoPlayerScreen.subscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.xerox.studyrays.ui.leaderBoard.user.utils.CButton

@Composable
fun TelegramScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CButton(
            text = "Follow us on Telegram", onClick = {
            onClick()
        })

    }

}