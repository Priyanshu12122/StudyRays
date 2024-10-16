package com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.ui.screens.khazana.khazanaTeachersScreen.EachCardForKhazanaTeacherLoading
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForVideo3
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForVideo3Loading
import com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen.EachCardForSubjectLoading
import com.xerox.studyrays.utils.Constants
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.LoadingTemplate
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn
import com.xerox.studyrays.utils.SearchTextField

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActualKhazanaLecturesScreen(
    vm: KhazanaViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    subjectId: String,
    chapterId: String,
    topicId: String,
    topicName: String,
    pullToRefreshState: PullToRefreshState,
    paddingValues: PaddingValues,
    onVideoClicked: (String, String, String, String, String, String, String) -> Unit,
) {

    val videoState by vm.khazanaVideo.collectAsState()
    val videosResult = videoState

    LaunchedEffect(key1 = Unit) {
        if (videosResult !is Response.Success) {
            vm.getKhazanaVideos(subjectId, chapterId, topicId, topicName = topicName)
        }
    }


    var searchText by rememberSaveable { mutableStateOf("") }

    when (videosResult) {
        is Response.Error -> {
            val messageState = rememberMessageBarState()

            DataNotFoundScreen(
                errorMsg = videosResult.msg,
                state = messageState,
                paddingValues = paddingValues,
                shouldShowBackButton = false,
                onRetryClicked = {
                    vm.getKhazanaVideos(subjectId, chapterId, topicId, topicName = topicName)
                }) {

            }
        }

        is Response.Loading -> {

            LoadingTemplate() {
                EachCardForVideo3Loading()
            }

        }

        is Response.Success -> {

            val result = remember {
                videosResult.data
            }

            if (result.isEmpty()) {
                NoFilesFoundScreen()
            } else {
                val list = result.filter {
                    it.video_image != null || it.video_name != null || it.video_created_at != null || it.video_duration != null || it.video_url != null
                }.distinctBy { it.video_id }
                    .sortedBy { it.chapter_name }

                val filteredList = list.filter {
                    it.video_name?.contains(searchText, true)
                        ?: false
                }


                PullToRefreshLazyColumn(
                    item = {
                        SearchTextField(
                            searchText = searchText,
                            onSearchTextChanged = { searchText = it },
                            text = "Search Lectures",
                            onCrossIconClicked = { searchText = "" }
                        )
                    },
                    pullToRefreshState = pullToRefreshState,
                    items = if (searchText == "") list else filteredList,
                    content = {


                        EachCardForVideo3(
                            imageUrl = it.video_image,
                            title = it.video_name,
                            dateCreated = it.video_created_at?.substring(11),
                            duration = it.video_duration,
                            onVideoClicked = {
                                onVideoClicked(
                                    it.video_url ?: "",
                                    it.video_name ?: "",
                                    it.video_id,
                                    it.video_image ?: "",
                                    it.video_created_at?.substring(11)
                                        ?: "no data",
                                    it.video_duration ?: "duration not available",
                                    Constants.KHAZANA
                                )
                            },
                            videoId = it.video_id
                        )

                    },
                    isRefreshing = vm.isRefreshing,
                    onRefresh = {
                        vm.refreshKhazanaVideos(
                            subjectId = subjectId,
                            chapterId = chapterId,
                            topicId = topicId,
                            topicName = topicName
                        ) {
                            vm.showSnackBar(snackbarHostState)
                        }
                    }
                )

            }

        }
    }


}