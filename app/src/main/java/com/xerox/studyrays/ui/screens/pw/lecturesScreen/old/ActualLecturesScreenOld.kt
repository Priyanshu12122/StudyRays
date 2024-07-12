package com.xerox.studyrays.ui.screens.pw.lecturesScreen.old

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForVideo3
import com.xerox.studyrays.utils.Constants
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn
import com.xerox.studyrays.utils.SearchTextField

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualLecturesScreenOld(
    vm: MainViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    slug: String,
    onVideoClicked: (String, String, String, String, String, String, String, String, String) -> Unit,
) {


    LaunchedEffect(key1 = Unit) {
        vm.getAllVideosOld(slug)
    }


    val videosState by vm.videosOld.collectAsState()
    val videosResult = videosState


    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    when (videosResult) {
        is Response.Error -> {
            val messageState = rememberMessageBarState()

            if (videosResult.msg == vm.nullErrorMsg) {
                NoFilesFoundScreen()
            } else {
                DataNotFoundScreen(
                    errorMsg = videosResult.msg,
                    paddingValues = paddingValues,
                    state = messageState,
                    shouldShowBackButton = true,
                    onRetryClicked = {
                        vm.getAllVideosOld(slug)
                    }) {
                    onBackClicked()
                }

            }

        }

        is Response.Loading -> {
            LoadingScreen(paddingValues = PaddingValues())
        }

        is Response.Success -> {
            if (videosResult.data.isEmpty()) {
                NoFilesFoundScreen()
            } else {

//                val savedStatusMap =
//                    remember { mutableStateMapOf<String, Boolean>() }
//
//                val savedStatusMapWatchLater =
//                    remember { mutableStateMapOf<String, Boolean>() }
//
//                var isOpen by rememberSaveable {
//                    mutableStateOf(false)
//                }
//
//                var selectedVideoId by rememberSaveable {
//                    mutableStateOf("")
//                }
//
//                var selectedVideoUrl by rememberSaveable {
//                    mutableStateOf("")
//                }
//
//                var isCompleted by rememberSaveable {
//                    mutableStateOf(false)
//                }

                val videoList = videosResult.data.sortedBy { it.name }
                val filteredList = videoList.filter { it.name.contains(searchText, true) }


//                if (isOpen) {
//                    BottomSheet(
//                        sheetState = rememberModalBottomSheetState(),
//                        onMarkAsCompletedClicked = {
//                            vm.onMarkAsCompleteClicked(Video(selectedVideoId))
//                            savedStatusMap[selectedVideoId] = !isCompleted
//                            isOpen = false
//
//                        },
//                        onDownloadClicked = {
//
//                        },
//                        isCompleted = isCompleted,
//                    ) {
//                        isOpen = false
//                    }
//                }

                PullToRefreshLazyColumn(
                    item = {

                        SearchTextField(
                            searchText = searchText,
                            onSearchTextChanged = { searchText = it },
                            text = "Search Lectures",
                            onCrossIconClicked = {
                                searchText = ""
                            },
                        )

                    },
                    items = if (searchText == "") videoList else filteredList,
                    content = { video ->
//                        val isComplete = savedStatusMap[video.video_id] ?: false
//
//                        val isSaved = savedStatusMapWatchLater[video.video_id] ?: false

                        EachCardForVideo3(
                            imageUrl = video.image_url,
                            title = video.name,
                            dateCreated = video.createdAt.substringBefore("T"),
                            duration = video.duration,
                            videoId = video.video_id,
                            onVideoClicked = {
                                onVideoClicked(
                                    video.video_url,
                                    video.name,
                                    video.external_id,
                                    video.embedCode,
                                    video.video_id,
                                    video.image_url,
                                    video.createdAt,
                                    video.duration,
                                    Constants.PW
                                )
                            }
                        )
//                        EachCardForVideo(
//                            imageUrl = video.image_url,
//                            title = video.name,
//                            dateCreated = video.createdAt.substringBefore("T"),
//                            isCompleted = isComplete,
//                            duration = video.duration,
//                            onMoreVertClicked = {
//                                isOpen = true
//                                selectedVideoId = video.video_id
//                                selectedVideoUrl = video.video_url
//                                isCompleted = isComplete
//                            },
//                            videoId = video.video_id,
//                            isSaved = isSaved,
//                            onStarClick = {
//                                vm.onStarClick(WatchLaterEntity(
//                                    imageUrl = video.image_url,
//                                    title = video.name,
//                                    dateCreated = video.createdAt,
//                                    isComplete = isComplete,
//                                    duration = video.duration,
//                                    videoId = video.video_id,
//                                    videoUrl = video.video_url,
//                                    externalId = video.external_id,
//                                    embedCode = video.embedCode,
//                                    time = System.currentTimeMillis(),
//                                    isAk = false,
//                                    isKhazana = false,
//                                    isPw = true
//                                ), context = context)
//                                savedStatusMapWatchLater[video.video_id] = !isSaved
//                            },
//                            checkIfSaved = {id->
//                                scope.launch {
//                                    val saved = vm.checkIfPresentInWatchLater(id)
//                                    savedStatusMapWatchLater[video.video_id] = saved
//                                }
//                            },
//                            checkIfCompleted = {
//                                scope.launch {
//                                    val saved =
//                                        vm.checkIfItemIsPresentInVideoDb(
//                                            Video(
//                                                it
//                                            )
//                                        )
//                                    savedStatusMap[video.video_id] = saved
//                                }
//                            }
//                        ) {
//                            onVideoClicked(
//                                video.video_url,
//                                video.name,
//                                video.external_id,
//                                video.embedCode,
//                                video.video_id,
//                                video.image_url,
//                                video.createdAt,
//                                video.duration,
//                                Constants.PW
//                            )
//                        }
                    },
                    isRefreshing = vm.isRefreshing,
                    onRefresh = {
                        vm.refreshAllVideosOld(slug) {
                            vm.showSnackBar(snackbarHostState)
                        }
                    })

            }
        }
    }
}