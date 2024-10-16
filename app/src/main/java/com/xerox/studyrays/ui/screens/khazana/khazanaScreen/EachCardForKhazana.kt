package com.xerox.studyrays.ui.screens.khazana.khazanaScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xerox.studyrays.R
import com.xerox.studyrays.model.khazanaModel.khazana.KhazanaItem
import com.xerox.studyrays.utils.shimmerEffect

@Composable
fun EachCardForKhazana(
    item: KhazanaItem,
    onClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .height(150.dp)
            .width(150.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.LightGray
            )
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onClick()
            }
            .then(
                Modifier.border(
                    1.dp,
                    Color.White.copy(0.6f),
                    RoundedCornerShape(10.dp)
                )

            )
    ) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(
                R.raw.khazanaaa
            )
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .size(90.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = item.name, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

        }

    }

}

@Composable
fun EachCardForKhazanaLoading() {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .height(150.dp)
            .width(150.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.LightGray
            )
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .then(
                Modifier.border(
                    1.dp,
                    Color.White.copy(0.6f),
                    RoundedCornerShape(10.dp)
                )

            )
    ) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(
                R.raw.khazanaaa
            )
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .shimmerEffect()
            )

        }

    }

}