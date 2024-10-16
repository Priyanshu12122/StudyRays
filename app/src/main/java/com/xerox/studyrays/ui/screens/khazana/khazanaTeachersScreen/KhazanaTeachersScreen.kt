package com.xerox.studyrays.ui.screens.khazana.khazanaTeachersScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFav
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen.EachCardForSubjectLoading
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingTemplate
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyVerticalGrid
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KhazanaTeachersScreen(
    vm: KhazanaViewModel = hiltViewModel(),
    id: String,
    subject: String,
    onBackClicked: () -> Unit,
    onClick: (subjectId: String, chapterId: String, imageUrl: String, courseName: String) -> Unit,
) {

    val state by vm.khazanaTeachers.collectAsState()
    val result = state

    LaunchedEffect(key1 = Unit) {
        if (result !is Response.Success) {
            vm.getKhazanaTeachers(id)
        }
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        },
        topBar = {
            TopAppBar(
                title = { Text(text = subject) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
                })
        }
    ) { paddingValues ->

        when (result) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()
                DataNotFoundScreen(
                    errorMsg = result.msg,
                    paddingValues = paddingValues,
                    state = messageState,
                    shouldShowBackButton = true,
                    onRetryClicked = {
                        vm.getKhazanaTeachers(id)
                    }) {
                    onBackClicked()
                }
            }

            is Response.Loading -> {

                Column {
                    LazyVerticalGrid(GridCells.Fixed(2)) {
                        items(10){
                            EachCardForKhazanaTeacherLoading()
                        }
                    }
                }


            }

            is Response.Success -> {

                if (result.data.isEmpty()) {
                    NoFilesFoundScreen()
                } else {
                    val savedStatusMap =
                        remember { mutableStateMapOf<String, Boolean>() }
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    )
                    {

                        PullToRefreshLazyVerticalGrid(
                            items = result.data,
                            content = { item ->
                                val isSaved = savedStatusMap[item.external_id] ?: false
                                EachCardForKhazanaTeacher(
                                    item = item,
                                    isSaved = isSaved,
                                    onFavouriteIconClicked = { value ->
                                        vm.onFavoriteClick(
                                            item = KhazanaFav(
                                                externalId = value,
                                                imageUrl = item.imageId_baseUrl + item.imageId_key,
                                                subjectId = item.subjectId,
                                                chapterId = item.external_id,
                                                courseName = item.name + " " + item.description.substringBefore(
                                                    ";"
                                                ),
                                                totalLectures = item.totalLectures
                                            )
                                        )
                                        savedStatusMap[item.external_id] = !isSaved
                                        vm.showToast(
                                            context,
                                            if (!isSaved) "${
                                                item.name + " " + item.description.substringBefore(
                                                    ";"
                                                )
                                            } added to favourites list"
                                            else "${
                                                item.name + " " + item.description.substringBefore(
                                                    ";"
                                                )
                                            } removed from favourites list"
                                        )

                                    },
                                    checkIfSaved = {
                                        scope.launch {
                                            val saved =
                                                vm.checkIfItemIsPresentInDb(it)
                                            savedStatusMap[item.external_id] = saved
                                        }
                                    }
                                ) {
                                    onClick(
                                        item.subjectId,
                                        item.external_id,
                                        item.imageId_baseUrl + item.imageId_key,
                                        item.name + " " + item.description.substringBefore(";")
                                    )
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            gridCells = 2,
                            onRefresh = {
                                vm.refreshKhazanaTeachers(id) {
                                    vm.showSnackBar(snackBarHostState)
                                }
                            })

                    }

                }

            }
        }

    }

}