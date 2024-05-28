package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.BadgeWithCrossIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchSection(
    vm: MainViewModel = hiltViewModel(),
    onBatchClick: (String, String) -> Unit,
) {
    val searchState by vm.search.collectAsState()
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
                        EachCardForSearchItem(
                            item = searchItem,
                            isSaved = isSaved,
                            onFavouriteIconClicked = { value ->
                                vm.onFavoriteClick(FavouriteCourse(externalId = value))
                                savedStatusMap[searchItem.id] = !isSaved
                                vm.showToast(
                                    context,
                                    if (!isSaved) "${searchItem.name} added to favourites list" else "${searchItem.name} removed from favourites list"
                                )
                            },
                            checkIfSaved = {
                                scope.launch {
                                    val saved =
                                        vm.checkIfItemIsPresentInDb(FavouriteCourse(it))
                                    savedStatusMap[searchItem.id] = saved
                                }
                            }) {
                            onBatchClick(searchItem.id, searchItem.name)
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