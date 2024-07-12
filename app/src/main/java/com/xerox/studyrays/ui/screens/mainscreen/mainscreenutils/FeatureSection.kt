package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun FeatureSection(
    list: List<MainScreenItem>,
    onFollowOnTelegramClicked: () -> Unit,
    onFavBatchesClicked: () -> Unit,
    onKhazanaClicked: () -> Unit,
    onAkClicked: () -> Unit,
    onUpdateBatchesClicked: () -> Unit,
    onAllBatchesClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 7.5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            item(span = { GridItemSpan(2) }) {
                Promo()
            }

            item(span = { GridItemSpan(2) }) {
                PriceSection()
            }

            item(span = { GridItemSpan(2) }) {
                StatusScreen()
            }

            items(list, key = {it.title }) {
                FeatureItem(item = it) {

                    when (it.title) {
                        "Physics Wallah" -> onAllBatchesClicked()
                        "Follow on Telegram" -> onFollowOnTelegramClicked()
                        "Favourite Batches" -> onFavBatchesClicked()
                        "Khazana" -> onKhazanaClicked()
                        "Apni Kaksha" -> onAkClicked()
                        "Update/add batches" -> onUpdateBatchesClicked()

                    }
                }
            }
        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun FeatureItem(
    item: MainScreenItem,
    onClick: () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(7.5.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
            )
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (!isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.background)
            .clickable {
                onClick()
            }
            .then(
                if (isSystemInDarkTheme()) {
                    Modifier.border(
                        1.dp,
                        brush = Brush.horizontalGradient(colors = item.colors),
                        RoundedCornerShape(10.dp)
                    )
                } else {
                    Modifier
                }
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (item.isRaw) {
                    val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            item.raw
                        )
                    )

                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(80.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = item.raw),
                        contentDescription = "",
                        modifier = Modifier.size(80.dp)
                    )
                }

                Text(
                    text = item.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 25.sp,
                    )
            }
        }
    }
}