package com.xerox.studyrays.ui.leaderBoard.leaderboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xerox.studyrays.R
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItem
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.leaderBoard.LeaderBoardViewModel
import com.xerox.studyrays.ui.leaderBoard.user.utils.TextComposable
import com.xerox.studyrays.ui.leaderBoard.user.utils.TextComposable2
import com.xerox.studyrays.ui.theme.TextBlack
import com.xerox.studyrays.ui.theme.TextWhite
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.SpacerHeight

@Composable
fun PostAlertDialog(
    modifier: Modifier = Modifier,
    vm: LeaderBoardViewModel,
    onDismiss: () -> Unit,
) {

    var isLoading by remember { mutableStateOf(true) }


    val uploadLeaderboard by vm.leaderBoardUpload.collectAsState()

    val context = LocalContext.current

    AlertDialog(onDismissRequest = {
        if (!isLoading) {
            onDismiss()
        }
    }, confirmButton = { },
        text = {


            when (val result = uploadLeaderboard) {
                is Response.Error -> {
                    isLoading = false

                    if (result.msg == vm.nullErrorMsg) {
                        TextComposable(
                            text = "Please add and study some subjects first!",
                            fontWeight = 1000,
                            fontSize = 25,
                            color = if (isSystemInDarkTheme()) TextWhite else TextBlack
                        )
                    } else {
                        onDismiss()
                        vm.showToast(context, "Error in posting, error = ${result.msg}", true)
                    }

                }

                is Response.Loading -> {
//                    isLoading = false
                    LoadingScreen(paddingValues = PaddingValues())
                }

                is Response.Success -> {
                    isLoading = false

                    val user = result.data.user

                    if (user != null) {

                        val itemm = LeaderBoardItem(
                            date = user.date ?: "Data not available",
                            exam = user.exam,
                            isBanned = user.isBanned.toString(),
                            user_id = user.userId,
                            user_name = user.name,
                            subject_studies = result.data.subjects,
                            today_study_hours = result.data.totalStudyTime.toString(),
                            gender = user.gender
                        )
                        LaunchedEffect(key1 = Unit) {
                            vm.postLeaderBoardData(itemm)
                        }
                        val postLeaderBoard by vm.postLeaderboard.collectAsState()
                        when (val postResult = postLeaderBoard) {
                            is Response.Error -> {

                            }

                            is Response.Loading -> {
                                LoadingScreen(paddingValues = PaddingValues())
                            }

                            is Response.Success -> {

//                            LaunchedEffect(key1 = Unit) {
//                                vm.getLeaderBoardData()
//                            }

                                val item = remember { postResult.data }

                                Column(
                                    modifier = modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    TextComposable(
                                        text = "Success!!",
                                        fontWeight = 1000,
                                        fontSize = 28
                                    )

                                    Image(
                                        painter = painterResource(id = R.drawable.man),
                                        contentDescription = ""
                                    )

                                    SpacerHeight(dp = 8.dp)

                                    TextComposable(
                                        text = item.user_name ?: "",
                                        fontWeight = 800,
                                        fontSize = 28
                                    )

                                    SpacerHeight(dp = 16.dp)

                                    TextComposable2(
                                        text = "Total Studied: ${item.today_study_hours} hours",
                                        fontWeight = 500,
                                        fontSize = 23
                                    )

                                    SpacerHeight(dp = 12.dp)

                                    TextComposable2(
                                        text = "Date joined: ${item.date}",
                                        fontWeight = 500,
                                        fontSize = 23
                                    )

                                    SpacerHeight(dp = 12.dp)

                                    TextComposable2(
                                        text = "Preparing for: ${item.exam}",
                                        fontWeight = 500,
                                        fontSize = 23
                                    )

                                    SpacerHeight(dp = 12.dp)

                                    TextComposable2(
                                        text = "Subjects studied: ",
                                        fontWeight = 600,
                                        fontSize = 23
                                    )

                                    LazyRow(
                                        contentPadding = PaddingValues(
                                            horizontal = 12.dp,
                                            vertical = 0.dp
                                        )
                                    ) {
                                        items(item.subject_studies) {
                                            Column {
                                                TextComposable(
                                                    text = it.subject,
                                                    fontWeight = 400,
                                                    fontSize = 20
                                                )
                                                TextComposable(
                                                    text = "${it.study_time} hours",
                                                    fontWeight = 600,
                                                    fontSize = 20
                                                )
                                            }
                                        }
                                    }

                                }

                            }


                        }
                    } else {

                        TextComposable(
                            text = "First Create an Account!!",
                            fontWeight = 1000,
                            fontSize = 25
                        )


                    }


                }
            }

        }
    )

}
