package com.xerox.studyrays.ui.screens.pw.favouriteCoursesScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFav
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.SpacerHeight
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun KhazanaFavCoursesScreen(
    vm: KhazanaViewModel = hiltViewModel(),
    onCLick: (subjectId: String, chapterId: String, imageUrl: String, courseName: String) -> Unit,
) {

    val favCoursesId by vm.allFavCourses.collectAsState()
    val allSubjects by vm.khazanaFavSubjects.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = favCoursesId) {
        vm.getAllFavCourses()
        if (favCoursesId != null) {
            vm.getAllFavSubjects()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        when (val subjects = allSubjects) {
            is ResponseTwo.Error -> {
                val messageState = rememberMessageBarState()

                DataNotFoundScreen(
                    errorMsg = subjects.msg,
                    state = messageState,
                    shouldShowBackButton = true,
                    onRetryClicked = {
                        processKhazanaFavCourses(vm = vm, favCoursesId = favCoursesId)
                    }) {

                }
            }

            is ResponseTwo.Loading -> {
                LoadingScreen(paddingValues = PaddingValues(0.dp))
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
                            R.raw.datanotfound
                        )
                    )

                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .size(300.dp)
                    )

                    SpacerHeight(dp = 16.dp)

                    Text(
                        text = "Courses that you add to favourites will appear here",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            is ResponseTwo.Success -> {
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

                    val savedStatusMap = remember { mutableStateMapOf<String, Boolean>() }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val list = vm.favCourseList

                        LazyVerticalGrid(columns = GridCells.Fixed(2) ) {
                            items(list) { batchItem: KhazanaFav? ->
                                val isSaved = savedStatusMap[batchItem?.externalId] ?: false

                                if (batchItem != null) {
                                    EachCardForFavKhazanaTeacher(
                                        item = batchItem,
                                        isSaved = isSaved,
                                        onFavouriteIconClicked = { value ->
                                            vm.onFavoriteClick(
                                                KhazanaFav(
                                                    externalId = value,
                                                    imageUrl = batchItem.imageUrl,
                                                    subjectId = batchItem.subjectId,
                                                    chapterId = batchItem.externalId,
                                                    courseName = batchItem.courseName,
                                                    totalLectures = batchItem.totalLectures
                                                )
                                            )
                                            savedStatusMap[batchItem.externalId] = !isSaved
                                            vm.showToast(
                                                context,
                                                if (!isSaved) "${batchItem.courseName} added to favourites list"
                                                else "${batchItem.courseName} removed from favourites list"
                                            )
                                        },
                                        checkIfSaved = {
                                            scope.launch {
                                                val saved =
                                                    vm.checkIfItemIsPresentInDb(it)
                                                savedStatusMap[batchItem.externalId] = saved
                                            }
                                        }
                                    ) {
                                        onCLick(
                                            batchItem.subjectId,
                                            batchItem.externalId,
                                            batchItem.imageUrl,
                                            batchItem.courseName
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
}

@RequiresApi(Build.VERSION_CODES.O)
fun processKhazanaFavCourses(vm: KhazanaViewModel, favCoursesId: List<KhazanaFav?>?) {
    vm.getAllFavCourses()

    if (favCoursesId != null) {
        favCoursesId.forEach {
            vm.getAllFavSubjects(/*it?.externalId ?: ""*/)
        }
    } else {
        Log.d("TAG", "FavouriteCoursesScreen: all ids are null")
    }
}