package com.xerox.studyrays.ui.screens.pw.chaptersScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xerox.studyrays.ui.theme.MainPurple
import com.xerox.studyrays.utils.SpacerWidth
import com.xerox.studyrays.utils.shimmerEffect


@Composable
fun EachCardForChapters(
    modifier: Modifier = Modifier,
    lessonName: String,
    notes: String,
    exercises: String,
    videos: String,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.DarkGray
            )
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onClick()
            }
            .then(
                Modifier.border(
                    1.dp,
                    Color.White.copy(0.6f),
                    RoundedCornerShape(10.dp)
                )

            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Divider(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
                    .width(3.5.dp),
                color = MainPurple,
                thickness = 0.dp
            )

            Column(
                modifier = Modifier.weight(7f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = lessonName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MainPurple,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                )

                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(text = "$videos Videos", fontWeight = FontWeight.SemiBold)

                    Divider(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxHeight()
                            .width(1.5.dp)
                    )

                    Text(text = "$exercises Exercises", fontWeight = FontWeight.SemiBold)

                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.5.dp)
                    )
                    Text(text = "$notes Notes", fontWeight = FontWeight.SemiBold)


                }


            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "",
                modifier = Modifier.weight(
                    1f
                )
            )

        }

    }
}

@Composable
fun EachCardForChapterLoading(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.DarkGray
            )
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .then(
                Modifier.border(
                    1.dp,
                    Color.White.copy(0.6f),
                    RoundedCornerShape(10.dp)
                )

            )
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            VerticalDivider(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight(),
                color = MainPurple,
                thickness = 3.5.dp
            )

            Column(
                modifier = Modifier.weight(7f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.Start
            ) {


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .weight(1f)
                        .shimmerEffect()
                )

                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    SpacerWidth(5.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .weight(1f)
                            .shimmerEffect()
                    )

//                    SpacerWidth(5.dp)


                    VerticalDivider(
                        thickness = 1.5.dp,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxHeight()
                    )

//                    SpacerWidth(5.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .weight(1f)
                            .shimmerEffect()
                    )

//                    SpacerWidth(5.dp)


                    VerticalDivider(
                        thickness = 1.5.dp,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxHeight()
                    )

//                    SpacerWidth(5.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .weight(1f)
                            .shimmerEffect()
                    )

                    SpacerWidth(5.dp)

                }


            }

//            Icon(
//                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
//                contentDescription = "",
//                modifier = Modifier.weight(
//                    1f
//                )
//            )

        }

    }

}