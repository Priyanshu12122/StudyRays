package com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.db.videoDb.Video
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForVideo
import com.xerox.studyrays.utils.BottomSheet
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn
import kotlinx.coroutines.launch

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
    snackBarHostState: SnackbarHostState,
    onVideoClicked: (String, String) -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getKhazanaSolution(subjectId, chapterId, topicId, topicName = topicName)
    }

    val solutionState by vm.khazanaSolution.collectAsState()
    val scope = rememberCoroutineScope()

    when (val videosResult = solutionState) {
        is Response.Error -> {
            val messageState = rememberMessageBarState()

            DataNotFoundScreen(
                errorMsg = videosResult.msg,
                state = messageState,
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

                val savedStatusMap = remember { mutableStateMapOf<String, Boolean>() }

                var isOpen by rememberSaveable {
                    mutableStateOf(false)
                }

                var selectedVideoId by rememberSaveable {
                    mutableStateOf("")
                }

                var isCompleted by rememberSaveable {
                    mutableStateOf(false)
                }


                if (isOpen) {
                    BottomSheet(
                        sheetState = rememberModalBottomSheetState(),
                        onMarkAsCompletedClicked = {
                            mainViewModel.onMarkAsCompleteClicked(Video(selectedVideoId))
                            savedStatusMap[selectedVideoId] = !isCompleted
                            isOpen = false

                        },
                        onDownloadClicked = {
                            // Perform download action
                        },
                        isCompleted = isCompleted
                    ) {
                        isOpen = false
                    }
                }

                PullToRefreshLazyColumn(
                    items = videosResult.data.video_lectures,
                    content = {
                        val isComplete = savedStatusMap[it.video_id] ?: false

                        EachCardForVideo(
                            imageUrl = it.video_image,
                            title = it.video_name,
                            dateCreated = it.video_created_at.substring(11),
                            duration = it.video_duration,
                            isCompleted = isComplete,
                            onMoreVertClicked = {
                                isOpen = true
                                selectedVideoId = it.video_id
                                isCompleted = isComplete
                            },
                            videoId = it.video_id,
                            checkIfCompleted = { id ->
                                scope.launch {
                                    val saved =
                                        mainViewModel.checkIfItemIsPresentInVideoDb(Video(id))
                                    savedStatusMap[it.video_id] = saved
                                }
                            }
                        ) {
                            onVideoClicked(it.video_url, it.video_name)
                        }
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

//                LazyColumn {
//                    items(videosResult.data.video_lectures) {
//
//                        val isComplete = savedStatusMap[it.video_id] ?: false
//
//                        EachCardForVideo(
//                            imageUrl = it.video_image,
//                            title = it.video_name,
//                            dateCreated = it.video_created_at.substring(11),
//                            duration = it.video_duration,
//                            isCompleted = isComplete,
//                            onMoreVertClicked = {
//                                isOpen = true
//                                selectedVideoId = it.video_id
//                                isCompleted = isComplete
//                            },
//                            videoId = it.video_id,
//                            checkIfCompleted = { id ->
//                                scope.launch {
//                                    val saved =
//                                        mainViewModel.checkIfItemIsPresentInVideoDb(Video(id))
//                                    savedStatusMap[it.video_id] = saved
//                                }
//                            }
//                        ) {
//                            onVideoClicked(it.video_url, it.video_name)
//                        }
//
//                    }
//                }

            }
        }
    }

}