package com.xerox.studyrays.ui.screens.videoPlayerScreen.subscreens

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import kotlinx.coroutines.delay

@Composable
fun TimelineScreen(
    vm: MainViewModel = hiltViewModel(),
    externalId: String,
    videoViewModel: VideoViewModel = hiltViewModel(),
    onSlideClicked: (Long) -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getTimeline(externalId)
    }

    val state by vm.timeline.collectAsState()


    Column {

        when (val result = state) {
            is Response.Error -> {
                DataNotFoundScreen(
                    errorMsg = result.msg,
                    state = rememberMessageBarState(),
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getTimeline(externalId)
                    }) {

                }

            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = PaddingValues())
            }

            is Response.Success -> {

                val list =
                    remember { result.data?.sortedBy { it.slides_serial_number } ?: emptyList() }

                val lazyListState = rememberLazyListState()
                var blinkingSlideIndex by remember { mutableIntStateOf(-1) }

                LaunchedEffect(key1 = Unit) {
                    try {
                        val position = videoViewModel.getPlayerPosition()/1000
                        val index = list.indexOfFirst { it.slide_timestamp.toLong() >= position }
                        if (index != -1) {
                            lazyListState.animateScrollToItem(index)
                            blinkingSlideIndex = index
                        }
                    } catch (e: Exception){
                        Log.d("TAG", "TimelineScreen: exception $e")
                    }
                }

                LazyColumn(state = lazyListState) {
                    itemsIndexed(list) { index, item ->
                        EachCardForTimeLine(
                            url = item.slide_image_url,
                            time = item.slide_timestamp.toLong(),
                            slide = index + 1,
                            isBlinking = index == blinkingSlideIndex,
                            onClick = {
                                onSlideClicked(it)
                            }
                        )
                    }
                }


            }
        }

    }

}

@Composable
fun EachCardForTimeLine(
    modifier: Modifier = Modifier,
    url: String,
    time: Long,
    onClick: (Long) -> Unit,
    slide: Int,
    isBlinking: Boolean
) {

    var alpha by remember { mutableFloatStateOf(1f) }

    if (isBlinking) {
        LaunchedEffect(key1 = isBlinking) {
            repeat(3) {
                alpha = 0f
                delay(500)
                alpha = 1f
                delay(500)
            }
        }
    }

    Box(modifier = modifier
        .wrapContentSize()
        .alpha(alpha)) {


        AsyncImage(model = url, contentDescription = "",
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clickable {
                    onClick(time * 1000)
                })

        BadgeTimeline(text = "Slide no: $slide", modifier.align(Alignment.BottomStart))
    }
}


@Composable
fun BadgeTimeline(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12)
                )
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSystemInDarkTheme()) Color.Black else Color.White,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .align(Alignment.Center)
            )
        }
    }
}