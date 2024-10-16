package com.xerox.studyrays.ui.leaderBoard.user.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xerox.studyrays.ui.theme.AlegreyaFontFamily
import com.xerox.studyrays.ui.theme.AlegreyaSansFontFamily
import com.xerox.studyrays.utils.SpacerHeight

@Composable
fun SuccessScreen(
    modifier: Modifier = Modifier,
    userId: String,
    onUserIdCopy: () -> Unit,
    onProceedClick: () -> Unit,
) {

    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Account Created Successfully!",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = AlegreyaFontFamily,
                fontWeight = FontWeight(700),
                color = Color.White
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
        )

        SpacerHeight(dp = 3.dp)

        Text(
            text = "Copy and Save the following Unique Id \n to login afterwards",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = AlegreyaSansFontFamily,
                fontWeight = FontWeight(700),
                color = Color.White
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
        )

        Text(
            text = userId,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = AlegreyaFontFamily,
                fontWeight = FontWeight(500),
                color = Color.White
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
        )
        SpacerHeight(dp = 6.dp)

        OutlinedButton(
            onClick = {
                clipboardManager.setText(AnnotatedString(userId))
                onUserIdCopy()
            },
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "COPY UNIQUE ID",
                style = TextStyle(
                    fontFamily = AlegreyaFontFamily,
                    fontWeight = FontWeight(500),
                    color = Color.White
                )
            )

        }

        Button(
            onClick = {
                onProceedClick()
            },
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "PROCEED",
                style = TextStyle(
                    fontFamily = AlegreyaFontFamily,
                    fontWeight = FontWeight(500),
                    color = Color.White
                )
            )

        }


    }


}