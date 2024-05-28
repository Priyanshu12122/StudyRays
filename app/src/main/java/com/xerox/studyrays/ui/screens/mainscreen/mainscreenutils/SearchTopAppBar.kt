package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.xerox.studyrays.model.pwModel.SearchItem
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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
    onSearchIconClicked: () -> Unit
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
    onItemClicked: () -> Unit
) {

    LaunchedEffect(key1 = Unit){
        checkIfSaved(item.id)
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                onItemClicked()
            },
        colors = CardDefaults.cardColors(if (isSystemInDarkTheme()) Color.DarkGray else Color.White),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            AsyncImage(
                model = item.previewImageUrl, contentDescription = "",
                modifier = Modifier
                    .padding(12.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp))
                    .weight(1f),
                contentScale = ContentScale.FillBounds
            )

            Text(
                text = item.name + "  Class: " + item.`class`, fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Red,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(2f)
            )

            IconButton(
                onClick = {
                    onFavouriteIconClicked(item.id)
                }, modifier = Modifier.weight(
                    1f
                )
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "",
                    tint = if (isSaved) Color.Red else if ( isSystemInDarkTheme()) Color.White else Color.Black
                )

            }


        }

    }
}
