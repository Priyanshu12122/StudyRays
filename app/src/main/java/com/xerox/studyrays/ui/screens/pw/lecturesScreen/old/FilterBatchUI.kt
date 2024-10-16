package com.xerox.studyrays.ui.screens.pw.lecturesScreen.old

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.pw.coursesscreen.EachCourseCardInClass
import com.xerox.studyrays.ui.screens.pw.coursesscreen.LoadingCourseCardInClass
import com.xerox.studyrays.utils.SearchTextField
import kotlinx.coroutines.launch

@Composable
fun FilterBatchUI(
    vm: MainViewModel = hiltViewModel(),
    onCLick: (String, String, String, String) -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        vm.fetchClasses()
        vm.fetchAllBatches()
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedClass by remember { mutableStateOf("CLASS") }
    var selectedYear by remember { mutableStateOf("YEAR") }
    var selectedExam by remember { mutableStateOf("EXAM") }
    var searchQuery by remember { mutableStateOf("") }

    val classes = remember { mutableStateListOf<String>() }
    val years = remember { mutableStateListOf<String>() }
    val exams = remember { mutableStateListOf<String>() }


    val fetchClassState by vm.fetchClass.collectAsStateWithLifecycle()
    val fetchClassResult = fetchClassState

    val fetchYearState by vm.fetchYears.collectAsStateWithLifecycle()
    val fetchYearResult = fetchYearState

    val fetchExamState by vm.fetchExams.collectAsStateWithLifecycle()
    val fetchExamResult = fetchExamState

    val fetchAllBatchesState by vm.fetchAllBatches.collectAsStateWithLifecycle()
    val fetchAllBatchesResult = fetchAllBatchesState

    val fetchBatches by vm.fetchBatches.collectAsStateWithLifecycle()
    val fetchBatchesResult = fetchBatches

    when (fetchClassResult) {
        is Response.Error -> {

        }

        is Response.Loading -> {

        }

        is Response.Success -> {

            val result = remember { fetchClassResult.data }

            LaunchedEffect(Unit) {
                classes.clear()
                result?.forEach {
                    classes.add(it.`class`)
                }
            }

            LaunchedEffect(selectedClass) {
                if (selectedClass != "CLASS") {
                    vm.fetchYears(selectedClass)
                }
            }

            when (fetchYearResult) {

                is Response.Error -> {
                }

                is Response.Loading -> {
                }

                is Response.Success -> {

                    LaunchedEffect(Unit) {
                        years.clear()
                        fetchYearResult.data?.forEach {
                            years.add(it.year)
                        }
                    }

                    LaunchedEffect(selectedYear) {
                        if (selectedYear != "YEAR") {
                            vm.fetchExams(selectedClass, selectedYear)
                        }
                    }

                    when (fetchExamResult) {
                        is Response.Error -> {

                        }

                        is Response.Loading -> {
                        }

                        is Response.Success -> {

                            LaunchedEffect(Unit) {
                                exams.clear()
                                fetchExamResult.data?.forEach {
                                    exams.add(it?.exam ?: "Null")
                                }

                            }

                            LaunchedEffect(selectedExam) {
                                if (selectedExam != "EXAM") {
                                    vm.fetchBatches(selectedClass, selectedYear, selectedExam)
                                }
                            }


                        }
                    }

                }
            }


        }
    }




    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Dropdown Menus for Month, Year, and Exam
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            DropdownMenuComponent(
                label = "Classes",
                items = classes,
                selectedValue = selectedClass,
                modifier = Modifier.weight(1f)
            ) {
                selectedClass = it
            }

            DropdownMenuComponent(
                label = "Year",
                items = years,
                selectedValue = selectedYear,
                modifier = Modifier.weight(1f)
            ) {
                selectedYear = it
            }

            DropdownMenuComponent(
                label = "Exam",
                items = exams,
                selectedValue = selectedExam,
                modifier = Modifier.weight(1f)
            ) {
                selectedExam = it
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar

        SearchTextField(
            searchText = searchQuery,
            onSearchTextChanged = { searchQuery = it },
            text = "Search Lectures",
            onCrossIconClicked = {
                searchQuery = ""
            },
        )


        Spacer(modifier = Modifier.height(16.dp))

        if (selectedExam == "EXAM") {

            when (fetchAllBatchesResult) {
                is Response.Error -> {

                }

                is Response.Loading -> {
                    LazyColumn {
                        items(10){
                            LoadingCourseCardInClass()
                        }
                    }

                }

                is Response.Success -> {

                    val result = remember {
                        fetchAllBatchesResult.data
                    }

                    if (result != null) {

                        val savedStatusMap =
                            remember { mutableStateMapOf<String, Boolean>() }

                        LazyColumn {
                            items(result) { batchItem ->

                                val isSaved = savedStatusMap[batchItem.batch_id] ?: false
                                EachCourseCardInClass(
                                    item = batchItem,
                                    isSaved = isSaved,
                                    onFavouriteIconClicked = { value ->
                                        vm.onFavoriteClick(
                                            FavouriteCourse(
                                                externalId = value,
                                                name = batchItem.batch_name,
                                                byName = batchItem.byName,
                                                language = batchItem.language,
                                                imageUrl = batchItem.previewImage,
                                                slug = batchItem.slug,
                                                classValue = batchItem.`class`,
                                                isOld = false
                                            )
                                        )
                                        savedStatusMap[batchItem.batch_id] = !isSaved
                                        vm.showToast(
                                            context,
                                            if (!isSaved) "${batchItem.batch_name} added to favourites list"
                                            else "${batchItem.batch_name} removed from favourites list"
                                        )
                                    },
                                    checkIfSaved = {
                                        scope.launch {
                                            val saved =
                                                vm.checkIfItemIsPresentInDb(it)
                                            savedStatusMap[batchItem.batch_id] = saved
                                        }
                                    }
                                ) {

                                    onCLick(
                                        batchItem.batch_id,
                                        batchItem.slug,
                                        batchItem.`class`,
                                        batchItem.batch_name
                                    )

                                }

                            }
                        }
                    } else {
                        Text(text = "No data found")
                    }

                }
            }
        } else {

            when (fetchBatchesResult) {
                is Response.Error -> {

                }

                is Response.Loading -> {
                    LazyColumn {
                        items(10){
                            LoadingCourseCardInClass()
                        }
                    }
                }

                is Response.Success -> {

                    val result = remember {
                        fetchBatchesResult.data
                    }

                    if (result != null) {

                        val savedStatusMap =
                            remember { mutableStateMapOf<String, Boolean>() }

                        LazyColumn {
                            items(result) { batchItem ->

                                val isSaved = savedStatusMap[batchItem.batch_id] ?: false
                                EachCourseCardInClass(
                                    item = batchItem,
                                    isSaved = isSaved,
                                    onFavouriteIconClicked = { value ->
                                        vm.onFavoriteClick(
                                            FavouriteCourse(
                                                externalId = value,
                                                name = batchItem.batch_name,
                                                byName = batchItem.byName,
                                                language = batchItem.language,
                                                imageUrl = batchItem.previewImage,
                                                slug = batchItem.slug,
                                                classValue = batchItem.`class`,
                                                isOld = false
                                            )
                                        )
                                        savedStatusMap[batchItem.batch_id] = !isSaved
                                        vm.showToast(
                                            context,
                                            if (!isSaved) "${batchItem.batch_name} added to favourites list"
                                            else "${batchItem.batch_name} removed from favourites list"
                                        )
                                    },
                                    checkIfSaved = {
                                        scope.launch {
                                            val saved =
                                                vm.checkIfItemIsPresentInDb(it)
                                            savedStatusMap[batchItem.batch_id] = saved
                                        }
                                    }
                                ) {

                                    onCLick(
                                        batchItem.batch_id,
                                        batchItem.slug,
                                        batchItem.`class`,
                                        batchItem.batch_name
                                    )

                                }

                            }
                        }
                    } else {
                        Text(text = "No data found")
                    }

                }
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuComponent(
    modifier: Modifier = Modifier,
    label: String,
    items: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
            Log.d("TAG", "DropdownMenuComponent: $items")
        },
        modifier = modifier.padding(10.dp)
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            maxLines = 1,
            modifier = Modifier.menuAnchor(),
//            onClick = { expanded = true },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) })
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onValueChange(item)
                    expanded = false
                }, text = {
                    Text(item)
                })
            }
        }
    }
}