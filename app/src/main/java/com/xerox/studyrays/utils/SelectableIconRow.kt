package com.xerox.studyrays.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xerox.studyrays.ui.theme.TextBlack
import com.xerox.studyrays.ui.theme.TextWhite

@Composable
fun SelectableIconRow(
    modifier: Modifier = Modifier,
    icons: ImmutableIcons,
    isOld: Boolean,
    onTelegramClick: () -> Unit,
    onStarClick: () -> Unit,
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            icons.icons.forEachIndexed { index, iconItem ->

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .shadow(
                            elevation = 20.dp,
                            shape = CircleShape,
                            ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                        )
                        .clip(CircleShape)
                        .background(
                            if (selectedIndex == index) MaterialTheme.colorScheme.primary
                            else if (!isSystemInDarkTheme()) Color.White
                            else MaterialTheme.colorScheme.background
                        )
                        .then(
                            if (isSystemInDarkTheme()) {
                                Modifier.border(
                                    width = 1.dp,
                                    color = Color.White.copy(0.6f),
                                    shape = CircleShape
                                )
                            } else {
                                Modifier
                            }
                        )

                ) {

                    IconButton(
                        onClick = {
                            selectedIndex = index

                            when {
                                !isOld && selectedIndex == 3 -> onStarClick()
                                !isOld && selectedIndex == 4 -> onTelegramClick()
                                isOld && selectedIndex == 1 -> onStarClick()
                                isOld && selectedIndex == 2 -> onTelegramClick()
                            }

                        }, modifier = modifier
                            .align(
                                Alignment.Center
                            )
                            .size(35.dp)
                    ) {

                        if (iconItem.isImageVector){

                            Icon(
                                imageVector = iconItem.icon,
                                contentDescription = null,
                                modifier = modifier.size(25.dp),
                                tint = if(selectedIndex == index ) TextBlack else if (isSystemInDarkTheme()) TextWhite else TextBlack
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = iconItem.painter),
                                contentDescription = null,
                                modifier = modifier.size(25.dp),
                                tint = if(selectedIndex == index ) TextBlack else if (isSystemInDarkTheme()) TextWhite else TextBlack
                            )
                        }

                    }
                }

            }
        }

        SpacerHeight(16.dp)

        HorizontalDivider()

        icons.icons[selectedIndex].content()
    }
}

@Immutable
data class ImmutableIcons(
    val icons: List<IconItem>,
)

data class IconItem(
    val content: @Composable () -> Unit,
    val isImageVector: Boolean,
    val icon: ImageVector,
    val painter: Int,
)





