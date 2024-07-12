package com.xerox.studyrays.ui.screens.pw.classesscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.R
import com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb.SearchEntity
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.SearchSection
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.SearchSectionOld
import com.xerox.studyrays.utils.CategoryTabRow2
import com.xerox.studyrays.utils.EachCardForClass
import com.xerox.studyrays.utils.SearchTextField2
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun ClassesScreen(
    vm: MainViewModel = hiltViewModel(),
    shouldShow: Boolean,
    onBackClicked: () -> Unit,
    onBatchClicked: (String, String, String, String) -> Unit,
    onOldBatchClick: (String, String) -> Unit,
    onEachCardOfOldBatchClicked: (String, Boolean) -> Unit,
    onEachCardClicked: (String, Boolean) -> Unit,

    ) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    val cacheState by vm.cacheSearch.collectAsState()

    val tabList = listOf("New Batches", "Old Batches")
    val pagerState = rememberPagerState { tabList.size }
    val scope = rememberCoroutineScope()


    val list = remember {
        listOf(
            EachCardForClass(R.drawable.thirteen, "13"),
            EachCardForClass(R.drawable.twelve, "12"),
            EachCardForClass(R.drawable.eleven, "11"),
            EachCardForClass(R.drawable.ten, "10"),
            EachCardForClass(R.drawable.nine, "9"),
            EachCardForClass(R.drawable.eight, "8"),
            EachCardForClass(R.drawable.seven, "7"),
            EachCardForClass(R.drawable.six, "6")
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {

            if (shouldShow) {
                TopAppBar(
                    title = { Text(text = "Select your class:-") },
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {

                        IconButton(onClick = {
                            onBackClicked()
                        }) {

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = ""
                            )

                        }
                    }
                )

            } else {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Select your class:-") },
                    scrollBehavior = scrollBehavior
                )
            }
        }) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            CategoryTabRow2(pagerState = pagerState, categories = tabList,
                onTabClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                })

            HorizontalPager(state = pagerState) { page ->
                when (page) {

                    0 -> {
                        Column {

                            var isFocused by remember {
                                mutableStateOf(false)
                            }

                            LaunchedEffect(searchText) {
                                if (searchText.length >= 3) {
                                    delay(500)
                                    vm.getQuery(query = searchText)
                                    vm.insertSearchItem(SearchEntity(searchText = searchText))
                                }
                            }

                            SearchTextField2(
                                searchText = searchText,
                                onSearchTextChanged = { searchText = it },
                                onCrossIconClicked = {
                                    searchText = ""
                                    vm.removeQueries()
                                },
                                text = "Search Pw Batches",
                                onSearch = {
                                    vm.getQuery(query = searchText)
                                    vm.insertSearchItem(SearchEntity(searchText = searchText))
                                },
                                onFocused = {
                                    isFocused = true
                                },
                                onUnFocused = {
                                    isFocused = false
                                }
                            )


                            if(isFocused){
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
                                ) {
                                    cacheState?.forEach { searchEntity ->

                                        AssistChip(onClick = { searchText = searchEntity.searchText }, label = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    searchEntity.searchText,
                                                    fontSize = 14.sp,
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                IconButton(onClick = {
                                                    vm.deleteSearchItem(searchEntity)
                                                }, modifier = Modifier.size(16.dp)) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Close",
                                                    )
                                                }
                                            }
                                        })
                                    }
                                }
                            }



                            SearchSection(onBatchClick = { id, name, classValue, slugg ->
                                onBatchClicked(id, name, classValue, slugg)
                            })


                            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                                items(list, key = { it.text }) { eachClass ->

                                    EachClassCard(image = eachClass.image, text = eachClass.text) {
                                        onEachCardClicked(eachClass.text, false)
                                    }
                                }
                            }
                        }
                    }


                    1 -> {

                        Column {

                            var isFocused by remember {
                                mutableStateOf(false)
                            }

                            LaunchedEffect(searchText) {
                                if (searchText.length >= 3) {
                                    delay(500)
                                    vm.getQuery(query = searchText)
                                    vm.insertSearchItem(SearchEntity(searchText = searchText))
                                }
                            }

                            SearchTextField2(
                                searchText = searchText,
                                onSearchTextChanged = { searchText = it },
                                onCrossIconClicked = {
                                    searchText = ""
                                    vm.removeQueries()
                                },
                                text = "Search Pw Batches",
                                onSearch = {
                                    vm.getQueryOld(query = searchText)
                                    vm.insertSearchItem(SearchEntity(searchText = searchText))
                                },
                                onFocused = {
                                    isFocused = true
                                },
                                onUnFocused = {
                                    isFocused = false
                                }
                            )

                            if(isFocused){
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
                                ) {
                                    cacheState?.forEach { searchEntity ->

                                        AssistChip(onClick = { searchText = searchEntity.searchText }, label = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    searchEntity.searchText,
                                                    fontSize = 14.sp,
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                IconButton(onClick = {
                                                    vm.deleteSearchItem(searchEntity)
                                                }, modifier = Modifier.size(16.dp)) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Close",
                                                    )
                                                }
                                            }
                                        })
                                    }
                                }
                            }


                            SearchSectionOld(onBatchClick = { id, name ->
                                onOldBatchClick(id, name)
                            })


                            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                                items(list, key = { it.text }) { eachClass ->

                                    EachClassCard(image = eachClass.image, text = eachClass.text) {
                                        onEachCardOfOldBatchClicked(eachClass.text, true)
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

