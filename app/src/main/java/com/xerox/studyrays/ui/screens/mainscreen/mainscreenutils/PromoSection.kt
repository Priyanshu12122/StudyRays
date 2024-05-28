package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.xerox.studyrays.cacheDb.mainScreenCache.promoDb.PromoEntity
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.shimmerEffect
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Promo(
    vm: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by vm.promo.collectAsState()
    when (val result = state) {
        is Response.Error -> {

        }

        is Response.Loading -> {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .shimmerEffect()
            ) {

            }

        }

        is Response.Success -> {
            var isOpen by rememberSaveable {
                mutableStateOf(false)

            }

            if (result.data != null) {

                val pagerState = rememberPagerState(pageCount = { result.data.size })

                if (result.data.isNotEmpty()) {
                    PromoSectionAlertDialog(
                        item = result.data[pagerState.currentPage]!!,
                        isOpen = isOpen,
                        onDismissRequest = {
                            isOpen = false
                        }) {
                        vm.openUrl(
                            context,
                            result.data[pagerState.currentPage]!!.redirectionLink!!
                        )
                    }

                    PromoSection(list = result.data, state = pagerState, isOpen = isOpen) {
                        isOpen = true
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PromoSection(
    list: List<PromoEntity?>?,
    state: PagerState,
    isOpen: Boolean,
    onClick: () -> Unit,
) {

    var isScrolling by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isOpen) {
        if (!isOpen) {
            isScrolling = true // Start scrolling
            while (isScrolling) {
                delay(2500)
                val nextPage = (state.currentPage + 1) % (list?.size ?: 1)
                state.animateScrollToPage(nextPage)
            }

        } else {
            isScrolling = false
        }
    }

    if (list != null) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Box(modifier = Modifier.wrapContentSize()) {

                HorizontalPager(
//                pageCount = list?.size ?: 0,
                    state = state
                ) { currentPage ->
                    AsyncImage(
                        model = list.get(currentPage)?.imageUrl,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(horizontal = 25.dp, vertical = 10.dp)
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .clickable {
                                onClick()
                            },
                        contentScale = ContentScale.FillBounds
                    )

                }
            }
        }
    }


}

@Composable
fun PromoSectionAlertDialog(
    item: PromoEntity,
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onButtonClicked: () -> Unit,
) {

    if (isOpen) {
        AlertDialog(onDismissRequest = { onDismissRequest() }, confirmButton = { },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = "",
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .height(170.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillBounds
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = ("Description: " + item.description),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    if (item.redirectionLink != null) {
                        OutlinedButton(onClick = {
                            onButtonClicked()
                        }) {
                            Text(text = "Click here to visit")
                        }
                    }
                }
            })
    }

}