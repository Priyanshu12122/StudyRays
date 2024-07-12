package com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
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
fun KhazanaSolutionsScreen(
    vm: KhazanaViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    subjectId: String,
    chapterId: String,
    topicId: String,
    topicName: String,
    paddingValues: PaddingValues,
    snackBarHostState: SnackbarHostState,
    onVideoClicked: (String, String, String, String, String, String, String) -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getKhazanaSolution(subjectId, chapterId, topicId, topicName = topicName)
    }

    val solutionState by vm.khazanaSolution.collectAsState()
    val scope = rememberCoroutineScope()
    var searchText by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    when (val videosResult = solutionState) {
        is Response.Error -> {
            val messageState = rememberMessageBarState()

            DataNotFoundScreen(
                errorMsg = videosResult.msg,
                state = messageState,
                paddingValues = paddingValues,
                shouldShowBackButton = false,
                onRetryClicked = {
                    vm.getKhazanaSolution(subjectId, chapterId, topicId, topicName = topicName)
                }) {

            }
        }

        is Response.Loading -> {
            LoadingScreen(paddingValues = PaddingValues())

        }

        is Response.Success -> {

            if (videosResult.data.video_lectures.isEmpty()) {
                NoFilesFoundScreen()
            } else {

                val list = videosResult.data.video_lectures
                val filteredList = list.filter { it.video_name.contains(searchText, true) }

//                val savedStatusMap = remember { mutableStateMapOf<String, Boolean>() }
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
//                var isCompleted by rememberSaveable {
//                    mutableStateOf(false)
//                }
//
//
//                if (isOpen) {
//                    BottomSheet(
//                        sheetState = rememberModalBottomSheetState(),
//                        onMarkAsCompletedClicked = {
//                            mainViewModel.onMarkAsCompleteClicked(Video(selectedVideoId))
//                            savedStatusMap[selectedVideoId] = !isCompleted
//                            isOpen = false
//
//                        },
//                        onDownloadClicked = {
//                            // Perform download action
//                        },
//                        isCompleted = isCompleted
//                    ) {
//                        isOpen = false
//                    }
//                }

                PullToRefreshLazyColumn(
                    item = {
                        SearchTextField(
                            searchText = searchText,
                            onSearchTextChanged = { searchText = it },
                            onCrossIconClicked = { searchText = "" },
                            text = "Search Dpp solutions"
                        )
                    },
                    items = if (searchText == "") list else filteredList,
                    content = {
//                        val isComplete = savedStatusMap[it.video_id] ?: false
//
//                        val isSaved = savedStatusMapWatchLater[it.video_id] ?: false

                        EachCardForVideo3(
                            imageUrl = it.video_image,
                            title = it.video_name,
                            dateCreated = it.video_created_at.substring(11),
                            duration = it.video_duration,
                            onVideoClicked = {
                                onVideoClicked(
                                    it.video_url ?: "",
                                    it.video_name ?: "",
                                    it.video_id,
                                    it.video_image,
                                    it.video_created_at.substring(11)
                                        ?: "no data",
                                    it.video_duration ?: "duration not available",
                                    Constants.KHAZANA
                                )
                            },
                            videoId = it.video_id
                        )


//                        EachCardForVideo(
//                            imageUrl = it.video_image,
//                            title = it.video_name,
//                            dateCreated = it.video_created_at?.substring(11)
//                                ?: "no data",
//                            duration = it.video_duration,
//                            isCompleted = isComplete,
//                            onMoreVertClicked = {
//                                isOpen = true
//                                selectedVideoId = it.video_id
//                                isCompleted = isComplete
//                            },
//                            videoId = it.video_id,
//                            isSaved = isSaved,
//                            checkIfSaved = {id->
//                                scope.launch {
//                                    val saved = mainViewModel.checkIfPresentInWatchLater(id)
//                                    savedStatusMapWatchLater[it.video_id] = saved
//                                }
//                            },
//                            onStarClick = {
//                                mainViewModel.onStarClick(
//                                    WatchLaterEntity(
//                                        imageUrl = it.video_image ,
//                                        title = it.video_name ,
//                                        dateCreated = it.video_created_at?.substring(11) ?: "no data"
//                                            ,
//                                        isComplete = isComplete,
//                                        duration = it.video_duration ,
//                                        videoId = it.video_id,
//                                        videoUrl = it.video_url ,
//                                        externalId = it.external_id,
//                                        embedCode = "",
//                                        time = System.currentTimeMillis(),
//                                        isAk = false,
//                                        isKhazana = true,
//                                        isPw = false
//                                    ), context = context)
//                                savedStatusMapWatchLater[it.video_id] = !isSaved
//                            },
//                            checkIfCompleted = { id ->
//                                scope.launch {
//                                    val saved =
//                                        mainViewModel.checkIfItemIsPresentInVideoDb(
//                                            Video(id)
//                                        )
//                                    savedStatusMap[it.video_id] = saved
//                                }
//                            }
//                        ) {
//                            onVideoClicked(
//                                it.video_url ?: "",
//                                it.video_name ?: "",
//                                it.video_id,
//                                it.video_image,
//                                it.video_created_at?.substring(11)
//                                    ?: "no data",
//                                it.video_duration ?: "duration not available",
//                                Constants.KHAZANA
//                            )
//                        }

                    },
                    isRefreshing = vm.isRefreshing,
                    onRefresh = {
                        vm.refreshKhazanaSolutionDpp(
                            subjectId = subjectId,
                            chapterId = chapterId,
                            topicId = topicId,
                            topicName = topicName
                        ) {
                            vm.showSnackBar(snackBarHostState)
                        }
                    })

            }
        }
    }

}