package com.xerox.studyrays.ui.screens.pw.lecturesScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.xerox.studyrays.utils.BottomSheet
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import kotlinx.coroutines.launch


// DPP Solution screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DppSolutionScreen(
    vm: MainViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    slug: String,
    onClick: (String, String, String) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        vm.getAllDppSolution(slug)
    }

    val state by vm.dppSolution.collectAsState()
    val dppSolutionResult = state
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        when (dppSolutionResult) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()
                DataNotFoundScreen(
                    paddingValues = paddingValues,
                    errorMsg = dppSolutionResult.msg,
                    state = messageState,
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getAllDppSolution(slug)
                    }) {

                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = paddingValues)
            }

            is Response.Success -> {
                if (dppSolutionResult.data.isEmpty()){
                    Box(modifier = Modifier.fillMaxSize()) {
                        val composition by rememberLottieComposition(
                            spec = LottieCompositionSpec.RawRes(
                                if (isSystemInDarkTheme()) R.raw.comingsoondarkmode else  R.raw.comingsoon
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
                                vm.onMarkAsCompleteClicked(Video(selectedVideoId))
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

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LazyColumn {
                            items(dppSolutionResult.data.sortedBy { it.name }) {

                                val isComplete = savedStatusMap[it.video_id] ?: false

                                EachCardForVideo(
                                    imageUrl = it.image_url,
                                    title = it.name,
                                    dateCreated = it.created_at.substringBefore("T"),
                                    duration = it.duration,
                                    isCompleted = isComplete,
                                    onMoreVertClicked = {
                                        isOpen = true
                                        selectedVideoId = it.video_id
                                        isCompleted = isComplete
                                    },
                                    videoId = it.video_id,
                                    checkIfCompleted = {id->
                                        scope.launch {
                                            val saved =
                                                vm.checkIfItemIsPresentInVideoDb(Video(id))
                                            savedStatusMap[it.video_id] = saved
                                        }
                                    }
                                ) {
                                    onClick(it.video_url,it.name, it.external_id)
                                }
                            }
                        }


                    }

                }
            }


            null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "No items here",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }
            }
        }
    }
}
