package com.xerox.studyrays.ui.screens.pw.lecturesScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.Constants
import com.xerox.studyrays.utils.Constants.PAGE_SIZE
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumnForLecturesScreen
import com.xerox.studyrays.utils.SearchTextField

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualLecturesScreen(
    vm: MainViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    topicSlug: String,
    subjectSlug: String,
    batchId: String,
    onVideoClicked: (String, String, String, String, String, String, String, String, String) -> Unit,
) {


    val videosState by vm.videos.collectAsState()
    val videosResult = videosState


//    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        if (videosResult !is Response.Success) {
            vm.getAllVideos(topicSlug, subjectSlug, batchId)
        }
    }


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
                        vm.getAllVideos(
                            batchId = batchId,
                            subjectSlug = subjectSlug,
                            topicSlug = topicSlug,
                        )
                    }) {
                    onBackClicked()
                }

            }

        }

        is Response.Loading -> {

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                LazyColumn {
                    items(10) {
                        EachCardForVideo3Loading()
                    }
                }

            }

        }

        is Response.Success -> {

            val state = vm.state
            val result = vm.state.items

            SideEffect {
                Log.d("TAG", "Success with ${result.size} items")
                Log.d("TAG", "result $result")
            }

            if (result.isEmpty()) {
                NoFilesFoundScreen()
            } else {

                val videoList = result.sortedBy { it.createdAt }
                val filteredList = videoList.filter { it.video_name.contains(searchText, true) }

//                LazyColumn {
//                    itemsIndexed(videoList) { index, video ->
//
//                        vm.onChangeScrollPosition(index)
//                        if ((index + 1) >= (state.page * PAGE_SIZE) && !state.isLoading) {
//                            vm.loadNextItems(
//                                topicSlug = topicSlug,
//                                subjectSlug = subjectSlug,
//                                batchId = batchId
//                            )
//                        }
//                        EachCardForVideo3(
//                            imageUrl = video.video_image,
//                            title = video.video_name,
//                            dateCreated = video.upload_date.substringBefore("T"),
//                            duration = video.duration,
//                            videoId = video.video_id,
//                            onVideoClicked = {
//                                onVideoClicked(
//                                    video.videoUrl,
//                                    video.video_name,
//                                    video.video_external_id,
//                                    video.embed_code,
//                                    video.video_id,
//                                    video.video_image,
//                                    video.createdAt,
//                                    video.duration,
//                                    Constants.PW
//                                )
//                            }
//                        )
//
//                    }
//
//                    item {
//                        if (state.isLoading) {
//                            CircularProgressIndicator(modifier = Modifier.size(60.dp))
//                        }
//                    }
//
//                }

                PullToRefreshLazyColumnForLecturesScreen(
//                    lazyListState = listState,
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
                    item2 = {
                        if (state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(50.dp))
                        }
                    },
                    items = if (searchText == "") videoList else filteredList,
                    content = { index, video ->

                        vm.onChangeScrollPosition(index)
                        if ((index + 1) >= (state.page * PAGE_SIZE) && !state.isLoading) {
                            vm.loadNextItems(
                                topicSlug = topicSlug,
                                subjectSlug = subjectSlug,
                                batchId = batchId
                            )
                        }

                        EachCardForVideo3(
                            imageUrl = video.video_image,
                            title = video.video_name,
                            dateCreated = video.upload_date.substringBefore("T"),
                            duration = video.duration,
                            videoId = video.video_id,
                            onVideoClicked = {
                                onVideoClicked(
                                    video.videoUrl,
                                    video.video_name,
                                    video.video_external_id,
                                    video.embed_code,
                                    video.video_id,
                                    video.video_image,
                                    video.createdAt,
                                    video.duration,
                                    Constants.PW
                                )
                            }
                        )
                    },
                    isRefreshing = vm.isRefreshing,
                    onRefresh = {
                        vm.refreshAllVideos(
                            batchId = batchId,
                            subjectSlug = subjectSlug,
                            topicSlug = topicSlug,
                        ) {
                            vm.showSnackBar(snackbarHostState)
                        }
                    })

            }
        }
    }
}