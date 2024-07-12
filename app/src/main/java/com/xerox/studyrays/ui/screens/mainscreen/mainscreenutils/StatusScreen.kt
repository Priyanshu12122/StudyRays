package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.R
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.SpacerWidth

@Composable
fun StatusScreen(
    modifier: Modifier = Modifier,
    vm: MainViewModel = hiltViewModel(),
) {

    val state by vm.status.collectAsState()

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        when(val result = state){
            is Response.Error -> {

                AssistChip(onClick = {
                   vm.getStatus()
                }, label = {
                    Text(text = "Error in fetching, error = ${result.msg}")

                    SpacerWidth(dp = 8.dp)

                    IconButton(onClick = {
                        vm.getStatus()
                    }) {
                        Icon(painter = painterResource(id = R.drawable.refresh_24), contentDescription = "")
                    }
                },
                    modifier = modifier.padding(horizontal = 12.dp,vertical = 0.dp))

            }
            is Response.Loading -> {
                AssistChip(onClick = {}, label = {
                    Text(text = "Video Status: ")
                })

            }
            is Response.Success -> {

                AssistChip(onClick = {
                    vm.getStatus()
                }, label = {
                    Text(text = "Video Status: ${result.data.status}")

                    SpacerWidth(dp = 4.dp)

                    Icon(painter = painterResource(
                        id = if (result.data.status.equals(
                                "working",
                                true
                            )
                        ) R.drawable.happiness else R.drawable.sad
                    ), contentDescription = "",
                        tint = if (result.data.status.equals(
                                "working",
                                true
                            )
                        ) Color.Green else MaterialTheme.colorScheme.error,
                        modifier = modifier.size(25.dp))

                    SpacerWidth(dp = 8.dp)

                    IconButton(onClick = {
                        vm.getStatus()
                    }) {
                        Icon(painter = painterResource(id = R.drawable.refresh_24), contentDescription = "")
                    }
                },
                    modifier = modifier.padding(horizontal = 12.dp,vertical = 0.dp))

            }
        }

    }


}