package com.xerox.studyrays.ui.screens.pw.favouriteCoursesScreen

import android.os.Build
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.CategoryTabRow2
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.UseNewerVersionScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FavouriteCoursesScreen(
    vm: MainViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onKhazanaClick: (subjectId: String, chapterId: String, imageUrl: String, courseName: String) -> Unit,
    onCLick: (String, String) -> Unit,
) {
    val context = LocalContext.current
    val favCoursesId by vm.allFavCourses.collectAsState()
    val allSubjects by vm.favSubjects.collectAsState()

    val tabListRow = listOf(
        "Physics Wallah",
        "Khazana"
    )

    val pagerState = rememberPagerState(pageCount = { tabListRow.size })

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = favCoursesId) {
        vm.getAllFavCourses()

        if (favCoursesId != null) {
            favCoursesId?.forEach {
                vm.getAllFavSubjects(it?.externalId ?: "")
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = { Text(text = "Favourite courses") },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClicked()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) {


        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            CategoryTabRow2(
                pagerState = pagerState,
                categories = tabListRow,
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

                        when (val subjects = allSubjects) {
                            is ResponseTwo.Error -> {
                                val messageState = rememberMessageBarState()

                                DataNotFoundScreen(
                                    errorMsg = subjects.msg,
                                    state = messageState,
                                    paddingValues = it,
                                    shouldShowBackButton = true,
                                    onRetryClicked = {
                                        processFavCourses(vm = vm, favCoursesId = favCoursesId)
                                    }) {
                                    onBackClicked()
                                }
                            }

                            is ResponseTwo.Loading -> {
                                LoadingScreen(paddingValues = it)
                            }

                            is ResponseTwo.Nothing -> {

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    val composition by rememberLottieComposition(
                                        spec = LottieCompositionSpec.RawRes(
                                            R.raw.comingsoon
                                        )
                                    )

                                    LottieAnimation(
                                        composition = composition,
                                        iterations = LottieConstants.IterateForever,
                                        modifier = Modifier
                                            .size(250.dp)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = "Courses that you add to favourites will appear here",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }

                            is ResponseTwo.Success -> {

                                val savedStatusMap =
                                    remember { mutableStateMapOf<String, Boolean>() }

                                if (vm.favCourseList.isEmpty()) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        Text(
                                            text = "No courses saved",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .align(
                                                    Alignment.Center
                                                )
                                        )
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        val list = vm.favCourseList
                                        LazyColumn {
                                            items(list) { batchItem ->
                                                val isSaved =
                                                    savedStatusMap[batchItem?.externalId] ?: false

                                                if (batchItem != null) {
//                                vm.checkIfItemIsPresent(FavouriteCourse(batchItem.external_id))
                                                    EachCardForFavouriteCourse(
                                                        item = batchItem,
                                                        isSaved = isSaved,
                                                        onFavouriteIconClicked = { value ->
                                                            vm.onFavoriteClick(
                                                                FavouriteCourse(
                                                                    externalId = value
                                                                )
                                                            )
                                                            savedStatusMap[batchItem.externalId] =
                                                                !isSaved
                                                            vm.showToast(
                                                                context,
                                                                if (!isSaved) "${batchItem.name} added to favourites list"
                                                                else "${batchItem.name} removed from favourites list"
                                                            )
                                                        },
                                                        checkIfSaved = {
                                                            scope.launch {
                                                                val saved =
                                                                    vm.checkIfItemIsPresentInDb(
                                                                        FavouriteCourse(it)
                                                                    )
                                                                savedStatusMap[batchItem.externalId] =
                                                                    saved
                                                            }
                                                        }
                                                    ) {
                                                        onCLick(
                                                            batchItem.externalId,
                                                            batchItem.name ?: "Name"
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

                    1 -> {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            KhazanaFavCoursesScreen(
                                onCLick = { subjectId, chapterId, imageUrl, courseName ->
                                    onKhazanaClick(subjectId, chapterId, imageUrl, courseName)

                                })
                        } else {
                            UseNewerVersionScreen()
                        }
                    }
                }
            }
        }
    }
}

fun processFavCourses(vm: MainViewModel, favCoursesId: List<FavouriteCourse?>?) {
    vm.getAllFavCourses()

    if (favCoursesId != null) {
        favCoursesId.forEach {
            vm.getAllFavSubjects(it?.externalId ?: "")
        }
    } else {
        Log.d("TAG", "FavouriteCoursesScreen: all ids are nulll")
    }
}
