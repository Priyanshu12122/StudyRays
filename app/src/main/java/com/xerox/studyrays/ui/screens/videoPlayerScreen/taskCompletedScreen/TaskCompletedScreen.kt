package com.xerox.studyrays.ui.screens.videoPlayerScreen.taskCompletedScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xerox.studyrays.R
import com.xerox.studyrays.utils.SpacerHeight

@Composable
fun TaskCompletedScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    val composition by
    rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.taskcompleted))
    AlertDialog(
        onDismissRequest = { }, confirmButton = { },
        text = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SpacerHeight(dp = 10.dp)

                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(200.dp)
                )
                SpacerHeight(dp = 20.dp)

                Text(
                    text = "Your task is completed! Now you can download lectures",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                SpacerHeight(dp = 20.dp)

                OutlinedButton(
                    onClick = {
                        onClick()
                    },
                    modifier = modifier
                        .padding(vertical = 0.dp, horizontal = 10.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "GO TO APP")
                }
            }
        }
    )
}