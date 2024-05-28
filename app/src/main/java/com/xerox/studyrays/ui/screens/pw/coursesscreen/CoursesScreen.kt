package com.xerox.studyrays.ui.screens.pw.coursesscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.Category
import com.xerox.studyrays.utils.CategoryTabRow
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.EmptyScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CoursesScreen(
    classValue: String,
    vm: MainViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onCLick: (String, String) -> Unit,
) {

    val state by vm.courses.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        vm.getClasses(classValue)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Batch List For Class $classValue") },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClicked()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }) { it ->
        when (val result = state) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()

                DataNotFoundScreen(
                    errorMsg = result.msg,
                    state = messageState,
                    shouldShowBackButton = true,
                    onRetryClicked = {
                        vm.getClasses(classValue)
                    }) {
                    onBackClicked()
                }
            }

            is Response.Loading -> {

                LoadingScreen(paddingValues = it)

            }

            is Response.Success -> {


                val allCourses: MutableList<Category> = mutableListOf()

                val pagerState = rememberPagerState { allCourses.size }

                if (vm.jeeCourses.isNotEmpty()) {
                    allCourses.add(
                        Category(
                            name = "JEE",
                            category = vm.jeeCourses
                        )
                    )
                }

                if (vm.neetCourses.isNotEmpty()) {
                    allCourses.add(
                        Category(
                            name = "NEET",
                            category = vm.neetCourses
                        )
                    )
                }

                if (vm.boardCourses.isNotEmpty()) {
                    allCourses.add(
                        Category(
                            name = "Board",
                            category = vm.boardCourses // Ensure to convert it to a list
                        )
                    )
                }

                if (vm.vidyapeethCourses.isNotEmpty()) {
                    allCourses.add(
                        Category(
                            name = "VidyaPeeth",
                            category = vm.vidyapeethCourses // Ensure to convert it to a list
                        )
                    )
                }


// For freeCourses:
                if (vm.freeCourses.isNotEmpty()) {
                    allCourses.add(
                        Category(
                            name = "Free",
                            category = vm.freeCourses // Ensure to convert it to a list
                        )
                    )
                }

// For youtubeCourses:
                if (vm.youtubeCourses.isNotEmpty()) {
                    allCourses.add(
                        Category(
                            name = "Youtube",
                            category = vm.youtubeCourses // Ensure to convert it to a list
                        )
                    )
                }

// For otherCourses:
                if (vm.otherCourses.isNotEmpty()) {
                    allCourses.add(
                        Category(
                            name = "Other",
                            category = vm.otherCourses // Ensure to convert it to a list
                        )
                    )
                }


                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {

                    if (allCourses.size <= 0) {
                        EmptyScreen()
                        return@Scaffold
                    } else {
                        CategoryTabRow(
                            pagerState = pagerState,
                            categories = allCourses,
                            onTabClicked = {
                                scope.launch {
                                    pagerState.animateScrollToPage(it)
                                }
                            },
                        )

                        HorizontalPager(
                            state = pagerState
                        ) {
                            val list = allCourses[it].category
                            if (list.isEmpty()) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        text = "No items in this Category",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            } else {
                                val savedStatusMap =
                                    remember { mutableStateMapOf<String, Boolean>() }

                                PullToRefreshLazyColumn(
                                    items = list,
                                    content = {batchItem->
                                        val isSaved = savedStatusMap[batchItem.externalId] ?: false
                                        EachCourseCardInClass(
                                            item = batchItem,
                                            isSaved = isSaved,
                                            onFavouriteIconClicked = { value ->
                                                vm.onFavoriteClick(FavouriteCourse(externalId = value))
                                                savedStatusMap[batchItem.externalId] = !isSaved
                                                vm.showToast(
                                                    context,
                                                    if (!isSaved) "${batchItem.name} added to favourites list" else "${batchItem.name} removed from favourites list"
                                                )
                                            },
                                            checkIfSaved = {
                                                scope.launch {
                                                    val saved =
                                                        vm.checkIfItemIsPresentInDb(
                                                            FavouriteCourse(
                                                                it
                                                            )
                                                        )
                                                    savedStatusMap[batchItem.externalId] = saved
                                                }
                                            }
                                        ) {
                                            onCLick(batchItem.externalId, batchItem.name)
                                        }
                                    },
                                    isRefreshing = vm.isRefreshing,
                                    onRefresh = { vm.refreshAllCourses(classValue = classValue){
                                        vm.showSnackBar(snackBarHostState)
                                    } })

//                                LazyColumn {
//                                    items(list) { batchItem ->
//                                        val isSaved = savedStatusMap[batchItem.externalId] ?: false
//
//                                        EachCourseCardInClass(
//                                            item = batchItem,
//                                            isSaved = isSaved,
//                                            onFavouriteIconClicked = { value ->
//                                                vm.onFavoriteClick(FavouriteCourse(externalId = value))
//                                                savedStatusMap[batchItem.externalId] = !isSaved
//                                                vm.showToast(
//                                                    context,
//                                                    if (!isSaved) "${batchItem.name} added to favourites list" else "${batchItem.name} removed from favourites list"
//                                                )
//                                            },
//                                            checkIfSaved = {
//                                                scope.launch {
//                                                    val saved =
//                                                        vm.checkIfItemIsPresentInDb(
//                                                            FavouriteCourse(
//                                                                it
//                                                            )
//                                                        )
//                                                    savedStatusMap[batchItem.externalId] = saved
//                                                }
//                                            }
//                                        ) {
//                                            onCLick(batchItem.externalId, batchItem.name)
//                                        }
//                                    }
//                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

