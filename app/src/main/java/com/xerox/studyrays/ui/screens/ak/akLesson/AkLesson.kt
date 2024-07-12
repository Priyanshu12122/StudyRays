package com.xerox.studyrays.ui.screens.ak.akLesson

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.ak.AkViewModel
import com.xerox.studyrays.ui.theme.MainPurple
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.PullToRefreshLazyColumn

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AkLesson(
    vm: AkViewModel = hiltViewModel(),
    sid: Int,
    bid: Int,
    onClick: (Int, Int, Int) -> Unit,
    onBackClick: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getAkLesson(sid = sid, bid = bid)
    }

    val state by vm.akLesson.collectAsState()
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
                Text(text = "Lessons")
            }, navigationIcon = {
                IconButton(onClick = {
                    onBackClick()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            },
                scrollBehavior = scrollBehavior
            )
        }
    ) {

        when (val result = state) {
            is Response.Error -> {

                DataNotFoundScreen(
                    paddingValues = it,
                    errorMsg = result.msg,
                    state = rememberMessageBarState(),
                    shouldShowBackButton = false,
                    onRetryClicked = { vm.getAkLesson(sid = sid, bid = bid) }) {

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

                    if (result.data.data.batch_topic.isEmpty()) {
                        NoFilesFoundScreen()
                    } else {

                        PullToRefreshLazyColumn(
                            items = result.data.data.batch_topic,
                            content = {
                                EachCardForAkLessons(
                                    lessonName = it.topicName,
                                    classes = it.clsCount.toString()
                                ) {

                                    onClick(
                                        result.data.data.batch_detail.subjectId,
                                        it.id,
                                        result.data.data.batch_detail.batchId
                                    )

                                }
                            },
                            isRefreshing = vm.isRefreshing,
                            onRefresh = {
                                vm.refreshAkLesson(bid = bid, sid = sid) {
                                    vm.showSnackBar(snackBarHostState)
                                }
                            })

                    }


                }

            }
        }
    }


}


@Composable
fun EachCardForAkLessons(
    lessonName: String,
    classes: String,
    onClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
            )
            .clip(RoundedCornerShape(10.dp))
            .background(if (!isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.background)
            .clickable {
                onClick()
            }
            .then(
                if (isSystemInDarkTheme()) {
                    Modifier.border(
                        1.dp,
                        Color.White.copy(0.6f),
                        RoundedCornerShape(10.dp)
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Divider(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
                    .width(3.5.dp),
                thickness = 0.dp,
                color = MainPurple
            )

            Column(
                modifier = Modifier.weight(7f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = lessonName,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MainPurple,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 3.dp)
                        .weight(1f)
                )


                Text(
                    text = "$classes Classes",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 8.dp, top = 3.dp)
                        .weight(1f)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "",
                modifier = Modifier.weight(
                    1f
                )
            )

        }

    }
}