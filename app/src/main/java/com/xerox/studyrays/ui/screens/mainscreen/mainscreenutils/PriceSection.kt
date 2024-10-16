package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.formatToIndianRupees
import com.xerox.studyrays.utils.shimmerEffect


@Composable
fun PriceSection(
    vm: MainViewModel = hiltViewModel(),
) {

    val totalFee by vm.totalFee.collectAsState()
    val fee = totalFee

    LaunchedEffect(key1 = Unit) {
        if (fee !is Response.Success) {
            vm.getTotalFee()
        }
    }
    when (fee) {
        is Response.Error -> {

        }

        is Response.Loading -> {

            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            ) {}

        }

        is Response.Success -> {
            val price = fee.data.total_amount

            var animateText by rememberSaveable {
                mutableStateOf(false)
            }

            LaunchedEffect(key1 = Unit) {
                animateText = true
            }
            val animatedInt by animateIntAsState(
                targetValue = if (animateText) price else 0,
                label = "",
                animationSpec = tween(1000)
            )

            CardForPriceSection(totalFee = animatedInt)
        }
    }
}

@Composable
fun CardForPriceSection(
    totalFee: Int,
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(60.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
            )
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xfff4cf65))
            .then(
                if (isSystemInDarkTheme()) {
                    Modifier.border(
                        1.dp,
                        Color.White.copy(0.6f),
                        RoundedCornerShape(10.dp)
                    )
                } else {
                    Modifier
                }
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Total Batch Fee : ${totalFee.formatToIndianRupees()}",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF444444)
            )
        }


    }

}