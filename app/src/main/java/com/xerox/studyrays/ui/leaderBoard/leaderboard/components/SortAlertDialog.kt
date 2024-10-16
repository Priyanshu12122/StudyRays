package com.xerox.studyrays.ui.leaderBoard.leaderboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.xerox.studyrays.ui.leaderBoard.user.utils.TextComposable

@Composable
fun SortAlertDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onDismiss: () -> Unit,
    list: List<String>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,

    ) {


    if (isOpen) {
        AlertDialog(
            modifier = modifier.fillMaxWidth()
                .fillMaxHeight(0.6f),
            onDismissRequest = {
                onDismiss()
            }, confirmButton = {

            },
            text = {
                LazyColumn {
                    item {
                        TextComposable(text = "Sort By Exam:", fontWeight = 800, fontSize = 24)
                    }

                    itemsIndexed(list) { index, item ->
                        DropdownMenuItem(text = {
                            Row(
                                modifier = modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(text = item)

                                RadioButton(
                                    selected = index == selectedIndex, onClick = {
                                        onSelectedChange(index)
                                        onDismiss()
                                    })

                            }
                        }, onClick = {
                            onSelectedChange(index)
                            onDismiss()
                        })
                    }
                }

            }
        )
    }

}