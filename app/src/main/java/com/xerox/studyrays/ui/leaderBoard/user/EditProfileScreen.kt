package com.xerox.studyrays.ui.leaderBoard.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.xerox.studyrays.db.userDb.UserEntity
import com.xerox.studyrays.ui.leaderBoard.LeaderBoardViewModel
import com.xerox.studyrays.ui.leaderBoard.user.utils.CButton
import com.xerox.studyrays.ui.leaderBoard.user.utils.CTextField
import com.xerox.studyrays.ui.leaderBoard.user.utils.DropDownMenu
import com.xerox.studyrays.ui.leaderBoard.user.utils.GenderSelection
import com.xerox.studyrays.ui.leaderBoard.user.utils.UpdateAlertDialog
import com.xerox.studyrays.ui.theme.AlegreyaFontFamily
import com.xerox.studyrays.ui.theme.DarkTeal
import com.xerox.studyrays.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    vm: LeaderBoardViewModel = hiltViewModel()
) {

    var user by remember { mutableStateOf<UserEntity?>(null) }

    var name by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var exam by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val list =
        listOf(
            "IIT-JEE", "NEET", "CA", "UPSC", "NDA", "BOARD-EXAM",
            "COMMERCE", "GATE", "SARKARI-JOB", "FOUNDATION", "OLYMPIAD", "OTHERS"
        )


    var nameError by rememberSaveable { mutableStateOf(false) }
    var examError by rememberSaveable { mutableStateOf(false) }
    var genderError by rememberSaveable { mutableStateOf(false) }

    var isOpen by rememberSaveable { mutableStateOf(false) }

    var nameErrorText by rememberSaveable { mutableStateOf<String?>(null) }
    var examErrorText by rememberSaveable { mutableStateOf<String?>(null) }
    var genderErrorText by rememberSaveable { mutableStateOf<String?>(null) }


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



    LaunchedEffect(key1 = Unit) {
        user = vm.getUserFromDb(1)
        gender = user?.gender.orEmpty()
        exam = user?.exam.orEmpty()
        name = user?.name.orEmpty()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Edit your details", color = TextWhite)},
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "",
                            tint = TextWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkTeal)
            )
        }
    ) {

        Surface(
            color = DarkTeal,
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {

            if (isOpen) {
                UpdateAlertDialog(
                    onDismiss = { isOpen = false },
                    vm = vm,
                    exam = exam,
                    gender = gender,
                    name = name
                )

            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
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
                    text = "Update Your Account",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontFamily = AlegreyaFontFamily,
                        fontWeight = FontWeight(500),
                        color = Color.White
                    ),
                    modifier = Modifier.align(Alignment.Start)
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
                    text = "UPDATE",
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
            }
        }
    }


}