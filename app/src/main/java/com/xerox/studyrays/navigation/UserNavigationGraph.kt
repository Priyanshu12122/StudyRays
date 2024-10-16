package com.xerox.studyrays.navigation

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.xerox.studyrays.ui.leaderBoard.user.CreateAccountScreen
import com.xerox.studyrays.ui.leaderBoard.user.DecideWhichScreen
import com.xerox.studyrays.ui.leaderBoard.user.EditProfileScreen
import com.xerox.studyrays.ui.leaderBoard.user.LoginScreen
import com.xerox.studyrays.ui.theme.DarkTeal
import com.xerox.studyrays.utils.SetSystemUiColors
import com.xerox.studyrays.utils.navigateTo
import com.xerox.studyrays.utils.navigateTo2

fun NavGraphBuilder.userNavigationGraph(navController: NavHostController) {

    navigation(
        startDestination = NavRoutes.createAccountScreen,
        route = NavGraphRoutes.userScreenNavigation
    ) {


        composable(route = NavRoutes.createAccountScreen) {

            val activity = LocalContext.current as Activity

            SetSystemUiColors(
                activity = activity,
                statusBarColor = DarkTeal,
                navigationBarColor = DarkTeal
            )

            CreateAccountScreen(
                onLoginClick = {
                    navigateTo(navController, NavRoutes.loginScreen)
                },
                onProceedClick = {
                    navigateTo(navController, NavRoutes.userScreen)
                }
            )
        }

        composable(route = NavRoutes.loginScreen) {

            val activity = LocalContext.current as Activity

            SetSystemUiColors(
                activity = activity,
                statusBarColor = DarkTeal,
                navigationBarColor = DarkTeal
            )

            LoginScreen(onCreateAccountClick = {
                navigateTo2(navController, NavRoutes.createAccountScreen)
            },
                onProceed = {

                    navController.navigate(NavRoutes.userScreen) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }

                    navigateTo2(navController, NavRoutes.userScreen)
                }
            )
        }


        composable(route = NavRoutes.userScreen) {
            DecideWhichScreen(
                onCreateAccountClick = {
                    navigateTo2(navController, NavRoutes.createAccountScreen)
                },
                onLoginClick = {
                    navigateTo2(navController, NavRoutes.loginScreen)
                },
                onNavigateToKeyScreen = {
                    navigateTo(navController, NavRoutes.keyGenerationScreen)
                },
                onEditClick = {
                    navigateTo2(navController, NavRoutes.editDetailsScreen)
                }
            )
        }

        composable(route = NavRoutes.editDetailsScreen) {

            val activity = LocalContext.current as Activity

            SetSystemUiColors(
                activity = activity,
                statusBarColor = DarkTeal,
                navigationBarColor = DarkTeal
            )
            EditProfileScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }


    }

}