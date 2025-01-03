package com.xerox.studyrays.ui.screens.mainscreen

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xerox.studyrays.R
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.AlertSection
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.FeatureSection
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.NavDrawerItems
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.SearchSection
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.SearchTopAppBar
import com.xerox.studyrays.utils.SpacerHeight
import com.xerox.studyrays.utils.mainScreenItemsList
import com.xerox.studyrays.utils.navBarList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    vm: MainViewModel = hiltViewModel(),
    onBatchClick: (String, String, String, String) -> Unit,
    onFavBatchesClicked: () -> Unit,
    onKhazanaClicked: () -> Unit,
    onAkClicked: () -> Unit,
    onUpdateBatchesClicked: () -> Unit,
    onWatchLaterClicked: () -> Unit,
    onProfileClick: () -> Unit,
    onAllBatchesClicked: () -> Unit,

    ) {

    val isInternetAccessible by vm.isInternetAccessible.collectAsStateWithLifecycle()
    val conf = LocalConfiguration.current

    val navItems by vm.navBar.collectAsState()
    val navBarResult = navItems

    LaunchedEffect(key1 = isInternetAccessible) {
        vm.getNavItems()
//        vm.getAllPromoItems()
        vm.getAlertItem()
//        vm.getTotalFee()
        vm.observeInternetAccessibility()
        vm.getStatus()
        vm.onTaskCompleted()
    }


    var backPressedTime by rememberSaveable { mutableLongStateOf(0L) }
    val backPressThreshold = 3000
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val navBarState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var telegramUrl by rememberSaveable { mutableStateOf("") }
    var officialChannelLink by rememberSaveable { mutableStateOf("") }

    val list2 = mainScreenItemsList()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SpacerHeight(dp = 8.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            Modifier.wrapContentSize(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.studyrays),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(70.dp)
                                    .padding(start = 10.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        if (officialChannelLink == "") {
                                            vm.showToast(context, " Sorry Some error occurred")
                                        } else {
                                            vm.openUrl(context, officialChannelLink)

                                        }
                                    }
                            )
                            Column {
                                Text(
                                    text = "Study Rays",
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp, bottom = 3.dp),
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = officialChannelLink,
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 3.dp, bottom = 6.dp)
                                        .clickable {
                                            if (officialChannelLink == "") {
                                                vm.showToast(context, " Sorry Some error occurred")
                                            } else {
                                                vm.openUrl(context, officialChannelLink)

                                            }
                                        },
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )

                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(
                            Modifier.align(Alignment.BottomCenter), thickness = 1.dp,
                            DarkGray
                        )

                    }

                    navBarList.forEachIndexed { index, drawerItem ->

                        when (navBarResult) {
                            is ResponseTwo.Error -> {

                                NavDrawerItems(
                                    drawerItem = drawerItem,
                                    index = index,
                                    onOpenGmailClicked = {
                                        vm.showFixedToast(context)
                                    },
                                    onOpenUrlClicked = { vm.showFixedToast(context) },
                                    onOpenTelegramClicked = { vm.showFixedToast(context) }) {
                                    vm.showFixedToast(context)
                                }
                            }

                            is ResponseTwo.Loading -> {
                                NavDrawerItems(
                                    drawerItem = drawerItem,
                                    index = index,
                                    onOpenGmailClicked = { vm.showFixedToast(context) },
                                    onOpenUrlClicked = { vm.showFixedToast(context) },
                                    onOpenTelegramClicked = { vm.showFixedToast(context) }) {
                                    vm.showFixedToast(context)

                                }
                            }

                            is ResponseTwo.Nothing -> {
                                NavDrawerItems(
                                    drawerItem = drawerItem,
                                    index = index,
                                    onOpenGmailClicked = { vm.showFixedToast(context) },
                                    onOpenUrlClicked = { vm.showFixedToast(context) },
                                    onOpenTelegramClicked = { vm.showFixedToast(context) }) {
                                    vm.showFixedToast(context)
                                }
                            }

                            is ResponseTwo.Success -> {
                                telegramUrl = navBarResult.data.telegram
                                officialChannelLink = navBarResult.data.website
                                NavDrawerItems(
                                    drawerItem = drawerItem,
                                    index = index,
                                    onOpenGmailClicked = {
                                        vm.openGmail(
                                            mail = navBarResult.data.email,
                                            context,
                                            "Reporting a bug",
                                            "Bug: "
                                        )
                                    },
                                    onOpenUrlClicked = {
                                        vm.openUrl(context, navBarResult.data.website)
                                    },
                                    onOpenTelegramClicked = {
                                        vm.openTelegram(
                                            context,
                                            navBarResult.data.telegram
                                        )
                                    }
                                ) {
                                    vm.shareApp(
                                        context,
                                        "Download the StudyRays app now and boost your studying journey \n  \n ${navBarResult.data.share}"
                                    )

                                }
                            }
                        }
                    }
                }
            }
        },
        drawerState = navBarState
    ) {
        Scaffold(
            topBar = {

                if (conf.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    SearchTopAppBar(
                        scrollBehaviour = scrollBehavior,
                        onMenuIconClicked = {
                            scope.launch {
                                navBarState.open()
                            }
                        },
                        onWatchLaterClicked = {
                            onWatchLaterClicked()
                        },
                        onProfileClick = {
                            onProfileClick()
                        }
                    )

                }
            }, modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->

            BackHandler {
                val currentTime = System.currentTimeMillis()
                if (currentTime - backPressedTime < backPressThreshold) {
                    (context as ComponentActivity).finish()
                } else {
                    backPressedTime = currentTime
                    vm.showToast(context, "Press back again to exit StudyRays")
                }

            }

//            Alert

            AlertSection()

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

//                Search

                SearchSection(
                    onBatchClick = { id, name, classValue, slugg ->
                        onBatchClick(id, name, classValue, slugg)
                    }
                )

                FeatureSection(
                    list = list2, onFollowOnTelegramClicked = {
                        if (telegramUrl == "") {
                            vm.showToast(context, "Sorry some error occurred")
                        } else {
                            vm.openTelegram(context, telegramUrl)
                        }
                    },
                    onKhazanaClicked = {
                        onKhazanaClicked()
                    },
                    onFavBatchesClicked = {
                        onFavBatchesClicked()
                    },
                    onAkClicked = {
                        onAkClicked()
                    },
                    onUpdateBatchesClicked = {
                        onUpdateBatchesClicked()
                    }
                ) {
                    onAllBatchesClicked()
                }
            }
        }
    }
}