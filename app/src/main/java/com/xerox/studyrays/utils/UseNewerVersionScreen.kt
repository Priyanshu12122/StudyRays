package com.xerox.studyrays.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xerox.studyrays.ui.theme.LightGreen3
import com.xerox.studyrays.ui.theme.LightRed
import com.xerox.studyrays.ui.theme.OrangeYellow1
import com.xerox.studyrays.ui.theme.TextBlack
import com.xerox.studyrays.ui.theme.TextWhite

@Composable
fun UseNewerVersionScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(10.dp),
                    ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                )
                .clip(RoundedCornerShape(10.dp))
                .background(if (!isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.background)
                .then(
                    if (isSystemInDarkTheme()) {
                        Modifier.border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    OrangeYellow1,
                                    LightRed,
                                    LightGreen3
                                )
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    } else {
                        Modifier
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "It seems like you are using a lower end device, this section is only available to higher end devices, If you wish to access this section then try using a newer model device",
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                modifier = Modifier.padding(15.dp),
                color = if (isSystemInDarkTheme()) TextWhite else TextBlack
            )

        }

    }
}