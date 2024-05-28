package com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.CategoryTabRow2
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SubjectsScreen(
    vm: MainViewModel = hiltViewModel(),
    courseId: String,
    onBackIconClicked: () -> Unit,
    onClick: (String, String) -> Unit,
) {

    val scope = rememberCoroutineScope()

    val state by vm.subjects.collectAsState()
    val result = state

    val tabRowList = listOf(
        "Subjects",
        "Teachers"
    )
    val pagerState = rememberPagerState(pageCount = { tabRowList.size })

    LaunchedEffect(key1 = Unit) {
        vm.getAllSubjects(courseId)
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = "Subjects") },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = {
                        onBackIconClicked()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (result) {
            is ResponseTwo.Error -> {
                val messageState = rememberMessageBarState()
                DataNotFoundScreen(
                    paddingValues = paddingValues,
                    errorMsg = result.msg,
                    state = messageState,
                    shouldShowBackButton = false,
                    onRetryClicked = {
                        vm.getAllSubjects(courseId)
                    }) {

                }
            }

            is ResponseTwo.Loading -> {
                LoadingScreen(paddingValues = paddingValues)

            }

            is ResponseTwo.Success -> {

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
//                        pageCount = tabRowList.size,
                        state = pagerState
                    ) { page ->
                        when (page) {
                            0 -> {
                                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                                    items(vm.subjectsList) {
                                        val baseUrl: String? = it?.imageId?.baseUrl
                                        val key: String? = it?.imageId?.key
                                        val imageUrl: String =
                                            if (baseUrl.equals(null) || key.equals(null)) {
                                                "https://i.ibb.co/r6812HL/Physics.png"
                                            } else {
                                                baseUrl + key
                                            }
                                        EachCardForSubject(
                                            imageUrl = imageUrl,
                                            text = it?.subject ?: ""
                                        ) {
                                            onClick(it?._id ?: "", it?.subject ?: "")

                                        }
                                    }
                                }

                            }

                            1 -> {
                                TeacherPagerScreen(list = vm.teachersList)
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

