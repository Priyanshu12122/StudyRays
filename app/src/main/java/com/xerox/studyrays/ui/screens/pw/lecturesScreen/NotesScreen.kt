package com.xerox.studyrays.ui.screens.pw.lecturesScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    vm: MainViewModel = hiltViewModel(),
    slug: String,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    onPdfViewClicked: (String, String) -> Unit,
    onPdfDownloadClicked: (String?, String?) -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getAllNotes(slug)
    }

    val state by vm.notes.collectAsState()
    val notesResult = state

    Column(modifier = Modifier.fillMaxSize()) {
        when (notesResult) {
            is Response.Error -> {

                val messageState = rememberMessageBarState()
                DataNotFoundScreen(
                    paddingValues = paddingValues,
                    errorMsg = notesResult.msg,
                    state = messageState,
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getAllNotes(slug)
                    }) {
                }

            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = paddingValues)

            }

            is Response.Success -> {

                if (notesResult.data.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val composition by rememberLottieComposition(
                            spec = LottieCompositionSpec.RawRes(
                                if (isSystemInDarkTheme()) R.raw.comingsoondarkmode else R.raw.comingsoon
                            )
                        )

                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier
                                .size(300.dp)
                                .align(Alignment.Center)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        PullToRefreshLazyColumn(
                            items = notesResult.data,
                            content = {
                                EachCardForNotes(title = it.topic,
                                    onViewPdfClicked = {
                                        onPdfViewClicked(
                                            it.baseUrl+it.attachmentKey,
                                            it.topic ?: ""
                                        )
                                    }) {
                                    onPdfDownloadClicked(
                                        it.baseUrl+it.attachmentKey,
                                        it.topic
                                    )
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            onRefresh = {
                                vm.refreshAllNotes(slug){
                                    vm.showSnackBar(snackbarHostState)
                                }
                            })
//                        LazyColumn {
//                            items(notesResult.data) {
//                                EachCardForNotes(title = it.topic,
//                                    onViewPdfClicked = {
//                                        onPdfViewClicked(
//                                            it.baseUrl+it.attachmentKey,
//                                            it.topic ?: ""
//                                        )
//                                    }) {
//                                    onPdfDownloadClicked(
//                                        it.baseUrl+it.attachmentKey,
//                                        it.topic
//                                    )
//                                }
//                            }
//                        }


                    }

                }
            }
        }
    }

}


@Composable
fun EachCardForNotes(
    title: String?,
    onViewPdfClicked: () -> Unit,
    onPdfDownloadClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
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
                        1.dp,
                        Color.White.copy(0.6f),
                        RoundedCornerShape(10.dp)
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.pdf),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .weight(1f)
            )

            Text(text = title ?: "", modifier = Modifier.weight(7f))

            Image(
                painter = painterResource(id = R.drawable.eye),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .weight(1f)
                    .clickable {
                        onViewPdfClicked()
                    },
                colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            )

            Image(
                painter = painterResource(id = R.drawable.download),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .weight(1f)
                    .clickable {
                        onPdfDownloadClicked()
                    },
                colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            )
        }

    }

}