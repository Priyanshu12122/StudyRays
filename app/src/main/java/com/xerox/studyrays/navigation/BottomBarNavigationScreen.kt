package com.xerox.studyrays.navigation

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import com.xerox.studyrays.ui.leaderBoard.leaderboard.LeaderBoardScreen
import com.xerox.studyrays.ui.leaderBoard.user.DecideWhichScreen
import com.xerox.studyrays.ui.screens.keyGeneratorScreen.KeyGeneratorScreen
import com.xerox.studyrays.ui.screens.mainscreen.StudyScreen
import com.xerox.studyrays.ui.studyfocus.dashboardScreen.DashboardScreenRoute
import com.xerox.studyrays.ui.studyfocus.sessionScreen.SessionScreenRoute
import com.xerox.studyrays.ui.studyfocus.sessionScreen.StudySessionTimerService
import com.xerox.studyrays.ui.studyfocus.subjectScreen.SubjectScreenRoute
import com.xerox.studyrays.ui.studyfocus.taskScreen.TaskScreenRoute
import com.xerox.studyrays.ui.theme.DarkTeal
import com.xerox.studyrays.ui.theme.DeepBlackBlue
import com.xerox.studyrays.ui.theme.MainPurple
import com.xerox.studyrays.ui.theme.MainPurple2
import com.xerox.studyrays.ui.theme.NavyBlue
import com.xerox.studyrays.ui.theme.NeonYellow
import com.xerox.studyrays.ui.theme.TextWhite
import com.xerox.studyrays.ui.theme.WhiteModeDeepBlackBlue
import com.xerox.studyrays.utils.SetSystemUiColors
import com.xerox.studyrays.utils.SpacerHeight
import com.xerox.studyrays.utils.navigateTo
import com.xerox.studyrays.utils.navigateTo2
import com.xerox.studyrays.utils.navigateTo4

@Composable
fun BottomBarNavigationScreen(
    startDestination: String,
    navController1: NavHostController,
    timerService: StudySessionTimerService,
) {

    val selectedIndex = rememberSaveable { mutableIntStateOf(0) }

    var color by remember { mutableStateOf(Color.Transparent) }
    val isDarkTheme = isSystemInDarkTheme()

    val conf = LocalConfiguration.current
    val title = "title"
    val arg_key = "url"
    val bname = "batchName"
    val externalId = "externalId"
    val slugg = "slug"
    val classValues = "classValue"
    val nullValues = -1

    val taskSubjectId = "taskSubjectId"
    val taskId = "taskId"




    Scaffold(
        bottomBar = {
            if (conf.orientation == Configuration.ORIENTATION_PORTRAIT) {
//                BottomNav(
//                    navController = navController1,
//                    selectedIndex = selectedIndex,
//                    color = color
//                )

                PerfectBottomBar(navController1)
            }
        }
    ) { paddingValues ->

//        if (selectedIndex.intValue == 3) {
//            UserNavigationScreen(navController = navController1, paddingValues = paddingValues)
//
//        } else {


        NavHost(
            navController = navController1,
            startDestination = startDestination,
            route = NavGraphRoutes.bottomBarNavigation,
            modifier = Modifier.padding(paddingValues),
//
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

                val activity = LocalContext.current as Activity

                SetSystemUiColors(
                    activity = activity,
                    statusBarColor = MaterialTheme.colorScheme.background,
                    navigationBarColor = MaterialTheme.colorScheme.background
                )

                LaunchedEffect(key1 = Unit) {
                    color = Color.Transparent
                }

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
                    },
                    onProfileClick = {
                        navigateTo2(navController1, NavRoutes.userScreen)
                    }
                ) {
                    navigateTo2(navController1, NavRoutes.classes)
                    return@StudyScreen
                }
            }

            composable(route = NavRoutes.batches) {

                LaunchedEffect(key1 = Unit) {
                    color = Color.Transparent
                }

                val activity = LocalContext.current as Activity

                SetSystemUiColors(
                    activity = activity,
                    statusBarColor = MaterialTheme.colorScheme.background,
                    navigationBarColor = MaterialTheme.colorScheme.background
                )

                DashboardScreenRoute(
                    onSubjectCardClick = {
                        navigateTo2(
                            navController1,
                            NavRoutes.studyFocusSubjectDetailsScreen + "/$it"
                        )
                    },
                    onTaskCardClick = {

                    },
                    onNavigateToKeyScreen = {
                        navigateTo(navController1, NavRoutes.keyGenerationScreen)
                    },
                    onSessionCardClick = {
                        navigateTo2(navController1, NavRoutes.sessionScreen)
                    })
            }

            composable(route = NavRoutes.test) {

                val activity = LocalContext.current as Activity

                SetSystemUiColors(
                    activity = activity,
                    statusBarColor = if (isDarkTheme) DeepBlackBlue else WhiteModeDeepBlackBlue,
                    navigationBarColor = if (isDarkTheme) DeepBlackBlue else WhiteModeDeepBlackBlue
                )

                LaunchedEffect(key1 = Unit) {
                    color = if (isDarkTheme) DeepBlackBlue else TextWhite
                }

                LeaderBoardScreen(
                    onNavigateToKeyScreen = {
                        navigateTo(navController1, NavRoutes.keyGenerationScreen)
                    },
                )

            }


            composable(route = NavRoutes.pwStore) {

                val activity = LocalContext.current as Activity

                SetSystemUiColors(
                    activity = activity,
                    statusBarColor = DarkTeal,
                    navigationBarColor = DarkTeal
                )

                LaunchedEffect(key1 = Unit) {
                    color = DarkTeal
                }

                DecideWhichScreen(
                    onCreateAccountClick = {
                        navigateTo(navController1, NavRoutes.createAccountScreen)
                    },
                    onLoginClick = {
                        navigateTo(navController1, NavRoutes.loginScreen)
                    },
                    onNavigateToKeyScreen = {
                        navigateTo(navController1, NavRoutes.keyGenerationScreen)
                    },
                    onEditClick = {
                        navigateTo(navController1, NavRoutes.editDetailsScreen)
                    }
                )
            }


            composable(route = NavRoutes.keyGenerationScreen) {

                val activity = LocalContext.current as Activity

                SetSystemUiColors(
                    activity = activity,
                    statusBarColor = MaterialTheme.colorScheme.background,
                    navigationBarColor = MaterialTheme.colorScheme.background
                )

                LaunchedEffect(key1 = Unit) {
                    color = Color.Transparent
                }

                KeyGeneratorScreen(
                    onClick = { taskUrl, shortenedUrl ->
                        navigateTo2(
                            navController1,
                            NavRoutes.webViewScreen + "?$arg_key=$taskUrl&$title=$shortenedUrl"
                        )
                    },
                    onNavigateToStudyScreen = {
                        navigateTo4(navController1, NavRoutes.study)
                    }
                )
            }


            composable(route = NavRoutes.studyFocusSubjectDetailsScreen + "/{subjectId}",
                arguments = listOf(
                    navArgument(name = "subjectId") {
                        type = NavType.IntType
                    }
                )
            ) {

                val activity = LocalContext.current as Activity

                SetSystemUiColors(
                    activity = activity,
                    statusBarColor = MaterialTheme.colorScheme.background,
                    navigationBarColor = MaterialTheme.colorScheme.background
                )

                LaunchedEffect(key1 = Unit) {
                    color = Color.Transparent
                }

                SubjectScreenRoute(
                    onAddNewTaskClick = {
                        navigateTo2(
                            navController1,
                            NavRoutes.taskStudyFocusScreen + "?$taskSubjectId=$it&$taskId=$nullValues"
                        )
                    },
                    onTaskCardClick = {
                        navigateTo2(
                            navController1,
                            NavRoutes.taskStudyFocusScreen + "?$taskSubjectId=$nullValues&$taskId=$it"
                        )
                    },
                    onBackButtonClick = {
                        navController1.navigateUp()
                    })
            }

            composable(route = NavRoutes.taskStudyFocusScreen + "?$taskSubjectId={$taskSubjectId}&$taskId={$taskId}",
                arguments = listOf(
                    navArgument(name = taskSubjectId) {
                        type = NavType.IntType
                        defaultValue = nullValues
                    },
                    navArgument(name = taskId) {
                        type = NavType.IntType
                        defaultValue = nullValues
                    }
                )
            ) {

                val activity = LocalContext.current as Activity

                SetSystemUiColors(
                    activity = activity,
                    statusBarColor = MaterialTheme.colorScheme.background,
                    navigationBarColor = MaterialTheme.colorScheme.background
                )

                LaunchedEffect(key1 = Unit) {
                    color = Color.Transparent
                }

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

                val activity = LocalContext.current as Activity

                SetSystemUiColors(
                    activity = activity,
                    statusBarColor = MaterialTheme.colorScheme.background,
                    navigationBarColor = MaterialTheme.colorScheme.background
                )

                LaunchedEffect(key1 = Unit) {
                    color = Color.Transparent
                }

                SessionScreenRoute(timerService = timerService,
                    onBackButtonClick = {
                        navController1.navigateUp()
                    })
            }



            subNavGraph(navController1)

            userNavigationGraph(navController1)
        }

//        }


    }
}

@Composable
fun BottomNav(navController: NavHostController, selectedIndex: MutableState<Int>, color: Color) {


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
        NavigationBar(containerColor = color) {

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
                    alwaysShowLabel = true /*selectedIndex.value == index*/,
                    label = {
                        Text(text = s.title)
                    }
                )
            }
        }
    }
}

@Composable
fun PerfectBottomBar(navController: NavHostController) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val items = listOf(
        Routes.Study,
        Routes.Batches,
        Routes.Test,
        Routes.PwStore,
    )

    var fullWidth by remember { mutableFloatStateOf(0f) }
    val itemWidth = remember(fullWidth) { fullWidth / items.size }

    val density = LocalDensity.current
    val indicatorOffset by animateDpAsState(
        targetValue = with(density) { ((selectedIndex * itemWidth) + itemWidth / 2 - 28.dp.toPx()).toDp() },
        animationSpec = androidx.compose.animation.core.spring(), label = ""
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination

    val bottomBarDestinations = items.any {
        it.route == currentRoute?.route
    }

    if (bottomBarDestinations) {

        val navigationBarHeight = WindowInsets.navigationBars.getBottom(LocalDensity.current)

        Column {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(50.dp)),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { coordinates ->
                            fullWidth = coordinates.size.width.toFloat()
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Indicator
                    Box(
                        modifier = Modifier
                            .offset(x = indicatorOffset)
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MainPurple)
                    )

                    // Bottom bar items
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items.forEachIndexed { index, item ->
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(
                                        indication = null,
                                        interactionSource = MutableInteractionSource()
                                    ) {
                                        selectedIndex = index
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.title,
                                    tint = Color.White,
                                    modifier = Modifier.size(21.dp)
                                )
                            }
                        }
                    }
                }
            }

            SpacerHeight(with(LocalDensity.current) { navigationBarHeight.toDp() })

        }

    }


}