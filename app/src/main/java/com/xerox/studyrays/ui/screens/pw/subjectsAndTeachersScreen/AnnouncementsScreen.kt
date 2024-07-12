package com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.model.pwModel.announcementsItem.AnnouncementItem
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen
import com.xerox.studyrays.utils.toTimeAgo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementsScreen(
    modifier: Modifier = Modifier,
    vm: MainViewModel = hiltViewModel(),
    batchId: String,
    isOld: Boolean,
    onBackClick: () -> Unit,
    ) {

    LaunchedEffect(key1 = Unit) {
        vm.getAnnouncements(batchId,isOld)
    }

    val state by vm.announcements.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text(text = "Announcements") },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "null"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        when (val result = state) {
            is Response.Error -> {
                DataNotFoundScreen(
                    errorMsg = result.msg,
                    state = rememberMessageBarState(),
                    paddingValues = paddingValues,
                    shouldShowBackButton = false,
                    onRetryClicked = { vm.getAnnouncements(batchId,isOld) }) {

                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = paddingValues)
            }

            is Response.Success -> {

                if (result.data.isNullOrEmpty()) {
                    NoFilesFoundScreen()
                } else {
                    Column(modifier = modifier.padding(paddingValues)) {
                        LazyColumn {

                            item {
                                HorizontalDivider(
                                    modifier = modifier.padding(
                                        horizontal = 20.dp,
                                        vertical = 8.dp
                                    ), thickness = 2.dp
                                )
                            }

                            items(result.data.sortedByDescending { it.created_at }) {
                                EachCardForAnnouncements(item = it)
                            }
                        }

                    }

                }

            }
        }
    }
}

@Composable
fun EachCardForAnnouncements(
    modifier: Modifier = Modifier,
    item: AnnouncementItem,
) {


    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = item.created_at.toTimeAgo(),
            modifier = modifier.padding(horizontal = 10.dp, vertical = 0.dp)
        )
        Text(
            text = item.announcement,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = modifier.padding(15.dp)
        )

        AsyncImage(
            model = item.image_url,
            contentDescription = null,
            modifier = modifier
                .padding(15.dp)
                .clip(RoundedCornerShape(10.dp))
        )

        HorizontalDivider(
            modifier = modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            thickness = 2.dp
        )

    }

}