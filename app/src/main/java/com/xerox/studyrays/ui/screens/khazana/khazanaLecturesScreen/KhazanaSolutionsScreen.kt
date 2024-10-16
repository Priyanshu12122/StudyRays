package com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
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
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForVideo3
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForVideo3Loading
import com.xerox.studyrays.utils.Constants
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingTemplate
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

    val solutionState by vm.khazanaSolution.collectAsState()
    val videosResult = solutionState

    LaunchedEffect(key1 = Unit) {
        if (videosResult !is Response.Success) {
            vm.getKhazanaSolution(subjectId, chapterId, topicId, topicName = topicName)
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
                    vm.getKhazanaSolution(subjectId, chapterId, topicId, topicName = topicName)
                }) {

            }
        }

        is Response.Loading -> {

            LoadingTemplate() {
                EachCardForVideo3Loading()
            }

        }

        is Response.Success -> {

            Log.d("TAG", "KhazanaSolutionsScreen: result ${videosResult.data}")

            val result = remember { videosResult.data.video_lectures.distinctBy { it.video_id } }

            if (result.isEmpty()) {
                NoFilesFoundScreen()
            } else {
//                val list = videosResult.data.video_lectures
                val filteredList = result.filter { it.video_name.contains(searchText, true) }

                PullToRefreshLazyColumn(
                    item = {
                        SearchTextField(
                            searchText = searchText,
                            onSearchTextChanged = { searchText = it },
                            onCrossIconClicked = { searchText = "" },
                            text = "Search Dpp solutions"
                        )
                    },
                    items = if (searchText == "") result else filteredList,
                    content = {

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
                    }
                )

            }
        }
    }

}