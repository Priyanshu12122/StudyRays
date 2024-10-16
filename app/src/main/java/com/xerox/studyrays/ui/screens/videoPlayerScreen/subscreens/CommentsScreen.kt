package com.xerox.studyrays.ui.screens.videoPlayerScreen.subscreens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.model.pwModel.comments.CommentItem
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.theme.TextBlack
import com.xerox.studyrays.ui.theme.TextWhite
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.SpacerHeight
import com.xerox.studyrays.utils.SpacerWidth
import com.xerox.studyrays.utils.toRelativeTime
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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

    Column(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
            SpacerWidth(dp = 8.dp)


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Text(
                            text = URLDecoder.decode(
                                item.created_name,
                                StandardCharsets.UTF_8.toString()
                            ), fontSize = 15.sp, fontWeight = FontWeight.Bold
                        )

                        Text(text = item.createdAt.toRelativeTime(), fontSize = 10.sp)
                    }

                    Text(
                        text = URLDecoder.decode(item.comment_text, StandardCharsets.UTF_8.toString()),
                        fontSize = 12.sp,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Icon(
                            imageVector = if (item.like_count != "0") Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "",
                            tint = if (item.like_count != "0") Color.Red else if (isSystemInDarkTheme()) TextWhite else TextBlack
                        )

                        Text(text = item.like_count, fontSize = 10.sp)

                    }


                }


        }


        SpacerHeight(dp = 12.dp)

        HorizontalDivider(thickness = 2.dp)

    }


}