package com.xerox.studyrays.ui.leaderBoard.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.R
import com.xerox.studyrays.db.userDb.UserEntity
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.leaderBoard.LeaderBoardViewModel
import com.xerox.studyrays.ui.leaderBoard.user.utils.CButton
import com.xerox.studyrays.ui.leaderBoard.user.utils.CTextField
import com.xerox.studyrays.ui.leaderBoard.user.utils.DontHaveAccountRow
import com.xerox.studyrays.ui.leaderBoard.user.utils.DropDownMenu
import com.xerox.studyrays.ui.leaderBoard.user.utils.GenderSelection
import com.xerox.studyrays.ui.leaderBoard.user.utils.SuccessScreen
import com.xerox.studyrays.ui.leaderBoard.user.utils.WarningAlertDialog
import com.xerox.studyrays.ui.theme.AlegreyaFontFamily
import com.xerox.studyrays.ui.theme.AlegreyaSansFontFamily
import com.xerox.studyrays.ui.theme.DarkTeal
import com.xerox.studyrays.ui.theme.TextBlack
import com.xerox.studyrays.ui.theme.TextWhite
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.toReadableDate

@Composable
fun CreateAccountScreen(
    onLoginClick: () -> Unit,
    vm: LeaderBoardViewModel = hiltViewModel(),
    onProceedClick: () -> Unit,
) {

    var name by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var exam by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val list =
        listOf("IIT-JEE", "NEET", "CA", "UPSC","NDA","BOARD-EXAM",
            "COMMERCE","GATE","SARKARI-JOB","FOUNDATION","OLYMPIAD","OTHERS")

    var nameError by rememberSaveable { mutableStateOf(false) }
    var examError by rememberSaveable { mutableStateOf(false) }
    var genderError by rememberSaveable { mutableStateOf(false) }

    var nameErrorText by rememberSaveable { mutableStateOf<String?>(null) }
    var examErrorText by rememberSaveable { mutableStateOf<String?>(null) }
    var genderErrorText by rememberSaveable { mutableStateOf<String?>(null) }

    var isOpen by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    examErrorText = when {
        exam.isBlank() -> "Please Select Your Exam!."
        else -> null
    }

    genderErrorText = when {
        gender.isBlank() -> "Please Select Your Gender!"
        else -> null
    }

    nameErrorText = when {
        name.isBlank() -> "Please enter your name."
        name.length < 3 -> "Name is too short."
        name.length > 50 -> "Name is too long."
        else -> null
    }


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


            WarningAlertDialog(
                isOpen = isOpen,
                onDismiss = {
                    isOpen = false
                },
                onProceed = {


                    vm.postUserData(
                        name = name,
                        gender = gender,
                        exam = exam,
                    )

                    isLoading = true
                },
                isLoading = isLoading,
                loadingScreen = {
                    val state by vm.postUser.collectAsState()


                    when (val result = state) {
                        is Response.Error -> {
                            vm.showToast(
                                context = context,
                                msg = "Error occurred, error = ${result.msg}",
                                true
                            )
                            isOpen = false
                            isLoading = false
                        }

                        is Response.Loading -> {
                            LoadingScreen(paddingValues = PaddingValues())
                        }

                        is Response.Success -> {
                            val user = result.data

                            vm.insertUser(
                                UserEntity(
                                    userId = user.user_id,
                                    name = user.user_name,
                                    isBanned = user.isBanned,
                                    exam = user.exam,
                                    gender = user.gender,
                                    userNumber = 1,
                                    date = System.currentTimeMillis().toReadableDate()
                                )
                            )

                            SuccessScreen(
                                userId = result.data.user_id,
                                onUserIdCopy = {
                                    vm.showToast(
                                        context,
                                        "Unique Id copied Successfully",
                                        false
                                    )
                                },
                                onProceedClick = {
                                    onProceedClick()
                                })

                        }
                    }

                }
            )

            /// Content

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
                        .padding(top = 34.dp)
                        .height(100.dp)
                        .align(Alignment.Start)
                        .offset(x = (-20).dp)
                )

                Text(
                    text = "Create New Account",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontFamily = AlegreyaFontFamily,
                        fontWeight = FontWeight(500),
                        color = TextWhite
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )

                Text(
                    "Create new Account and start competing with your Friends.",
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
                    hint = "Full Name",
                    value = name,
                    onValueChange = { name = it },
                    isError = nameError,
                    errorText = nameErrorText.orEmpty(),
                    textColor = TextWhite
                )

                GenderSelection(
                    gender = gender,
                    onGenderSelected = { gender = it },
                    isError = genderError,
                    errorText = genderErrorText.orEmpty()
                )

                DropDownMenu(
                    expanded = expanded,
                    onExpandedChanged = { expanded = it },
                    options = list,
                    onOptionSelected = { exam = it },
                    selectedOptionText = exam,
                    isError = examError,
                    errorText = examErrorText.orEmpty()
                )

                Spacer(modifier = Modifier.height(24.dp))

                CButton(
                    text = "CREATE",
                    enabled = true,
                    onClick = {
                        genderError = gender.isBlank()

                        nameError = when {
                            name.isBlank() -> true
                            name.length < 3 -> true
                            name.length > 50 -> true
                            else -> false
                        }

                        examError = exam.isBlank()

                        if (!genderError && !examError && !nameError) {
                            isOpen = true

                        }
                    }
                )


                DontHaveAccountRow(
                    onSignupTap = {
                        onLoginClick()
                    }
                )

            }

        }

    }
}