package com.xerox.studyrays.ui.screens.pw.lecturesScreen.old

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForVideo3
import com.xerox.studyrays.utils.BottomSheet
import com.xerox.studyrays.utils.Constants
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.SearchTextField


// DPP Solution screen
@Composable
fun DppSolutionScreenOld(
    vm: MainViewModel = hiltViewModel(),
    slug: String,
    paddingValues: PaddingValues,
    onClick: (String, String, String, String, String, String, String, String) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        vm.getAllDppSolutionOld(slug)
    }

    val context = LocalContext.current
    val state by vm.dppSolutionOld.collectAsState()
    val dppSolutionResult = state
    val scope = rememberCoroutineScope()
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when (dppSolutionResult) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()
                DataNotFoundScreen(
                    errorMsg = dppSolutionResult.msg,
                    paddingValues = paddingValues,
                    state = messageState,
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getAllDppSolutionOld(slug)
                    }) {

                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = PaddingValues())
            }

            is Response.Success -> {
                if (dppSolutionResult.data.isEmpty()) {
                    NoFilesFoundScreen()
                } else {

                    val list = dppSolutionResult.data.sortedBy { it.name }
                    val filteredList = list.filter { it.name.contains(searchText, true) }


//                    val savedStatusMap = remember { mutableStateMapOf<String, Boolean>() }
//
//                    val savedStatusMapWatchLater =
//                        remember { mutableStateMapOf<String, Boolean>() }
//
//
//                    var isOpen by rememberSaveable {
//                        mutableStateOf(false)
//                    }
//
//                    var selectedVideoId by rememberSaveable {
//                        mutableStateOf("")
//                    }
//
//                    var isCompleted by rememberSaveable {
//                        mutableStateOf(false)
//                    }
//
//
//                    if (isOpen) {
//                        BottomSheet(
//                            sheetState = rememberModalBottomSheetState(),
//                            onMarkAsCompletedClicked = {
//                                vm.onMarkAsCompleteClicked(Video(selectedVideoId))
//                                savedStatusMap[selectedVideoId] = !isCompleted
//                                isOpen = false
//
//                            },
//                            onDownloadClicked = {
//                                // Perform download action
//                            },
//                            isCompleted = isCompleted
//                        ) {
//                            isOpen = false
//                        }
//                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LazyColumn {

                            item {
                                SearchTextField(
                                    searchText = searchText,
                                    onSearchTextChanged = { searchText = it },
                                    text = "Search Dpp Solutions",
                                    onCrossIconClicked = {
                                        searchText = ""
                                    }
                                )
                            }
                            items(if (searchText == "") list else filteredList) {

//                                val isComplete = savedStatusMap[it.video_id] ?: false
//
//                                val isSaved = savedStatusMapWatchLater[it.video_id] ?: false

                                EachCardForVideo3(
                                    imageUrl = it.image_url,
                                    title = it.name,
                                    dateCreated = it.created_at.substringBefore("T"),
                                    duration = it.duration,
                                    videoId = it.video_id,
                                    onVideoClicked = {
                                        onClick(
                                            it.video_url,
                                            it.name,
                                            it.external_id,
                                            it.video_id,
                                            it.image_url,
                                            it.created_at,
                                            it.duration,
                                            Constants.PW
                                        )
                                    }
                                )

//                                EachCardForVideo(
//                                    imageUrl = it.image_url,
//                                    title = it.name,
//                                    dateCreated = it.created_at.substringBefore("T"),
//                                    duration = it.duration,
//                                    isCompleted = isComplete,
//                                    onMoreVertClicked = {
//                                        isOpen = true
//                                        selectedVideoId = it.video_id
//                                        isCompleted = isComplete
//                                    },
//                                    isSaved = isSaved,
//                                    onStarClick = {
//                                        vm.onStarClick(
//                                            WatchLaterEntity(
//                                                imageUrl = it.image_url,
//                                                title = it.name,
//                                                dateCreated = it.created_at,
//                                                isComplete = isComplete,
//                                                duration = it.duration,
//                                                videoId = it.video_id,
//                                                videoUrl = it.video_url,
//                                                externalId = it.external_id,
//                                                embedCode = "",
//                                                time = System.currentTimeMillis(),
//                                                isAk = false,
//                                                isKhazana = false,
//                                                isPw = true
//                                            ), context = context
//                                        )
//                                        savedStatusMapWatchLater[it.video_id] = !isSaved
//                                    },
//                                    checkIfSaved = { id ->
//                                        scope.launch {
//                                            val saved = vm.checkIfPresentInWatchLater(id)
//                                            savedStatusMapWatchLater[it.video_id] = saved
//                                        }
//                                    },
//                                    videoId = it.video_id,
//                                    checkIfCompleted = { id ->
//                                        scope.launch {
//                                            val saved =
//                                                vm.checkIfItemIsPresentInVideoDb(Video(id))
//                                            savedStatusMap[it.video_id] = saved
//                                        }
//                                    }
//                                ) {
//                                    onClick(
//                                        it.video_url,
//                                        it.name,
//                                        it.external_id,
//                                        it.video_id,
//                                        it.image_url,
//                                        it.created_at,
//                                        it.duration,
//                                        Constants.PW
//                                    )
//                                }
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
