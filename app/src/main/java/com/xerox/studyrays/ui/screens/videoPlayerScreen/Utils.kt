package com.xerox.studyrays.ui.screens.videoPlayerScreen

import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.xerox.studyrays.utils.SpacerHeight

@Composable
fun SaveAlertDialog(
    modifier: Modifier = Modifier,
    isSaved: Boolean,
    addText: String,
    removeText: String,
    onOkClicked: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(onDismissRequest = { onDismissRequest() }, confirmButton = {

    },
        text = {

            Column(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpacerHeight(dp = 15.dp)
                Text(text = if (isSaved) removeText else addText)
                SpacerHeight(dp = 15.dp)
                OutlinedButton(
                    onClick = {
                        onOkClicked()
                    }, modifier = modifier
                        .padding(vertical = 0.dp, horizontal = 10.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "OK")
                }
            }
        })
}

@Composable
fun VideoPlayerTaskAlertDialog(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner,
    onDismissRequest: () -> Unit,
    onOkClicked: () -> Unit,
) {
    AlertDialog(onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {

    },
        text = {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SpacerHeight(dp = 10.dp)

                Text(
                    text = "How to Complete Task :",
                    modifier = modifier.padding(vertical = 0.dp, horizontal = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 5.dp, vertical = 8.dp))

                SpacerHeight(dp = 10.dp)

                AndroidView(
                    factory = { ctx ->
                        YouTubePlayerView(ctx)
                            .apply {
                                lifecycleOwner.lifecycle.addObserver(this)

                                addYouTubePlayerListener(object :
                                    AbstractYouTubePlayerListener() {
                                    override fun onReady(youTubePlayer: YouTubePlayer) {
                                        youTubePlayer.loadVideo("b5kt1rm76q4", 0f)
                                    }


                                }
                                )

                                addFullscreenListener(object : FullscreenListener {
                                    override fun onEnterFullscreen(
                                        fullscreenView: View,
                                        exitFullscreen: () -> Unit,
                                    ) {
                                    }

                                    override fun onExitFullscreen() {
                                    }

                                })
                            }
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .height(350.dp)
                )
                SpacerHeight(dp = 10.dp)

                Text(text = "Complete Today's Task and Unlock 3 Lecture Downloads")

                SpacerHeight(dp = 18.dp)


                OutlinedButton(
                    onClick = {
                        onOkClicked()
                    },
                    modifier = modifier
                        .padding(vertical = 0.dp, horizontal = 10.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "COMPLETE NOW")
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoQualityBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onClick: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val qualitiesList = listOf("240p", "360p", "480p", "720p")
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismissRequest() },
        content = {

            LazyColumn {
                item {
                    Text(
                        text = "Select quality :- ",
                        modifier = modifier.padding(vertical = 0.dp, horizontal = 40.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )
                    SpacerHeight(dp = 15.dp)
                }

                items(qualitiesList) {
                    EachColumnForBottomSheetQuality(
                        qualityText = it,
                        onClick = {
                            onClick(it.substringBefore("p"))
                        }
                    )
                }
                item {
                    SpacerHeight(dp = 60.dp)
                }
            }
        }
    )
}


@Composable
fun EachColumnForBottomSheetQuality(
    qualityText: String,
    onClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = qualityText,
            modifier = Modifier.padding(vertical = 0.dp, horizontal = 25.dp),
            fontSize = 19.sp
        )
        SpacerHeight(dp = 8.dp)
        HorizontalDivider(modifier = Modifier.padding(vertical = 0.dp, horizontal = 20.dp))
    }
}

@Composable
fun TextWithIcon(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
            IconButton(
                onClick = {
                    onClick()
                }) {
                icon()
            }
        Text(text = text, fontSize = 13.sp)
        SpacerHeight(dp = 5.dp)

    }
}

