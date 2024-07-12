package com.xerox.studyrays.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.ui.screens.MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataNotFoundScreen(
    vm: MainViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(0.dp,0.dp),
    errorMsg: String? = "",
    state: MessageBarState,
    shouldShowBackButton: Boolean,
    onRetryClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            if (shouldShowBackButton) {
                TopAppBar(title = {}, navigationIcon = {
                    IconButton(onClick = {
                        onBackClicked()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                    }
                })

            }

        }
    ) {

        ContentWithMessageBar(
            messageBarState = state,
            modifier = Modifier
//                .padding(it)
                .padding(paddingValues),
            errorMaxLines = 2,
            showCopyButton = false
        ) {

            if (errorMsg == vm.nullErrorMsg){
                NoFilesFoundScreen()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    state.addError(Exception(errorMsg))

                    val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            if (errorMsg != vm.noInternetMsg) R.raw.datanotfound else R.raw.nointernet
                        )
                    )

                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .size(250.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Data not found",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            onRetryClicked()
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.Black
                        ),
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 0.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "RETRY")
                    }
                }

            }
        }
    }
}