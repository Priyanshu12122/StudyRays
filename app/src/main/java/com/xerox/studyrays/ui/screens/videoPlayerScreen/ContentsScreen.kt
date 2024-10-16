package com.xerox.studyrays.ui.screens.videoPlayerScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.R
import com.xerox.studyrays.db.downloadsDb.DownloadNumberEntity
import com.xerox.studyrays.db.watchLaterDb.WatchLaterEntity
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.videoPlayerScreen.subscreens.CommentsScreen
import com.xerox.studyrays.ui.screens.videoPlayerScreen.subscreens.NotesScreen
import com.xerox.studyrays.ui.screens.videoPlayerScreen.subscreens.TelegramScreen
import com.xerox.studyrays.ui.screens.videoPlayerScreen.subscreens.TimelineScreen
import com.xerox.studyrays.utils.Constants
import com.xerox.studyrays.utils.IconItem
import com.xerox.studyrays.utils.ImmutableIcons
import com.xerox.studyrays.utils.SelectableIconRow
import com.xerox.studyrays.utils.SpacerHeight
import com.xerox.studyrays.utils.toReadableDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ContentsScreen(
    modifier: Modifier = Modifier,
    title: String,
    mainViewModel: MainViewModel = hiltViewModel(),
    vm: VideoViewModel = hiltViewModel(),
    imageUrl: String,
    createdAt: String,
    duration: String,
    videoId: String,
    id: String,
    url: String,
    embedCode: String,
    isWhat: String,
    topicSlug: String,
    isOld: Boolean,
    onTaskUrlChanged: (String) -> Unit,
    onShortenedUrlChanged: (String) -> Unit,
    onDownloadUrlChanged: (String) -> Unit,
    onIsTaskOpenChanged: (Boolean) -> Unit,
    onIsPlayerReadyChanged: (Boolean) -> Unit,
    onBottomSheetChanged: (Boolean) -> Unit,
//    onAddNoteClicked: () -> Unit,
    onNoteClick: (Long) -> Unit,
    onSlideClicked: (Long) -> Unit,
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val navBar by mainViewModel.navBar.collectAsState()
    val downloadState by mainViewModel.downloads.collectAsState()
    val downloadResultState = downloadState

    val isWatchLaterSaved by mainViewModel.isSavedWatchLater.collectAsState()

    var baseLink by rememberSaveable { mutableStateOf("") }
    var telegramUrl by rememberSaveable { mutableStateOf("") }


    val icons = if (!isOld) {

        ImmutableIcons(
            icons = listOf(
                IconItem(
                    icon = Icons.Default.ChatBubbleOutline, content = {
                        CommentsScreen(externalId = id, topicSlug = topicSlug)
                    },
                    isImageVector = false,
                    painter = R.drawable.comment
                ),


                IconItem(
                    icon = Icons.AutoMirrored.Filled.NoteAdd, content = {
                        NotesScreen(
                            videoId = videoId,
                            onNoteClick = {
                                onNoteClick(it)
                            }
                        )
                    },
                    isImageVector = false,
                    painter = R.drawable.notes
                ),

                IconItem(
                    icon = Icons.Outlined.WatchLater, content = {
                        TimelineScreen(
                            externalId = id,
                            onSlideClicked = {
                                onSlideClicked(it)
                            })
                    },
                    isImageVector = true,
                    painter = R.drawable.staroutlined
                ),

                IconItem(
                    icon = if (isWatchLaterSaved) Icons.Default.Star else Icons.Default.StarBorder,
                    content = {
//                        Text(text = "This is the star screen")
                    },
                    isImageVector = true,
                    painter = if (isWatchLaterSaved) R.drawable.starfilled else R.drawable.staroutlined
                ),

                IconItem(
                    icon = Icons.Default.StarBorder,
                    content = {
                        TelegramScreen {
                            if (telegramUrl != "") {
                                mainViewModel.openTelegram(context, telegramUrl)
                            } else {
                                mainViewModel.showToast(
                                    context,
                                    "Some Error ocurred, Please try reloading the page.."
                                )
                            }
                        }

                    },
                    isImageVector = false,
                    painter = R.drawable.telegram
                )

            )
        )
    } else {
        ImmutableIcons(
            icons = listOf(
                IconItem(
                    icon = Icons.AutoMirrored.Filled.NoteAdd, content = {
                        NotesScreen(
                            videoId = videoId,
                            onNoteClick = {
                                onNoteClick(it)
                            }
                        )
                    },
                    isImageVector = true,
                    painter = R.drawable.staroutlined
                ),
                IconItem(
                    icon = Icons.Default.Star, content = {
//                        Text(text = "This is the star screen")
                    },
                    isImageVector = false,
                    painter = if (isWatchLaterSaved) R.drawable.starfilled else R.drawable.staroutlined
                ),

                IconItem(
                    icon = Icons.Default.StarBorder,
                    content = {
                        TelegramScreen {
                            if (telegramUrl != "") {
                                mainViewModel.openTelegram(context, telegramUrl)
                            } else {
                                mainViewModel.showToast(
                                    context,
                                    "Some Error occurred, Please try reloading the page.."
                                )
                            }
                        }

                    },
                    isImageVector = false,
                    painter = R.drawable.telegram
                )
            )


        )
    }



    Column {

        Column(
            modifier = modifier
//                .padding(it)
                .padding(1.dp)
                .fillMaxWidth()
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
                ),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
                SpacerHeight(dp = 10.dp)

//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(15.dp),
//
//                    ) {
//
//                    SpacerWidth(dp = 2.dp)
//
//                    TextWithIcon(text = "Watch Later", onClick = {
//                        mainViewModel.onStarClick(
//                            WatchLaterEntity(
//                                imageUrl = imageUrl,
//                                title = title,
//                                dateCreated = createdAt,
//                                duration = duration,
//                                videoId = videoId,
//                                videoUrl = url,
//                                externalId = id,
//                                embedCode = embedCode,
//                                time = System.currentTimeMillis(),
//                                isAk = false,
//                                isKhazana = isWhat == Constants.KHAZANA,
//                                isPw = isWhat == Constants.PW
//                            ),
//                            context = context
//                        )
//                    },
//                        icon = {
//                            Image(
//                                painter = painterResource(id = if (isWatchLaterSaved) R.drawable.starfilled else R.drawable.staroutlined),
//                                contentDescription = "",
//                                modifier = Modifier.size(70.dp),
//                                colorFilter = (if (!isWatchLaterSaved && isSystemInDarkTheme()) Color.White else null)?.let {
//                                    ColorFilter.tint(
//                                        color = it
//                                    )
//                                }
//                            )
//                        })
//
//                    TextWithIcon(
//                        onClick = {
//
//                            if (telegramUrl == "") {
//                                vm.showToast(context, "Sorry an error occurred")
//                            } else {
//                                mainViewModel.openTelegram(
//                                    context,
//                                    url = telegramUrl
//                                )
//
//                            }
//
//                        },
//                        text = "Telegram",
//                        icon = {
//                            Icon(
//                                painter = painterResource(id = R.drawable.telegram),
//                                contentDescription = "",
//                                modifier = Modifier.size(70.dp)
//                            )
//                        }
//                    )
//
//                    TextWithIcon(text = "Add note", onClick = {
//                        onAddNoteClicked()
//                    },
//                        icon = {
//                            Icon(
//                                painter = painterResource(id = R.drawable.baseline_note_add_24),
//                                contentDescription = "",
//                                modifier = modifier.size(70.dp)
//                            )
//                        })
//
                when (val navResult = navBar) {
                    is ResponseTwo.Error -> {}
                    is ResponseTwo.Loading -> {}
                    is ResponseTwo.Nothing -> {}
                    is ResponseTwo.Success -> {
                        telegramUrl = navResult.data.telegram
                    }
                }



                when (downloadResultState) {
                    is Response.Error -> {
                        Log.d(
                            "TAG",
                            "VideoPlayerScreen: Error in down load error = ${downloadResultState.msg}"
                        )
                    }

                    is Response.Loading -> {}
                    is Response.Success -> {

                        baseLink = if (downloadResultState.data.stream_type == "mpd_url") {
                            downloadResultState.data.mpd_url
                        } else {
                            downloadResultState.data.master_url
                        }
                        val licenseUrl = baseLink + url

                        baseLink += /*downloadResultState.data.master_url*/ url

                        LaunchedEffect(key1 = Unit) {
                            if (downloadResultState.data.stream_type == "mpd_url") {
                                vm.initialiseDrmPlayer(
                                    context = context,
                                    url = url,
                                    licenseUrl = licenseUrl,
                                    videoId = videoId,
                                )
                                Log.d("TAG", "ContentsScreen: Mpd url hai")
                                Log.d("TAG", "url = $url")
                            } else {
                                vm.initialisePlayer(
                                    context = context,
                                    url = baseLink,
                                    videoId = videoId,
                                )

                                Log.d("TAG", "ContentsScreen: M3u8 url hai")
                                Log.d("TAG", "baselink = $baseLink")


                            }
                            onIsPlayerReadyChanged(true)
                        }

                        if (downloadResultState.data.visible == "1") {
                            TextWithIcon(
                                onClick = {
                                    scope.launch {
                                        val task = vm.getTask(1)
                                        val alarmItem = vm.getAlarmById(1)
                                        val ip =
                                            withContext(Dispatchers.IO) { vm.getIPAddress(true) }

                                        if (ip == null || task == null) {
                                            onTaskUrlChanged(downloadResultState.data.task_url)
                                            onShortenedUrlChanged(downloadResultState.data.task_final_url)
                                            onIsTaskOpenChanged(true)
                                        } else if (task.ipAddress != ip) {
                                            onTaskUrlChanged(downloadResultState.data.task_url)
                                            onShortenedUrlChanged(downloadResultState.data.task_final_url)
                                            onIsTaskOpenChanged(true)
                                        } else {
                                            onDownloadUrlChanged(downloadResultState.data.download_url)
                                            val downloadNumber = vm.getDownloadNumberById(1)

                                            when {
                                                downloadNumber == null -> {
                                                    vm.insertDownloadNumber(
                                                        DownloadNumberEntity(
                                                            id = 1,
                                                            numberOfDownloads = 1
                                                        )
                                                    )
                                                    onBottomSheetChanged(true)
                                                }

                                                downloadNumber.numberOfDownloads < downloadResultState.data.download_limit.toInt() -> {
                                                    vm.insertDownloadNumber(
                                                        DownloadNumberEntity(
                                                            id = 1,
                                                            numberOfDownloads = downloadNumber.numberOfDownloads + 1
                                                        )
                                                    )
                                                    onBottomSheetChanged(true)
                                                }

                                                alarmItem != null && alarmItem.timeToTriggerAt > System.currentTimeMillis() -> {
                                                    mainViewModel.showToast(
                                                        context,
                                                        "You have reached today's download limit, you can download again after ${alarmItem.timeToTriggerAt.toReadableDate()}"
                                                    )
                                                }

                                                else -> {
                                                    if (alarmItem != null) {
                                                        vm.deleteAlarmItem(alarmItem)
                                                    }
                                                    vm.deleteDownloadNumber(downloadNumber)
                                                    vm.deleteTask(task)
                                                    onIsTaskOpenChanged(true)
                                                }
                                            }
                                        }
                                    }
                                },
                                text = "Download",
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_download_24),
                                        contentDescription = "",
                                        modifier = Modifier.size(90.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
//        }

        SelectableIconRow(
            icons = icons,
            isOld = isOld,
            onTelegramClick = {
                if (telegramUrl != "") {
                    mainViewModel.openTelegram(context, telegramUrl)
                } else {
                    mainViewModel.showToast(
                        context,
                        "Some Error ocurred, Please try reloading the page.."
                    )
                }
            },
            onStarClick = {

                mainViewModel.onStarClick(
                    WatchLaterEntity(
                        imageUrl = imageUrl,
                        title = title,
                        dateCreated = createdAt,
                        duration = duration,
                        videoId = videoId,
                        videoUrl = url,
                        externalId = id,
                        embedCode = embedCode,
                        time = System.currentTimeMillis(),
                        isKhazana = isWhat == Constants.KHAZANA,
                        isPw = isWhat == Constants.PW,
                        isOld = isOld
                    ),
                    context = context
                )

            }
        )


    }


}