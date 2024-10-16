package com.xerox.studyrays.ui.leaderBoard.leaderboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xerox.studyrays.R
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItem
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItemWithRank
import com.xerox.studyrays.ui.leaderBoard.user.utils.TextComposable
import com.xerox.studyrays.ui.theme.AlegreyaFontFamily
import com.xerox.studyrays.ui.theme.AlegreyaSansFontFamily
import com.xerox.studyrays.ui.theme.TextWhite
import com.xerox.studyrays.utils.SpacerHeight


@Composable
fun TopThreeUsers(
    items: List<LeaderBoardItemWithRank>,
    modifier: Modifier = Modifier,
    onClick: (LeaderBoardItemWithRank) -> Unit,
) {
    val reorderedIndices =
        listOf(1, 0, 2) // This maps to the desired order: 2nd user, 1st user, 3rd user

    if (items.size > 2) {

        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = modifier.wrapContentHeight()) {
            items(reorderedIndices.size) { index ->
                val reorderedIndex = reorderedIndices[index]
                val item = items[reorderedIndex]

                if (index == 0 || index == 2) {
                    SecondAndThirdItem(
                        item = item,
                        onClick = { onClick(item) },
                        position = reorderedIndex + 1
                    )
                } else { // For the 1st user
                    LeaderboardItem(item = item, position = reorderedIndex + 1) {
                        onClick(item)
                    }
                }
            }
        }
    }

}


@Composable
fun SecondAndThirdItem(
    modifier: Modifier = Modifier,
    item: LeaderBoardItemWithRank,
    onClick: () -> Unit,
    position: Int
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .width(120.dp)
//            .padding(top = 32.dp)
            .wrapContentHeight()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
            )
            .background(
                Color(0xFF4D4D7C) ,
                RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick()
            }
    ) {

        SpacerHeight(dp = 12.dp)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .background(
                    if (position == 2) Color(
                        0xFF00BFFF
                    ) else Color(0xFFFF4500), CircleShape
                )
        ) {
            Image(
                painter = painterResource(
                    id = if (item.gender.equals("Male", true)) R.drawable.man
                    else R.drawable.woman
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.White, CircleShape)
            )
        }


        Spacer(modifier = Modifier.height(4.dp))


        Text(
            text = item.user_name ?: "",
            color = TextWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = AlegreyaSansFontFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )


        Spacer(modifier = Modifier.height(2.dp))


        Text(
            text = "Rank: $position",
            color = TextWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = AlegreyaFontFamily,
        )

        SpacerHeight(dp = 12.dp)
    }

}

@Composable
fun LeaderboardItem(
    item: LeaderBoardItemWithRank,
    position: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp)
            .height(180.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
            )
            .background(Color(0xFF3D7694) ,
                RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick()
            }
    ) {

        SpacerHeight(dp = 12.dp)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(
                    Color(0xFFFFD700), CircleShape
                )
        ) {

            Image(
                painter = painterResource(
                    id = if (item.gender.equals("Male", true))
                        R.drawable.man else R.drawable.woman
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White, CircleShape)
            )

            Image(
                painter = painterResource(id = R.drawable.crown),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.TopCenter)
            )

        }


        Spacer(modifier = Modifier.height(4.dp))


        TextComposable(
            text = item.user_name ?: "",
            fontWeight = 800,
            fontSize = 16,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(2.dp))

        TextComposable(
            text = "Rank: $position",
            fontWeight = 800,
            fontSize = 16,
            color = TextWhite
        )

        SpacerHeight(dp = 12.dp)

//        Text(
//            text = "@${item.username}",
//            fontSize = 12.sp,
//            color = Color.Gray
//        )
    }
}
