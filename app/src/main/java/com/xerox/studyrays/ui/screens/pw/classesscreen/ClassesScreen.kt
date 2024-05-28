package com.xerox.studyrays.ui.screens.pw.classesscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.xerox.studyrays.R
import com.xerox.studyrays.utils.EachCardForClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesScreen(
    shouldShow: Boolean,
    onBackClicked: () -> Unit,
    onEachCardClicked: (String) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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

                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")

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
            val list = listOf(
                EachCardForClass(R.drawable.thirteen, "12+"),
                EachCardForClass(R.drawable.twelve, "12"),
                EachCardForClass(R.drawable.eleven, "11"),
                EachCardForClass(R.drawable.ten, "10"),
                EachCardForClass(R.drawable.nine, "9"),
                EachCardForClass(R.drawable.eight, "8"),
                EachCardForClass(R.drawable.seven, "7"),
                EachCardForClass(R.drawable.six, "6"),
            )

            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(list) { eachClass ->
                    EachClassCard(image = eachClass.image, text = eachClass.text) {
                        onEachCardClicked(eachClass.text)
                    }
                }
            }

        }

    }

}

