package com.xerox.studyrays.ui.leaderBoard.leaderboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xerox.studyrays.R
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItemWithRank
import com.xerox.studyrays.ui.leaderBoard.user.utils.TextComposable
import com.xerox.studyrays.ui.leaderBoard.user.utils.TextComposable2
import com.xerox.studyrays.ui.theme.TextBlack
import com.xerox.studyrays.ui.theme.TextWhite
import com.xerox.studyrays.utils.SpacerHeight

@Composable
fun UserInfoAlertDialog(
    modifier: Modifier = Modifier,
    item: LeaderBoardItemWithRank,
    onDismiss: () -> Unit,
) {


    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = { },
        text = {

            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(
                        id = if (item.gender.equals("Male", true))
                            R.drawable.man else R.drawable.woman
                    ), contentDescription = ""
                )

                SpacerHeight(dp = 8.dp)

                TextComposable(
                    text = item.user_name ?: "",
                    fontWeight = 800,
                    fontSize = 28,
                    color = if (isSystemInDarkTheme()) TextWhite else TextBlack
                )

                SpacerHeight(dp = 16.dp)

                TextComposable2(
                    text = "Total Studied: ${item.today_study_hours} hours",
                    fontWeight = 500,
                    fontSize = 23,
                    color = if (isSystemInDarkTheme()) TextWhite else TextBlack
                )

                SpacerHeight(dp = 12.dp)

                TextComposable2(
                    text = "Date joined: ${item.date}",
                    fontWeight = 500,
                    fontSize = 23,
                    color = if (isSystemInDarkTheme()) TextWhite else TextBlack
                )

                SpacerHeight(dp = 12.dp)

                TextComposable2(
                    text = "Preparing for: ${item.exam}",
                    fontWeight = 500,
                    fontSize = 23,
                    color = if (isSystemInDarkTheme()) TextWhite else TextBlack
                )

                SpacerHeight(dp = 12.dp)

                TextComposable2(
                    text = "Subjects studied: ",
                    fontWeight = 600,
                    fontSize = 23,
                    color = if (isSystemInDarkTheme()) TextWhite else TextBlack
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {

                    item {
                        VerticalDivider(thickness = 1.5.dp,modifier = modifier.height(65.dp))
                    }

                    items(item.subject_studies) {

                        Row {

                            Column(
                                modifier = modifier.padding(
                                    start = 12.dp,
                                    end = 12.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                )
                            ) {
                                TextComposable(
                                    text = it.subject,
                                    fontWeight = 400,
                                    fontSize = 20,
                                    color = if (isSystemInDarkTheme()) TextWhite else TextBlack
                                )
                                TextComposable(
                                    text = "${it.study_time} hours",
                                    fontWeight = 600,
                                    fontSize = 20,
                                    color = if (isSystemInDarkTheme()) TextWhite else TextBlack
                                )
                            }

                            VerticalDivider(thickness = 1.5.dp,modifier = modifier.height(65.dp))
                        }
                    }


                }

            }

        }
    )

}