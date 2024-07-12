package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xerox.studyrays.R
import com.xerox.studyrays.model.pwModel.SearchItem
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    scrollBehaviour: TopAppBarScrollBehavior,
    focusRequester: FocusRequester,
    searchText: String,
    isSearching: Boolean,
    onCrossIconClicked: () -> Unit,
    onDoneClicked: () -> Unit,
    onMenuIconClicked: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onWatchLaterClicked: () -> Unit,
    onSearchIconClicked: () -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    if (isSearching) {
        LaunchedEffect(key1 = Unit) {
            delay(200)
            focusRequester.requestFocus()
        }
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .focusRequester(focusRequester),
            placeholder = {
                Text(
                    "Search Pw Courses ( Enter 3 letter to start searching)",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onDoneClicked()
            }),
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                IconButton(onClick = {
                    onCrossIconClicked()
                    keyboardController?.hide()
                }
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "")
                }
            }
        )
    } else {
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
                    onSearchIconClicked()
                    keyboardController?.show()
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }

            },
            scrollBehavior = scrollBehaviour,
        )
    }
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
        modifier = Modifier.
            height(58.dp).fillMaxWidth().
        clickable {
            onItemClicked()
        }
    ) {
        Icon(imageVector = Icons.Default.Search, contentDescription = "",modifier = Modifier.weight(1f))
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
        Icon(painter = painterResource(id = R.drawable.arrowleft), contentDescription = "", modifier = Modifier.size(25.dp).weight(1f))

    }
}
