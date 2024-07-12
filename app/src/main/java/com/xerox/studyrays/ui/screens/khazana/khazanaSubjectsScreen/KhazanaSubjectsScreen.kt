package com.xerox.studyrays.ui.screens.khazana.khazanaSubjectsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.xerox.studyrays.ui.screens.khazana.KhazanaViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KhazanaSubjectsScreen(
    vm: KhazanaViewModel = hiltViewModel(),
    slug: String,
    onBackClicked: () -> Unit,
    onClick: (String, String) -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getKhazanaSubjects(slug)
    }

    val khazanaSubjectState by vm.khazanaSubject.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }

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
                title = { Text(text = "Subjects List") },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClicked()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {


        when (val result = khazanaSubjectState) {
            is Response.Error -> {
                val messageState = rememberMessageBarState()

                DataNotFoundScreen(
                    errorMsg = result.msg,
                    paddingValues = it,
                    state = messageState,
                    shouldShowBackButton = true,
                    onRetryClicked = {
                        vm.getKhazanaSubjects(slug)
                    }) {
                    onBackClicked()

                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = it)
            }

            is Response.Success -> {

                if (result.data.isEmpty()) {
                    NoFilesFoundScreen()
                } else {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                    ) {

                        PullToRefreshLazyColumn(
                            items = result.data,
                            content = { subjectItem ->
                                CardForKhazanaSubject(item = subjectItem) {
                                    onClick(subjectItem.name, subjectItem.id)
                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            onRefresh = {
                                vm.refreshKhazanaSubjects(slug) {
                                    vm.showSnackBar(snackBarHostState)
                                }
                            })
                    }

                }

            }
        }
    }
}