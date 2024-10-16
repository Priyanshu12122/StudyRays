package com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen.old

import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen.EachCardForSubject
import com.xerox.studyrays.utils.CategoryTabRow2
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyVerticalGrid
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SubjectsScreenOld(
    vm: MainViewModel = hiltViewModel(),
    courseId: String,
    onBackIconClicked: () -> Unit,
    onAnnouncementsClicked: (String) -> Unit,
    onClick: (String, String) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val state by vm.subjectsOld.collectAsState()
    val result = state

    val tabRowList = listOf(
        "Subjects",
        "Teachers"
    )
    val pagerState = rememberPagerState(pageCount = { tabRowList.size })
    val snackbarHostState = remember { SnackbarHostState() }

    var url by rememberSaveable {
        mutableStateOf("")
    }

    var batchId by rememberSaveable {
        mutableStateOf("")
    }

    val navBarState by vm.navBar.collectAsState()
    val navResult = navBarState

    LaunchedEffect(key1 = Unit) {
        if (result !is ResponseTwo.Success) {
            vm.getAllSubjectsOld(courseId = courseId)
        }

        if (navResult !is ResponseTwo.Success) {
            vm.getNavItems()
        }
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    when (navResult) {
        is ResponseTwo.Error -> {}
        is ResponseTwo.Loading -> {}
        is ResponseTwo.Nothing -> {}
        is ResponseTwo.Success -> {
            url = navResult.data.telegram
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Subjects") },
                scrollBehavior = scrollBehavior,
                actions = {

                    IconButton(onClick = {
                        if (url == "") {
                            vm.showToast(
                                context,
                                "Some error occurred, Please try reloading the page"
                            )
                        } else {
                            vm.openUrl(context, url)
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.telegram),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    IconButton(onClick = {
                        if (batchId == "") {
                            vm.showToast(
                                context,
                                "Some error occurred, Please try reloading the page"
                            )
                        } else {
                            onAnnouncementsClicked(batchId)
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.megaphone),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp)
                        )
                    }


                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackIconClicked()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (result) {
            is ResponseTwo.Error -> {
                Log.d("TAG", "SubjectsScreenOld: ${result.msg}")
                val messageState = rememberMessageBarState()
                DataNotFoundScreen(
                    paddingValues = paddingValues,
                    errorMsg = result.msg,
                    state = messageState,
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getAllSubjectsOld(
                            courseId = courseId
                        )
                    }) {

                }
            }

            is ResponseTwo.Loading -> {
                LoadingScreen(paddingValues = paddingValues)

            }

            is ResponseTwo.Success -> {


                LaunchedEffect(key1 = Unit) {
                    batchId = result.data?.batch_details?.external_id ?: ""

                    Log.d("TAG", "SubjectsScreenOld: ${result.data}")
                }


                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    CategoryTabRow2(
                        pagerState = pagerState,
                        categories = tabRowList,
                        onTabClicked = {
                            scope.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        }
                    )
                    HorizontalPager(
                        state = pagerState
                    ) { page ->
                        when (page) {
                            0 -> {
                                if (vm.subjectsListOld.isEmpty()) {
                                    NoFilesFoundScreen()
                                } else {

                                    PullToRefreshLazyVerticalGrid(
                                        items = vm.subjectsListOld,
                                        content = {
                                            val baseUrl: String? = it?.imageId?.baseUrl
                                            val key: String? = it?.imageId?.key
                                            val imageUrl: String =
                                                if (baseUrl.equals(null) || key.equals(null)) {
                                                    "https://i.ibb.co/r6812HL/Physics.png"
                                                } else {
                                                    baseUrl + key
                                                }
                                            EachCardForSubject(
                                                imageUrl = imageUrl
                                                    ?: "https://i.ibb.co/r6812HL/Physics.png",
                                                text = it?.subject ?: ""
                                            ) {
                                                onClick(
                                                    it?._id ?: "",
                                                    it?.subject ?: ""
                                                )

                                            }
                                        },
                                        isRefreshing = vm.isRefreshing,
                                        onRefresh = {
                                            vm.refreshAllSubjectsOld(
                                                courseId = courseId,
                                                onComplete = {
                                                    vm.showSnackBar(snackbarHostState)
                                                })
                                        },
                                        gridCells = 3
                                    )

                                }

                            }

                            1 -> {
                                if (vm.teachersListOld.isEmpty()) {
                                    NoFilesFoundScreen()
                                } else {
                                    TeacherPagerScreenOld(list = vm.teachersListOld)
                                }
                            }
                        }
                    }
                }
            }

            is ResponseTwo.Nothing -> {
                Text(text = "Sorry, this is Unavailable")
            }

        }

    }

}

