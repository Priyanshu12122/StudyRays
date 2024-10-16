package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import com.xerox.studyrays.model.pwModel.SearchOldItem
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.ui.screens.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchSectionOld(
    vm: MainViewModel = hiltViewModel(),
    onBatchClick: (String, String) -> Unit,
) {
    val searchState by vm.searchOld.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        vm.getAllSearchItem()
    }

    when (val resultState = searchState) {

        is ResponseTwo.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Error in fetching results",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        is ResponseTwo.Loading -> {

        }

        is ResponseTwo.Nothing -> {

        }

        is ResponseTwo.Success -> {
            val list = resultState.data
            if (list.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "No results found, please check your search query",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            } else {
                val savedStatusMap = remember { mutableStateMapOf<String, Boolean>() }
                LazyColumn {
                    items(list) { searchItem ->
                        val isSaved = savedStatusMap[searchItem.id] ?: false
                        EachCardForSearchItem2(
                            item = searchItem,
                            isSaved = isSaved,
                            onFavouriteIconClicked = { value ->
                                vm.onFavoriteClick(FavouriteCourse(
                                    externalId = value,
                                    name = searchItem.name,
                                    byName = "",
                                    language = "" ,
                                    imageUrl = searchItem.previewImageUrl,
                                    slug = "",
                                    classValue = searchItem.`class`,
                                    isOld = true
                                )
                                )
                                savedStatusMap[searchItem.id] = !isSaved
                                vm.showToast(
                                    context,
                                    if (!isSaved) "${searchItem.name} added to favourites list" else "${searchItem.name} removed from favourites list"
                                )
                            },
                            checkIfSaved = {
                                scope.launch {
                                    val saved =
                                        vm.checkIfItemIsPresentInDb(it)
                                    savedStatusMap[searchItem.id] = saved
                                }
                            }) {
                            onBatchClick(
                                searchItem.id,
                                searchItem.name,
                            )
                        }
                    }
                }
            }
        }

        null -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "No results found, please check your search query",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun EachCardForSearchItem2(
    item: SearchOldItem,
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