package com.xerox.studyrays.ui.screens.keyGeneratorScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.theme.TextWhite
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.SpacerHeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyGeneratorScreen(
    modifier: Modifier = Modifier,
    vm: MainViewModel = hiltViewModel(),
    onClick: (String, String) -> Unit,
) {

    val keyTask by vm.keyTask.collectAsState()
    val result = keyTask

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        vm.getKeyTask()
    }

    Scaffold(modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(text = "Generate key") })
        }
    ) { paddingValues ->

        when (result) {
            is Response.Error -> {
                DataNotFoundScreen(
                    errorMsg = result.msg,
                    state = rememberMessageBarState(),
                    shouldShowBackButton = true,
                    onRetryClicked = { vm.getKeyTask() }) {
                }
            }

            is Response.Loading -> {
                LoadingScreen(paddingValues = paddingValues)
            }

            is Response.Success -> {
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                    ) {

                        SpacerHeight(dp = 30.dp)

                        Text(
                            text = " \uD83D\uDD11 Generate access key to use our app for next 24 hours, it only takes about 1-2 minutes.",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 19.sp,
                            modifier = modifier.padding(vertical = 0.dp, horizontal = 15.dp)
                        )

                        SpacerHeight(dp = 20.dp)

                        TextButtonWithEmoji(
                            text = "GENERATE ACCESS KEY \uD83D\uDD11 ",
                            color = "#009688".toColorInt(),
                            onClick = {
                                onClick(result.data.task1_url, result.data.task1_final_url)
                            }
                        )

                        SpacerHeight(dp = 8.dp)

                        TextButtonWithEmoji(
                            text = "HOW TO USE ❓",
                            color = "#455a64".toColorInt(),
                            onClick = {
                                vm.openUrl(context, result.data.telegram)
                            }
                        )

                        SpacerHeight(dp = 25.dp)

                    }
                }
            }
        }
    }
}

@Composable
fun TextButtonWithEmoji(
    modifier: Modifier = Modifier,
    text: String,
    color: Int,
    onClick: () -> Unit,
) {

    Button(
        onClick = {
            onClick()
        },
        modifier = modifier
            .padding(horizontal = 25.dp, vertical = 0.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(color))
    ) {
        Text(text = text, color = TextWhite)

    }
}