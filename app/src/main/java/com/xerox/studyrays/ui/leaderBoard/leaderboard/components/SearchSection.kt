package com.xerox.studyrays.ui.leaderBoard.leaderboard.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.xerox.studyrays.db.userDb.UserEntity
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItemWithRank
import com.xerox.studyrays.ui.leaderBoard.user.utils.TextComposable2
import com.xerox.studyrays.ui.theme.DeepBlackBlue

@Composable
fun SearchSection(
    modifier: Modifier = Modifier,
    searchText: String,
    list: List<LeaderBoardItemWithRank>?,
    paddingValues: PaddingValues,
    user: UserEntity?,
    onClick: (LeaderBoardItemWithRank) -> Unit,
) {

    Surface(
        color = if (isSystemInDarkTheme()) DeepBlackBlue
        else MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (list != null) {
                val filteredList = list.filter {
                    it.user_name?.contains(
                        searchText,
                        true
                    ) == true || it.user_id.contains(searchText, true)
                }
                LazyColumn {
                    items(filteredList) { item ->
                        OtherUserItem(
                            item, item.rank + 1,
                            color =
                            if (item.user_id == user?.userId) {
                                Color(0xFF217EC7)
                            } else {
                                if (isSystemInDarkTheme()) Color(0xFF1F1F3E) else Color.LightGray
                            },
                            onClick = {
                                onClick(item)
                            }
                        )

                    }

                }

            } else {
                TextComposable2(text = "No Users Found...", fontWeight = 1000, fontSize = 26)
            }


        }
    }


}