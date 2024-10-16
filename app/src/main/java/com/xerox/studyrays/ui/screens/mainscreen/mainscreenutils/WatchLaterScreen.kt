package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.db.watchLaterDb.WatchLaterEntity
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.EachCardForVideo
import com.xerox.studyrays.utils.CategoryTabRow2
import com.xerox.studyrays.utils.Constants
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WatchLaterScreen(
    modifier: Modifier = Modifier,
    vm: MainViewModel = hiltViewModel(),
    onVideoClicked: (String, String, String, String, String, String, String, String, String, Boolean) -> Unit,
    onBackClick: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getAllWatchLater()
    }

    val categories = listOf("Physics wallah", "Khazana")
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val scope = rememberCoroutineScope()
    val state by vm.watchLater.collectAsState()
    val result = state

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            TopAppBar(title = { Text(text = "Watch later") }, navigationIcon = {
                IconButton(onClick = {
                    onBackClick()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        when (result) {
            is Response.Error -> {
                DataNotFoundScreen(
                    paddingValues = paddingValues,
                    state = rememberMessageBarState(),
                    shouldShowBackButton = false,
                    onRetryClicked = { vm.getAllWatchLater() }) {

                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = PaddingValues())
            }

            is Response.Success -> {

                Column(
                    modifier = modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    CategoryTabRow2(pagerState = pagerState, categories = categories,
                        onTabClicked = {
                            scope.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        })

                    HorizontalPager(
                        state = pagerState
                    ) { page ->
                        when (page) {

                            0 -> {

                                if (vm.pwWatchLater.isEmpty()) {
                                    NoFilesFoundScreen()
                                } else {

                                    PwScreen(
                                        videos = vm.pwWatchLater.sortedByDescending { it.time },
                                        vm = vm,
                                        onVideoClicked = { url, title, id, embedCode, videoId, imageUrl, createdAt, duration, isOld ->
                                            onVideoClicked(
                                                url,
                                                title,
                                                id,
                                                embedCode,
                                                videoId,
                                                imageUrl,
                                                createdAt,
                                                duration,
                                                Constants.PW,
                                                isOld

                                            )


                                        }
                                    )
                                }


                            }

                            1 -> {

                                if (vm.khazanaWatchLater.isEmpty()) {
                                    NoFilesFoundScreen()
                                } else {
                                    PwScreen(
                                        videos = vm.khazanaWatchLater.sortedByDescending { it.time },
                                        vm = vm,
                                        onVideoClicked = { url, title, id, embedCode, videoId, imageUrl, createdAt, duration, isOld ->
                                            onVideoClicked(
                                                url,
                                                title,
                                                id,
                                                embedCode,
                                                videoId,
                                                imageUrl,
                                                createdAt,
                                                duration,
                                                Constants.KHAZANA,
                                                isOld
                                            )


                                        }
                                    )
                                }

                            }



                        }

                    }
                }


            }

        }

    }
}


@Composable
fun PwScreen(
    modifier: Modifier = Modifier,
    videos: List<WatchLaterEntity>,
    vm: MainViewModel,
    onVideoClicked: (String, String, String, String, String, String, String, String, Boolean) -> Unit,
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    val savedStatusMapWatchLater =
        remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn {
        items(videos) { video ->

            val isSaved = savedStatusMapWatchLater[video.videoId] ?: false

            EachCardForVideo(
                imageUrl = video.imageUrl,
                title = video.title,
                dateCreated = video.dateCreated,
                duration = video.duration,
                videoId = video.videoId,
                isSaved = isSaved,
                onStarClick = {
                    vm.onStarClick(
                        WatchLaterEntity(
                            imageUrl = video.imageUrl,
                            title = video.title,
                            dateCreated = video.dateCreated,
                            duration = video.duration,
                            videoId = video.videoId,
                            videoUrl = video.videoUrl,
                            externalId = video.externalId,
                            embedCode = video.embedCode,
                            time = System.currentTimeMillis(),
                            isKhazana = video.isKhazana,
                            isPw = video.isPw,
                            isOld = video.isOld
                        ),
                        context = context
                    )
                    savedStatusMapWatchLater[video.videoId] = !isSaved
                },

                checkIfSaved = { id ->
                    scope.launch {
                        val saved = vm.checkIfPresentInWatchLater(id)
                        savedStatusMapWatchLater[video.videoId] = saved
                    }

                },
                onVideoClicked = {
                    onVideoClicked(
                        video.videoUrl,
                        video.title,
                        video.externalId,
                        video.embedCode,
                        video.videoId,
                        video.imageUrl,
                        video.dateCreated,
                        video.duration,
                        video.isOld
                    )
                }
            )
        }

    }
}