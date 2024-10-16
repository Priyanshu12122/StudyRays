package com.xerox.studyrays.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoadingTemplate(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    content: @Composable () -> Unit
) {

    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {

        LazyColumn {
            items(10) {
                content()
            }
        }

    }
}