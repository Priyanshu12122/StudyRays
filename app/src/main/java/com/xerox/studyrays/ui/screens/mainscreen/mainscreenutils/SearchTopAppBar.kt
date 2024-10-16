package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.R
import com.xerox.studyrays.db.userDb.UserEntity
import com.xerox.studyrays.model.pwModel.SearchItem
import com.xerox.studyrays.ui.leaderBoard.LeaderBoardViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    scrollBehaviour: TopAppBarScrollBehavior,
    onMenuIconClicked: () -> Unit,
    onWatchLaterClicked: () -> Unit,
    onProfileClick: () -> Unit,
    vm: LeaderBoardViewModel = hiltViewModel()
) {

    var user by remember { mutableStateOf<UserEntity?>(null) }

    LaunchedEffect(key1 = Unit) {
        user = vm.getUserByIdFromDb(1)
    }

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                onMenuIconClicked()
            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "")
            }
        },
        title = {
            Text(text = "Study Rays")

        },
        actions = {
            IconButton(onClick = {
                onWatchLaterClicked()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_access_time_24),
                    contentDescription = "Watch later"
                )
            }

            IconButton(onClick = {
                onProfileClick()
            }) {
                if (user == null){
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "",
                        modifier = Modifier.size(23.dp)
                    )
                } else {
                    Image(
                        painter = if (user?.gender?.equals(
                                "Male",
                                true
                            ) == true
                        ) painterResource(id = R.drawable.man)
                        else painterResource(id = R.drawable.woman),
                        contentDescription = "Profile",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

        },
        scrollBehavior = scrollBehaviour,
    )
//    }
}

@Composable
fun EachCardForSearchItem(
    item: SearchItem,
    isSaved: Boolean,
    onFavouriteIconClicked: (String) -> Unit,
    checkIfSaved: (String) -> Unit,
    onItemClicked: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        checkIfSaved(item.batch_id)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(58.dp)
            .fillMaxWidth()
            .clickable {
                onItemClicked()
            }
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "",
            modifier = Modifier.weight(1f)
        )
        Text(text = item.batch_name, modifier = Modifier.weight(7f))
        IconButton(
            onClick = {
                onFavouriteIconClicked(item.batch_id)
            }, modifier = Modifier.weight(
                1f
            )
        ) {
            Icon(
                imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "",
                tint = if (isSaved) Color.Red else if (isSystemInDarkTheme()) Color.White else Color.Black
            )

        }
        Icon(
            painter = painterResource(id = R.drawable.arrowleft),
            contentDescription = "",
            modifier = Modifier
                .size(25.dp)
                .weight(1f)
        )

    }
}
