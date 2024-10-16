package com.xerox.studyrays.ui.leaderBoard.leaderboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xerox.studyrays.R
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItem
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItemWithRank
import com.xerox.studyrays.ui.theme.AlegreyaFontFamily
import com.xerox.studyrays.ui.theme.AlegreyaSansFontFamily
import com.xerox.studyrays.ui.theme.TextBlack
import com.xerox.studyrays.ui.theme.TextWhite

@Composable
fun OtherUserItem(
    user: LeaderBoardItemWithRank,
    rank: Int,
    color: Color,
    onClick: (LeaderBoardItemWithRank) -> Unit,

    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
            )
            .background(
                color,
//                if (isSystemInDarkTheme()) Color(0xFF1F1F3E) else Color.LightGray,
                RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick(user)
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(60.dp)
                .weight(2f)
        ) {

            Text(text = "#$rank", modifier = Modifier.align(Alignment.BottomEnd))

            Image(
                painter = painterResource(
                    id = if (user.gender.equals(
                            "Male",
                            true
                        )
                    ) R.drawable.man else R.drawable.woman
                ),
                contentDescription = user.user_name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)

            )
        }

        Spacer(modifier = Modifier.width(16.dp))
//        Column {
        Text(
            text = user.user_name ?: "",
            color = if (isSystemInDarkTheme()) TextWhite else TextBlack,
            fontWeight = FontWeight.Bold,
            fontFamily = AlegreyaSansFontFamily,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(4f)
        )
//            Text(text = user.username, color = Color.Gray)
//        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(2.5f)
        ) {
//            Text(text = user.points.toString(), color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                text = "${user.today_study_hours} hours",
                color = if (isSystemInDarkTheme()) TextWhite else TextBlack,
                fontFamily = AlegreyaFontFamily,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,

                )
        }
    }
}
