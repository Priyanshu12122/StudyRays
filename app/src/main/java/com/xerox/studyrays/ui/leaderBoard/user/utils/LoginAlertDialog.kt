package com.xerox.studyrays.ui.leaderBoard.user.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xerox.studyrays.db.userDb.UserEntity
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.leaderBoard.LeaderBoardViewModel
import com.xerox.studyrays.ui.theme.AlegreyaFontFamily
import com.xerox.studyrays.ui.theme.AlegreyaSansFontFamily
import com.xerox.studyrays.ui.theme.DarkTeal
import com.xerox.studyrays.ui.theme.TextWhite
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.SpacerHeight

@Composable
fun LoginAlertDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    vm: LeaderBoardViewModel,
    onDismiss: () -> Unit,
    onProceed: () -> Unit,
    uniqueId: String,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getAllUsers()
    }

    val state by vm.allUsers.collectAsState()

    if (isOpen) {

        var isLoading by rememberSaveable { mutableStateOf(true) }

        AlertDialog(
            containerColor = DarkTeal,
            onDismissRequest = {
                if (!isLoading) {
                    onDismiss()
                }
            },
            confirmButton = { },
            text = {
                when (val result = state) {
                    is Response.Error -> {
                        isLoading = false

                    }

                    is Response.Loading -> {
                        LoadingScreen(paddingValues = PaddingValues())
                    }

                    is Response.Success -> {
                        isLoading = false
                        val data = result.data
                        data?.let { userItems ->
                            val user = userItems.find { it.user_id == uniqueId }
                            if (user != null) {

                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        text = "Found Account", style = TextStyle(
                                            fontSize = 25.sp,
                                            fontFamily = AlegreyaFontFamily,
                                            fontWeight = FontWeight(800),
                                            color = TextWhite
                                        )
                                    )
                                    SpacerHeight(dp = 2.dp)
                                    Text(
                                        text = "Confirm if this is your Account.",
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontFamily = AlegreyaSansFontFamily,
                                            fontWeight = FontWeight(500),
                                            color = Color.White
                                        ),
                                        modifier = Modifier.padding(10.dp)
                                    )

                                    SpacerHeight(dp = 2.dp)


                                    Text(
                                        text = "Name: ${user.user_name}",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontFamily = AlegreyaSansFontFamily,
                                            fontWeight = FontWeight(500),
                                            color = Color.White
                                        ),
                                        modifier = Modifier.padding(10.dp)
                                    )

                                    Text(
                                        text = "Preparing for: ${user.exam}",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontFamily = AlegreyaSansFontFamily,
                                            fontWeight = FontWeight(500),
                                            color = Color.White
                                        ),
                                        modifier = Modifier.padding(10.dp)
                                    )

                                    Text(
                                        text = "Gender: ${user.gender}",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontFamily = AlegreyaSansFontFamily,
                                            fontWeight = FontWeight(500),
                                            color = Color.White
                                        ),
                                        modifier = Modifier.padding(10.dp)
                                    )
                                    Text(
                                        text = "Date joined: ${user.date}",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontFamily = AlegreyaSansFontFamily,
                                            fontWeight = FontWeight(500),
                                            color = Color.White
                                        ),
                                        modifier = Modifier.padding(10.dp)
                                    )
                                    SpacerHeight(dp = 12.dp)

                                    OutlinedButton(
                                        onClick = {
                                            vm.insertUser(
                                                UserEntity(
                                                    userId = user.user_id,
                                                    name = user.user_name,
                                                    isBanned = user.isBanned,
                                                    exam = user.exam,
                                                    gender = user.gender,
                                                    userNumber = 1,
                                                    date = user.date
                                                )
                                            )
                                            onProceed()
                                        },
                                        modifier = modifier
                                            .padding(10.dp)
                                            .fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = "PROCEED",
                                            style = TextStyle(
                                                fontFamily = AlegreyaFontFamily,
                                                fontWeight = FontWeight(500),
                                                color = Color.White
                                            )
                                        )

                                    }

                                }
                            } else {
                                isLoading = false
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        text = "No User found with the UniqueId that you gave, please Check your UniqueId and Retry",
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontFamily = AlegreyaSansFontFamily,
                                            fontWeight = FontWeight(500),
                                            color = Color.White
                                        ),
                                        modifier = Modifier.padding(10.dp)
                                    )

                                }
                            }
                        }


                    }
                }

            }
        )
    }

}