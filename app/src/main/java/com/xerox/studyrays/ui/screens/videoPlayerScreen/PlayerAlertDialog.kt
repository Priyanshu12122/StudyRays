package com.xerox.studyrays.ui.screens.videoPlayerScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.xerox.studyrays.ui.theme.MainPurple


@Composable
fun PlayerAlertDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    selectedIndex: Int,
    qualityList: ArrayList<Pair<String, TrackSelectionOverrides.Builder>>,
    vm: VideoViewModel = hiltViewModel(),

    ) {

    val list = listOf(
        "0.5x",
        "0.75x",
        "1.0x",
        "1.25x",
        "1.5x",
        "1.75x",
        "2.0x",
        "2.25x",
        "2.5x",
        "2.75x",
        "3.0x",
        "3.25x",
        "3.5x",
        "3.75x",
        "4.0x"
    )

    if (isOpen) {
        AlertDialog(onDismissRequest = {
            onDismiss()
        }, confirmButton = {

        },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Row(modifier = Modifier.fillMaxWidth()) {

                        LazyColumn(modifier = Modifier.weight(1f)) {
                            item {
                                Text(text = "Select speed")
                            }
                            items(list) {
                                val speed = it.substringBefore("x").toFloat()
                                Text(text = it,
                                    color = if (vm.findPlayBackSpeed() == speed) MainPurple else if (isSystemInDarkTheme()) Color.White else Color.Black,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .clickable {
                                            vm.onPlayBackSpeedChanged(speed)
                                            onDismiss()
                                        }
                                )
                            }
                        }
                        
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            
                            
                            
                             
                            item {
                                Text(text = "Select quality")
                            }

                            qualityList.forEachIndexed { index, quality ->
                                item {
                                    Text(
                                        text = quality.first.substringAfter("x ") + "p",
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .clickable {
                                                vm.onQualitySelected(index, quality)
                                                onDismiss()
                                            },
                                        color = if (index == selectedIndex) MainPurple else if (isSystemInDarkTheme()) Color.White else Color.Black
                                    )
                                }
                            }
                        }

                    }
                }
            }
        )
    }

}