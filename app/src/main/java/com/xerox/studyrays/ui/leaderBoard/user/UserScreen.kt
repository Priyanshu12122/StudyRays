package com.xerox.studyrays.ui.leaderBoard.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.leaderBoard.LeaderBoardViewModel
import com.xerox.studyrays.ui.leaderBoard.user.utils.CButton
import com.xerox.studyrays.ui.leaderBoard.user.utils.CardComposable
import com.xerox.studyrays.ui.leaderBoard.user.utils.TextComposable
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.theme.AlegreyaFontFamily
import com.xerox.studyrays.ui.theme.DarkTeal
import com.xerox.studyrays.ui.theme.TextWhite
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.SpacerHeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    vm: LeaderBoardViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    onNavigateToKeyScreen: () -> Unit,
    onEditClick: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        vm.getUserFromDbFlow(1)
        vm.getTotalSessionsDurationNormal()

        mainViewModel.checkStartDestinationDuringNavigation(
            onNavigate = {
                onNavigateToKeyScreen()
            }
        )

    }

    val duration by vm.totalDuration.collectAsStateWithLifecycle()

    val state by vm.userFromDb.collectAsState()

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    var userName by rememberSaveable {
        mutableStateOf("User")
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "$userName's Progress",
                        color = TextWhite
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkTeal)
            )
        }
    ) {

        when (val result = state) {
            is Response.Error -> {
                DataNotFoundScreen(
                    errorMsg = result.msg,
                    state = rememberMessageBarState(),
                    shouldShowBackButton = false,
                    paddingValues = it,
                    onRetryClicked = {
                        vm.getUserFromDbFlow(1)
                    }) {

                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = it)
            }

            is Response.Success -> {
                val user = result.data

                if (user != null) {

                    userName = user.name

                    Surface(
                        color = DarkTeal
                    ) {

                        Box(modifier = Modifier.fillMaxSize()) {
                            /// Background Image
                            Image(
                                painter = painterResource(id = R.drawable.bg1),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(190.dp)
                                    .align(Alignment.BottomCenter)
                            )


                            Column(
                                modifier = modifier
                                    .padding(it)
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Box(
                                    modifier = Modifier
                                        .padding(top = 34.dp)
                                        .size(150.dp)
                                        .align(Alignment.CenterHorizontally)
                                ) {
                                    Image(
                                        painter = painterResource(
                                            id = if (user.gender.equals(
                                                    "Male",
                                                    true
                                                )
                                            ) R.drawable.man else R.drawable.woman
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    TextComposable(
                                        text = "12",
                                        fontWeight = 600,
                                        fontSize = 24,
                                        modifier = Modifier
                                            .size(24.dp) // Adjust size as needed
                                            .align(Alignment.BottomEnd)
                                            .offset(
                                                x = (-8).dp,
                                                y = (-8).dp
                                            ) // Adjust offset as needed
                                    )
                                }

                                SpacerHeight(dp = 8.dp)

                                TextComposable(
                                    text = user.name,
                                    fontWeight = 800,
                                    fontSize = 28,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )


                                CardComposable(text = "Preparing for: ${user.exam}")
                                CardComposable(text = "Date Joined: ${user.date}")
                                CardComposable(text = "Total Studied: $duration")

                                OutlinedButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(user.userId))
                                        vm.showToast(
                                            context,
                                            "Your Unique Id was copied successfully",
                                            true
                                        )
                                    },
                                    modifier = modifier
                                        .padding(10.dp)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "COPY UNIQUE ID",
                                        style = TextStyle(
                                            fontFamily = AlegreyaFontFamily,
                                            fontWeight = FontWeight(500),
                                            color = Color.White
                                        )
                                    )

                                }

                                SpacerHeight(dp = 16.dp)

                                CButton(text = "EDIT PROFILE", enabled = true, onClick = {
                                    onEditClick()
                                })


                            }
                        }

                    }

                }


            }
        }


    }

}