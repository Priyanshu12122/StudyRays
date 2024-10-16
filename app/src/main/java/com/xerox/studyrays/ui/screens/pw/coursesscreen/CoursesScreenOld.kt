package com.xerox.studyrays.ui.screens.pw.coursesscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.xerox.studyrays.utils.Category2
import com.xerox.studyrays.utils.CategoryTabRow44
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CoursesScreenOld(
    classValue: String,
    vm: MainViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onNavigateToKeyScreen: () -> Unit,
    onCLick: (String, String) -> Unit,
) {

    val state by vm.coursesOld.collectAsState()
    val result = state

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        if (result !is Response.Success){
            vm.getClassesOld(classValue)
        }

        vm.checkStartDestinationDuringNavigation(
            onNavigate = {
                onNavigateToKeyScreen()
            }
        )

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
        when (result) {
            is Response.Error -> {

                val messageState = rememberMessageBarState()

                DataNotFoundScreen(
                    errorMsg = result.msg,
                    state = messageState,
                    paddingValues = it,
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


                val allCourses: MutableList<Category2> = mutableListOf()

                val pagerState = rememberPagerState { allCourses.size }

                if (vm.jeeCoursesOld.isNotEmpty()) {
                    allCourses.add(
                        Category2(
                            name = "JEE",
                            category = vm.jeeCoursesOld
                        )
                    )
                }

                if (vm.neetCoursesOld.isNotEmpty()) {
                    allCourses.add(
                        Category2(
                            name = "NEET",
                            category = vm.neetCoursesOld
                        )
                    )
                }

                if (vm.boardCoursesOld.isNotEmpty()) {
                    allCourses.add(
                        Category2(
                            name = "Board",
                            category = vm.boardCoursesOld // Ensure to convert it to a list
                        )
                    )
                }

                if (vm.vidyapeethCoursesOld.isNotEmpty()) {
                    allCourses.add(
                        Category2(
                            name = "VidyaPeeth",
                            category = vm.vidyapeethCoursesOld // Ensure to convert it to a list
                        )
                    )
                }


// For freeCourses:
                if (vm.freeCoursesOld.isNotEmpty()) {
                    allCourses.add(
                        Category2(
                            name = "Free",
                            category = vm.freeCoursesOld // Ensure to convert it to a list
                        )
                    )
                }

// For youtubeCourses:
                if (vm.youtubeCoursesOld.isNotEmpty()) {
                    allCourses.add(
                        Category2(
                            name = "Youtube",
                            category = vm.youtubeCoursesOld // Ensure to convert it to a list
                        )
                    )
                }

// For otherCourses:
                if (vm.otherCoursesOld.isNotEmpty()) {
                    allCourses.add(
                        Category2(
                            name = "Other",
                            category = vm.otherCoursesOld // Ensure to convert it to a list
                        )
                    )
                }


                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {

                    if (allCourses.size <= 0) {
                        NoFilesFoundScreen()
                    } else {
                        CategoryTabRow44(
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
                        ) { it ->
                            val list = remember {
                                allCourses[it].category.sortedByDescending { it.id }
                            }
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
                                    content = { batchItem ->
                                        val isSaved = savedStatusMap[batchItem.external_id] ?: false
                                        EachCourseCardInClass2(
                                            item = batchItem,
                                            isSaved = isSaved,
                                            onFavouriteIconClicked = { value ->
                                                vm.onFavoriteClick(
                                                    FavouriteCourse(
                                                        externalId = value,
                                                        name = batchItem.name,
                                                        byName = batchItem.byName,
                                                        language = batchItem.language,
                                                        imageUrl = batchItem.previewImage_baseUrl + batchItem.previewImage_key,
                                                        slug = batchItem.slug,
                                                        classValue = batchItem.`class`,
                                                        isOld = true
                                                    )
                                                )
                                                savedStatusMap[batchItem.external_id] = !isSaved
                                                vm.showToast(
                                                    context,
                                                    if (!isSaved) "${batchItem.name} added to favourites list"
                                                    else "${batchItem.name} removed from favourites list"
                                                )
                                            },
                                            checkIfSaved = {
                                                scope.launch {
                                                    val saved =
                                                        vm.checkIfItemIsPresentInDb(it)
                                                    savedStatusMap[batchItem.external_id] = saved
                                                }
                                            }
                                        ) {

                                            onCLick(
                                                batchItem.external_id,
                                                batchItem.name
                                            )
//                                            }

                                        }
                                    },
                                    isRefreshing = vm.isRefreshing,
                                    onRefresh = {
                                        vm.refreshAllCourses(classValue = classValue) {
                                            vm.showSnackBar(snackBarHostState)
                                        }
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}

