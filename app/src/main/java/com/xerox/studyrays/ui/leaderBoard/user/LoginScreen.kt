package com.xerox.studyrays.ui.leaderBoard.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.R
import com.xerox.studyrays.ui.leaderBoard.LeaderBoardViewModel
import com.xerox.studyrays.ui.leaderBoard.user.utils.CButton
import com.xerox.studyrays.ui.leaderBoard.user.utils.CTextField
import com.xerox.studyrays.ui.leaderBoard.user.utils.LoginAlertDialog
import com.xerox.studyrays.ui.theme.AlegreyaFontFamily
import com.xerox.studyrays.ui.theme.AlegreyaSansFontFamily
import com.xerox.studyrays.ui.theme.DarkTeal
import com.xerox.studyrays.ui.theme.LightTeal
import com.xerox.studyrays.ui.theme.TextWhite

@Composable
fun LoginScreen(
    onCreateAccountClick: () -> Unit,
    vm: LeaderBoardViewModel = hiltViewModel(),
    onProceed: () -> Unit,
) {

    var uniqueId by rememberSaveable { mutableStateOf("") }

    var errorText by rememberSaveable { mutableStateOf<String?>(null) }

    var uniqueIdErrorText by rememberSaveable { mutableStateOf(false) }

    var isOpen by rememberSaveable { mutableStateOf(false) }

    errorText = when {
        uniqueId.isBlank() -> "Please enter your Unique Id"
        else -> null
    }


    // we can copy and paste and do changes for signup screen
    Surface(
        color = DarkTeal,
        modifier = Modifier.fillMaxSize()
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

            /// Content

            LoginAlertDialog(
                isOpen = isOpen,
                vm = vm,
                onDismiss = { isOpen = false },
                onProceed = {
                    onProceed()
                },
                uniqueId = uniqueId
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {

                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 54.dp)
                        .height(100.dp)
                        .align(Alignment.Start)
                        .offset(x = (-20).dp)
                )

                Text(
                    text = "Login",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontFamily = AlegreyaFontFamily,
                        fontWeight = FontWeight(500),
                        color = Color.White
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )

                Text(
                    "Login now to access your previous Study Sessions.",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = AlegreyaSansFontFamily,
                        color = Color(0xB2FFFFFF)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 24.dp)
                )


                // Text Field
                CTextField(
                    hint = "Unique Id",
                    value = uniqueId,
                    onValueChange = { uniqueId = it },
                    isError = uniqueIdErrorText,
                    errorText = errorText.orEmpty(),
                    textColor = TextWhite
                )

                Spacer(modifier = Modifier.height(24.dp))

                CButton(
                    text = "Sign In",
                    enabled = true,
                    onClick = {
                        uniqueIdErrorText = uniqueId.isBlank()
                        if (!uniqueIdErrorText) {
                            isOpen = true
                        }
                    }
                )

                Row(
                    modifier = Modifier.padding(top = 12.dp, bottom = 52.dp)
                ) {
                    Text(
                        "Dont have an account? ",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = AlegreyaSansFontFamily,
                            color = Color.White
                        )
                    )

                    Text("Create New",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = AlegreyaSansFontFamily,
                            fontWeight = FontWeight(800),
                            color = Color.White
                        ),
                        modifier = Modifier.clickable {
                            onCreateAccountClick()
                        }
                    )


                }


            }

        }

    }

}
