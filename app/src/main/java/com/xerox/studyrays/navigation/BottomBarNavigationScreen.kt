package com.xerox.studyrays.navigation

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.xerox.studyrays.ui.screens.ak.akIndex.AkIndex
import com.xerox.studyrays.ui.screens.keyGeneratorScreen.KeyGeneratorScreen
import com.xerox.studyrays.ui.screens.khazana.khazanaScreen.KhazanaScreen
import com.xerox.studyrays.ui.screens.mainscreen.StudyScreen
import com.xerox.studyrays.ui.studyfocus.dashboardScreen.DashboardScreenRoute
import com.xerox.studyrays.ui.studyfocus.sessionScreen.SessionScreenRoute
import com.xerox.studyrays.ui.studyfocus.sessionScreen.StudySessionTimerService
import com.xerox.studyrays.ui.studyfocus.subjectScreen.SubjectScreenRoute
import com.xerox.studyrays.ui.studyfocus.taskScreen.TaskScreenRoute
import com.xerox.studyrays.ui.theme.MainPurple2
import com.xerox.studyrays.utils.UseNewerVersionScreen
import com.xerox.studyrays.utils.navigateTo2

@Composable
fun BottomBarNavigationScreen(
    startDestination: String,
    navController1: NavHostController,
    timerService: StudySessionTimerService,
) {

    val selectedIndex = rememberSaveable { mutableIntStateOf(0) }
    val conf = LocalConfiguration.current
    val title = "title"
    val arg_key = "url"
    val bname = "batchName"
    val externalId = "externalId"
    val slugg = "slug"
    val classValues = "classValue"
    val nullValues = 1

    Scaffold(
        bottomBar = {
            if (conf.orientation == Configuration.ORIENTATION_PORTRAIT) {
                BottomNav(navController = navController1, selectedIndex = selectedIndex)
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController1,
            startDestination = startDestination /*NavRoutes.study*/,
            route = NavGraphRoutes.bottomBarNavigation,
            modifier = Modifier.padding(paddingValues),
            enterTransition = { slideInHorizontally(animationSpec = tween(300)) { it } },
            exitTransition = { slideOutHorizontally(animationSpec = tween(300)) { -it } },
            popEnterTransition = { slideInHorizontally(animationSpec = tween(300)) { -it } },
            popExitTransition = { slideOutHorizontally(animationSpec = tween(300)) { it } }
        ) {
            composable(route = NavRoutes.study,
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "https://studyrays.site"
                        action = Intent.ACTION_VIEW
                    }
                )
            ) {
                StudyScreen(
                    onBatchClick = { id, name, classValue, sluggg ->
                        navigateTo2(
                            navController1,
                            NavRoutes.subjectsScreen + "?$title=$name&$externalId=$id&$slugg=$sluggg&$classValues=$classValue"
                        )
                    },
                    onFavBatchesClicked = {
                        navigateTo2(navController1, NavRoutes.favouriteCoursesScreen)
                    },
                    onKhazanaClicked = {
                        navigateTo2(navController1, NavRoutes.khazanaScreen)
                    },
                    onAkClicked = {
                        navigateTo2(navController1, NavRoutes.akIndexScreen)
                    },
                    onUpdateBatchesClicked = {
                        navigateTo2(navController1, NavRoutes.updateBatchScreen)
                    },
                    onWatchLaterClicked = {
                        navigateTo2(navController1, NavRoutes.watchLaterScreen)
                    }
                ) {
                    navigateTo2(navController1, NavRoutes.classes)
                    return@StudyScreen
                }
            }

            composable(route = NavRoutes.test) {

                KhazanaScreen(shouldShow = false, onClick = {
                    navigateTo2(
                        navController1,
                        NavRoutes.khazanaSubjectsScreen + "/$it"
                    )
                }) {

                }
            }

            composable(route = NavRoutes.batches) {

                DashboardScreenRoute(
                    onSubjectCardClick = {
                        navigateTo2(
                            navController1,
                            NavRoutes.studyFocusSubjectDetailsScreen + "/$it"
                        )
                    },
                    onTaskCardClick = {

                    },
                    onSessionCardClick = {
                        navigateTo2(navController1, NavRoutes.sessionScreen)
                    })

//                ClassesScreen(shouldShow = false, onBackClicked = {
//
//                },
//                    onBatchClicked = { id, name ->
//                        navigateTo2(
//                            navController1,
//                            NavRoutes.subjectsScreen + "?$title=$name&$externalId=$id"
//                        )
//                    }) { classValue ->
//                    navigateTo2(navController1, "${NavRoutes.eachClass}/$classValue")
//                }
            }

            composable(route = NavRoutes.pwStore) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AkIndex(onClick = {
                        navigateTo2(navController1, NavRoutes.akSubjectsScreen + "/$it")
                    }, shouldShow = false) {

                    }
                } else {
                    UseNewerVersionScreen()
                }
            }

            composable(route = NavRoutes.keyGenerationScreen) {
                KeyGeneratorScreen(
                    onClick = { taskUrl, shortenedUrl ->
                        navigateTo2(
                            navController1,
                            NavRoutes.webViewScreen + "?$arg_key=$taskUrl&$title=$shortenedUrl"
                        )
                    })
            }


            composable(route = NavRoutes.studyFocusSubjectDetailsScreen + "/{subjectId}",
                arguments = listOf(
                    navArgument(name = "subjectId") {
                        type = NavType.IntType
                    }
                )
            ) {
                SubjectScreenRoute(
                    onAddNewTaskClick = {
                        navigateTo2(
                            navController1,
                            NavRoutes.taskStudyFocusScreen + "/$it/$nullValues"
                        )
                    },
                    onTaskCardClick = {
                        navigateTo2(
                            navController1,
                            NavRoutes.taskStudyFocusScreen + "/$nullValues/$it"
                        )
                    },
                    onBackButtonClick = {
                        navController1.navigateUp()
                    })
            }

            composable(route = NavRoutes.taskStudyFocusScreen + "/{taskSubjectId}/{taskId}",
                arguments = listOf(
                    navArgument(name = "taskSubjectId") {
                        type = NavType.IntType
                    },
                    navArgument(name = "taskId") {
                        type = NavType.IntType
                    }
                )
            ) {
                TaskScreenRoute(
                    onBackButtonClick = {
                        navController1.navigateUp()
                    }
                )
            }

            composable(route = NavRoutes.sessionScreen,
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "study_rays://dashboard/session"
                        action = Intent.ACTION_VIEW
                    }
                )
            ) {
                SessionScreenRoute(timerService = timerService,
                    onBackButtonClick = {
                        navController1.navigateUp()
                    })
            }



            subNavGraph(navController1)
        }

    }
}

@Composable
fun BottomNav(navController: NavHostController, selectedIndex: MutableState<Int>) {


    val list = listOf(
        Routes.Study,
        Routes.Batches,
        Routes.Test,
        Routes.PwStore,
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination

    val bottomBarDestinations = list.any {
        it.route == currentRoute?.route
    }
    if (bottomBarDestinations) {
        NavigationBar(containerColor = Color.Transparent) {

            list.forEachIndexed { index, s ->
                NavigationBarItem(
                    selected = currentRoute?.hierarchy?.any { it.route == s.route } == true,
                    onClick = {
//                        if (s.route == currentRoute?.route && selectedIndex.value == index) {
//                            return@NavigationBarItem
//                        } else {

                        navController.navigate(s.route) {
                            popUpTo(NavRoutes.study) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        selectedIndex.value = index
//                        }
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = s.icon),
                            contentDescription = "",
                            modifier = Modifier.size(26.dp),
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.tint(
                                color =
                                if (index == selectedIndex.value) MainPurple2 else if (isSystemInDarkTheme()) Color.White else Color.Black
                            )
                        )
                    },
                    alwaysShowLabel = selectedIndex.value == index,
                    label = {
                        Text(text = s.title)
                    }
                )
            }
        }
    }
}