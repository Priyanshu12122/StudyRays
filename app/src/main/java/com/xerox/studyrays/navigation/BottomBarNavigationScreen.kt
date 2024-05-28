package com.xerox.studyrays.navigation

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
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
import androidx.compose.runtime.mutableStateOf
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.xerox.studyrays.ui.screens.ak.akIndex.AkIndex
import com.xerox.studyrays.ui.screens.khazana.khazanaScreen.KhazanaScreen
import com.xerox.studyrays.ui.screens.mainscreen.StudyScreen
import com.xerox.studyrays.ui.screens.pw.classesscreen.ClassesScreen
import com.xerox.studyrays.ui.theme.MainPurple2
import com.xerox.studyrays.utils.UseNewerVersionScreen
import com.xerox.studyrays.utils.navigateTo2

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BottomBarNavigationScreen() {
    val selectedIndex = rememberSaveable {
        mutableStateOf(0)
    }
    val navController1 = rememberNavController()
    val conf = LocalConfiguration.current
    val title = "title"
    val bname = "batchName"
    val externalId = "externalId"


    Scaffold(
        bottomBar = {
            if (conf.orientation == Configuration.ORIENTATION_PORTRAIT) {
                BottomNav(navController = navController1, selectedIndex = selectedIndex)
            }
        }
    ) {

        NavHost(
            navController = navController1,
            startDestination = NavRoutes.study,
            route = NavGraphRoutes.bottomBarNavigation,
            modifier = Modifier.padding(it)
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
                    onBatchClick = { id, name ->
                        navigateTo2(navController1, NavRoutes.subjectsScreen + "?$title=$name&$externalId=$id")
                    },
                    onFavBatchesClicked = {
                        navigateTo2(navController1, NavRoutes.favouriteCoursesScreen)
                    },
                    onKhazanaClicked = {
                        navigateTo2(navController1, NavRoutes.khazanaScreen)
                    },
                    onAkClicked = {
                        navigateTo2(navController1, NavRoutes.akIndexScreen)
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
                ClassesScreen(shouldShow = false, onBackClicked = {

                }) { classValue ->
                    navigateTo2(navController1, "${NavRoutes.eachClass}/$classValue")
                }
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