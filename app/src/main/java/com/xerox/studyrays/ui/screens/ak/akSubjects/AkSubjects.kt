package com.xerox.studyrays.ui.screens.ak.akSubjects

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.ak.AkViewModel
import com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen.EachCardForSubject
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyVerticalGrid


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AkSubjects(
    vm: AkViewModel = hiltViewModel(),
    bid: Int,
    onClick: (Int, Int) -> Unit,
    onBackClick: () -> Unit,
) {

    val state by vm.akSubject.collectAsState()
    val result = state

    LaunchedEffect(key1 = Unit) {
        if (result !is Response.Success) {
            vm.getAkSubject(bid)
        }
    }


    val snackBarHostState = remember { SnackbarHostState() }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        topBar = {
            TopAppBar(title = {
                Text(text = "Subjects")
            }, navigationIcon = {
                IconButton(onClick = {
                    onBackClick()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                }
            })
        }
    ) { it ->

        when (result) {
            is Response.Error -> {

                DataNotFoundScreen(
                    paddingValues = it,
                    errorMsg = result.msg,
                    state = rememberMessageBarState(),
                    shouldShowBackButton = false,
                    onRetryClicked = { vm.getAkSubject(bid) }) {

                }

            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = it)
            }

            is Response.Success -> {

                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {

                    if (result.data.data.batch_subject.isEmpty()) {
                        NoFilesFoundScreen()
                    } else {

                        PullToRefreshLazyVerticalGrid(
                            items = result.data.data.batch_subject,
                            content = {
                                EachCardForSubject(
                                    imageUrl = "https://i.ibb.co/r6812HL/Physics.png",
                                    text = it.subjectName
                                ) {
                                    onClick(it.id, bid)
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            onRefresh = {
                                vm.refreshAkSubjects(id = bid) {
                                    vm.showSnackBar(snackBarHostState)
                                }
                            },
                            gridCells = 3
                        )

                    }

                }

            }
        }
    }
}