package com.xerox.studyrays.ui.screens.videoPlayerScreen.subscreens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.model.pwModel.comments.CommentItem
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.toRelativeTime

@Composable
fun CommentsScreen(
    vm: MainViewModel = hiltViewModel(),
    externalId: String,
    topicSlug: String,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getComments(
            externalId = externalId,
            topicSlug = topicSlug
        )


    }
    val commentState by vm.comments.collectAsState()

    when (val result = commentState) {
        is Response.Error -> {

            val messageState = rememberMessageBarState()
            DataNotFoundScreen(
                errorMsg = result.msg,
                state = messageState,
                shouldShowBackButton = false,
                onRetryClicked = {
                    vm.getComments(
                        externalId = externalId,
                        topicSlug = topicSlug
                    )
                }) {

            }
        }

        is Response.Loading -> {
            LoadingScreen(paddingValues = PaddingValues(0.dp))
        }

        is Response.Success -> {
            Log.d("TAG", "CommentsScreen: success = ${result.data}")

            Column(modifier = Modifier.fillMaxSize()) {
                val sortedList = remember {
                    result.data?.sortedByDescending { it.createdAt } ?: emptyList()
                }
                LazyColumn {
                    items(sortedList) {
                        EachComment(item = it)
                    }
                }
            }
        }
    }

}

@Composable
fun EachComment(
    item: CommentItem,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val image = item.created_image

        AsyncImage(
            model =
            if (image != "") image else "https://cutt.ly/StudyRaysLogo",
            contentDescription = "image",
            modifier = Modifier
                .weight(1f)
                .size(60.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(7f)
        ) {

            Text(text = item.createdAt.toRelativeTime(), fontSize = 10.sp)

            Text(
                text = item.comment_text,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            )

        }

    }

}