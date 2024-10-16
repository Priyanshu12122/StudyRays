package com.xerox.studyrays.ui.leaderBoard.user

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.leaderBoard.LeaderBoardViewModel
import com.xerox.studyrays.ui.theme.DarkTeal
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.SetSystemUiColors

@Composable
fun DecideWhichScreen(
    modifier: Modifier = Modifier,
    vm: LeaderBoardViewModel = hiltViewModel(),
//    navController1: NavHostController,
    onNavigateToKeyScreen: () -> Unit,
    onEditClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onLoginClick: () -> Unit,
) {


    val isUserPresent by vm.isUserPresent.collectAsState()

    val activity = LocalContext.current as Activity

    SetSystemUiColors(
        activity = activity,
        statusBarColor = DarkTeal,
        navigationBarColor = DarkTeal
    )

    LaunchedEffect(key1 = Unit) {
        vm.checkIfLoggedIn()
    }




    when (val result = isUserPresent) {
        is Response.Error -> {

            WelcomeScreen(
                onCreateNewAccountClick = {
                    onCreateAccountClick()
//                        navigateTo(navController1, NavRoutes.createAccountScreen)
                },
                onLoginClick = {
                    onLoginClick()
//                        navigateTo(navController1, NavRoutes.loginScreen)

                }
            )

        }

        is Response.Loading -> {
            LoadingScreen(paddingValues = PaddingValues())
        }

        is Response.Success -> {

            val isPresent = remember { result.data }

            if (isPresent) {

                UserScreen(
                    onNavigateToKeyScreen = {
                        onNavigateToKeyScreen()
                    },
                    onEditClick = {
                        onEditClick()
//                        navigateTo2(navController1, NavRoutes.editDetailsScreen)
                    }
                )

            } else {

                WelcomeScreen(
                    onCreateNewAccountClick = {
                        onCreateAccountClick()
//                        navigateTo(navController1, NavRoutes.createAccountScreen)
                    },
                    onLoginClick = {
                        Log.d("TAG", "DecideWhichScreen: loginCick")
                        onLoginClick()
//                        navigateTo(navController1, NavRoutes.loginScreen)

                    }
                )

            }

        }
    }


}