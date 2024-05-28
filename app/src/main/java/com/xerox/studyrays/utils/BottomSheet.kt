package com.xerox.studyrays.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    sheetState: SheetState,
    onMarkAsCompletedClicked: () -> Unit,
    onDownloadClicked: () -> Unit,
    isCompleted: Boolean,
    onDismiss: () -> Unit,
) {

    val items = listOf(
        BottomSheetItem(
            text = "Mark as Finished",
            otherText = "Remove from Finished",
            icon = Icons.Default.Favorite
        )
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }) {
        items.forEach {
            EachColumnForBottomSheet(item = it,isCompleted = isCompleted) {
                when (it.text) {
                    "Mark as Finished" -> {
                        onMarkAsCompletedClicked()
                    }

                }
            }
        }

    }
}

@Composable
fun EachColumnForBottomSheet(
    item: BottomSheetItem,
    isCompleted: Boolean,
    onClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .height(80.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
            )
            .clip(RoundedCornerShape(10.dp))
            .background(if (!isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.background)
            .clickable {
                onClick()
            }
            .then(
                if (isSystemInDarkTheme()) {
                    Modifier.border(
                        1.dp,
                        Color.White.copy(0.6f),
                        RoundedCornerShape(10.dp)
                    )
                } else {
                    Modifier
                }
            )
    ) {

        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = if (isCompleted) item.otherText else item.text)

        }


    }
}

data class BottomSheetItem(
    val text: String,
    val otherText: String,
    val icon: ImageVector,
)