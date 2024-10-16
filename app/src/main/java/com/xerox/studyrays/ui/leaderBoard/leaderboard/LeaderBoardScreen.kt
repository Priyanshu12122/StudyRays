package com.xerox.studyrays.ui.leaderBoard.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.leaderBoard.LeaderBoardViewModel
import com.xerox.studyrays.ui.leaderBoard.leaderboard.components.OtherUserItem
import com.xerox.studyrays.ui.leaderBoard.leaderboard.components.PostAlertDialog
import com.xerox.studyrays.ui.leaderBoard.leaderboard.components.SearchSection
import com.xerox.studyrays.ui.leaderBoard.leaderboard.components.SortAlertDialog
import com.xerox.studyrays.ui.leaderBoard.leaderboard.components.TopThreeUsers
import com.xerox.studyrays.ui.leaderBoard.leaderboard.components.UploadUserDetailsAlertDialog
import com.xerox.studyrays.ui.leaderBoard.leaderboard.components.UserInfoAlertDialog
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.theme.AlegreyaSansFontFamily
import com.xerox.studyrays.ui.theme.DeepBlackBlue
import com.xerox.studyrays.ui.theme.TextBlack
import com.xerox.studyrays.ui.theme.TextWhite
import com.xerox.studyrays.ui.theme.WhiteModeDeepBlackBlue
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.NoFilesFoundScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderBoardScreen(
    modifier: Modifier = Modifier,
    vm: LeaderBoardViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    onNavigateToKeyScreen: () -> Unit,
) {

    val state by vm.state.collectAsStateWithLifecycle()
    val onEvent = vm::onEvent

    val getLeaderboardState by vm.getLeaderboard.collectAsState()


    LaunchedEffect(key1 = Unit) {
        vm.getLeaderboardItemsToUpload()

        vm.getLeaderBoardData()
        onEvent(LeaderBoardEvent.SetCurrentUser(vm.getUserByIdFromDb(1)))
        if (state.currentUser != null) {
            state.currentUser?.let {
                vm.getCurrentLeaderBoardUser(it.userId)
            }
        }

        mainViewModel.checkStartDestinationDuringNavigation(
            onNavigate = {
                onNavigateToKeyScreen()
            }
        )

    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (state.isSearching) {
                OutlinedTextField(
                    modifier = modifier
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    value = state.searchText, onValueChange = {
                        onEvent(LeaderBoardEvent.OnSearchTextChange(it))
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = "Search Your friends by Name or their Unique Id",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        IconButton(onClick = {
                            onEvent(LeaderBoardEvent.SetIsSearching(false))
                        }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (state.searchText == "") {
                                onEvent(LeaderBoardEvent.SetIsSearching(false))
                            } else {
                                onEvent(LeaderBoardEvent.OnSearchTextChange(""))
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {

                    })
                )

            } else {

                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Leaderboard",
                            fontFamily = AlegreyaSansFontFamily,
                            color = if (isSystemInDarkTheme()) TextWhite else TextBlack
                        )
                    },
                    colors = TopAppBarDefaults
                        .centerAlignedTopAppBarColors(
                            containerColor =
                            if (isSystemInDarkTheme()) DeepBlackBlue
                            else WhiteModeDeepBlackBlue
                        ),
                    scrollBehavior = scrollBehavior,
                    actions = {

                        IconButton(onClick = {
                            onEvent(LeaderBoardEvent.SetIsSearching(true))
                        }) {
                            Icon(imageVector = Icons.Default.PersonSearch, contentDescription = "")
                        }

                        IconButton(onClick = {
                            onEvent(LeaderBoardEvent.SetExpanded(!state.expanded))
                        }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                        }

                        DropdownMenu(
                            expanded = state.expanded,
                            onDismissRequest = {
                                onEvent(LeaderBoardEvent.SetExpanded(false))
                            }
                        ) {
                            DropdownMenuItem(onClick = {
                                onEvent(LeaderBoardEvent.SetExpanded(!state.expanded))

                                onEvent(LeaderBoardEvent.SetIsUploadUserDialogOpen(true))

                            }, text = {
                                Text("Update My Progress")
                            }
                            )


                            DropdownMenuItem(onClick = {
                                onEvent(LeaderBoardEvent.SetExpanded(!state.expanded))
                                onEvent(LeaderBoardEvent.SetIsSortByDialogOpen(true))

                            }, text = {
                                Text("Sort By Exam")
                            }
                            )

                        }

                    }
                )
            }
        }
    ) { paddingValues ->

        UploadUserDetailsAlertDialog(
            onDismiss = {
                onEvent(LeaderBoardEvent.SetIsUploadUserDialogOpen(false))
            },
            onConfirmClick = {
                onEvent(LeaderBoardEvent.SetIsUploadUserDialogOpen(false))
                onEvent(LeaderBoardEvent.SetIsOpen(true))

            },
            isOpen = state.isUploadUserDialogOpen
        )


        when (val result = getLeaderboardState) {
            is Response.Error -> {



                    DataNotFoundScreen(
                        errorMsg = result.msg,
                        paddingValues = paddingValues,
                        state = rememberMessageBarState(),
                        color = if (isSystemInDarkTheme()) DeepBlackBlue else MaterialTheme.colorScheme.background,
                        shouldShowBackButton = false,
                        onRetryClicked = {
                            vm.getLeaderBoardData()
                        }) {

                    }


            }

            is Response.Loading -> {
                LoadingScreen(
                    paddingValues = paddingValues,
                    color = if (isSystemInDarkTheme()) DeepBlackBlue else MaterialTheme.colorScheme.background
                )
            }

            is Response.Success -> {

                LaunchedEffect(key1 = Unit) {
                    onEvent(LeaderBoardEvent.UpdateFilteredList(result.data))
                }


                SortAlertDialog(
                    isOpen = state.isSortByDialogOpen,
                    onDismiss = {
                        onEvent(LeaderBoardEvent.SetIsSortByDialogOpen(false))
                    },

                    list = state.allSortList,
                    selectedIndex = state.selectedIndex,
                    onSelectedChange = {
                        onEvent(LeaderBoardEvent.SetSelectedIndex(it))
                    }
                )


                if (state.isUserInfoAlertDialogOpen) {
                    UserInfoAlertDialog(
                        item = state.selectedUser!!,
                        onDismiss = {
                            onEvent(LeaderBoardEvent.SetIsUserInfoAlertDialogOpen(false))
                        }
                    )
                }


                if (state.isSearching) {
                    SearchSection(
                        searchText = state.searchText,
                        list = state.filteredList,
                        user = state.currentUser,
                        paddingValues = paddingValues,
                        onClick = {
                            onEvent(LeaderBoardEvent.SelectedUserChange(it))
                            onEvent(LeaderBoardEvent.SetIsUserInfoAlertDialogOpen(true))
                        }
                    )
                } else {

                    if (state.isOpen) {
                        PostAlertDialog(
                            vm = vm,
                            onDismiss = {
                                onEvent(LeaderBoardEvent.SetIsOpen(false))
                                vm.getLeaderBoardData()
                            }
                        )
                    }


                    if (state.leaderBoardList.isNullOrEmpty()) {
                        NoFilesFoundScreen()
                    } else {

                        LaunchedEffect(key1 = state.selectedIndex) {
                            if (state.selectedIndex == 0) {
                                onEvent(LeaderBoardEvent.UpdateLeaderBoardList(state.leaderBoardList))
                            } else {
                                onEvent(
                                    LeaderBoardEvent.OnFilterExamChanged(
                                        list = state.leaderBoardList!!,
                                        exam = state.allSortList[state.selectedIndex]
                                    )
                                )

                            }
                        }

                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                                .background(if (isSystemInDarkTheme()) DeepBlackBlue else WhiteModeDeepBlackBlue)
                                .padding(horizontal = 16.dp, vertical = 0.dp)
                        ) {

                            Box(modifier = Modifier.fillMaxSize()) {
                                LazyColumn(
                                    contentPadding = PaddingValues(bottom = 100.dp) // Bottom padding for sticky header
                                ) {
                                    item {
                                        TopThreeUsers(
                                            items = if (state.selectedIndex != 0) state.examFilteredList!!.take(3)
                                            else state.leaderBoardList!!.take(3),
                                            modifier = Modifier.height(250.dp)
                                        ) { user ->
                                            onEvent(LeaderBoardEvent.SelectedUserChange(user))
                                            onEvent(
                                                LeaderBoardEvent.SetIsUserInfoAlertDialogOpen(
                                                    true
                                                )
                                            )
                                        }
                                    }
                                    items(
                                        if (state.selectedIndex != 0) state.examFilteredList!!
                                        else state.leaderBoardList!!
                                    ) {  userr ->
                                        OtherUserItem(
                                            userr, userr.rank+1,
                                            color =
                                            if (userr.user_id == state.currentUser?.userId) {
                                                Color(0xFF217EC7)
                                            } else {
                                                if (isSystemInDarkTheme()) Color(0xFF1F1F3E) else Color.LightGray
                                            }
                                        ) {
                                            onEvent(LeaderBoardEvent.SelectedUserChange(it))
                                            onEvent(
                                                LeaderBoardEvent.SetIsUserInfoAlertDialogOpen(
                                                    true
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }

                            }
                        }

                    }
                }


            }
        }
    }

}
