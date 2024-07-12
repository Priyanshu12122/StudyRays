package com.xerox.studyrays.ui.screens.pw.favouriteCoursesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.BatchDetailsEntity
import com.xerox.studyrays.model.pwModel.batchDetails.BatchDetails
import com.xerox.studyrays.model.pwModel.batchDetails.BatchDetailsX
import com.xerox.studyrays.ui.screens.pw.coursesscreen.Badge


@Composable
fun EachCardForFavouriteCourse(
    item: BatchDetailsEntity,
    isSaved: Boolean = false,
    checkIfSaved: (String) -> Unit,
    onFavouriteIconClicked: (String) -> Unit,
    onCLick: () -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        checkIfSaved(item.externalId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
            )
            .clip(RoundedCornerShape(10.dp))
            .background(if (!isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.background)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {

                Text(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterStart),
                    text = item.name ?: "Name",
                    maxLines = 1,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )

                Badge(text = item.language ?: "Language",modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterEnd))


            }

            AsyncImage(
                model = item.baseUrl + item.key/*item.previewImage?.let { it.baseUrl + it.key }*/,
                contentDescription = "Image",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )


            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "",
                    modifier = Modifier.weight(
                        1f
                    )
                )
                Text(
                    modifier = Modifier
                        .weight(8f),
                    text = "Target " + item.byName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                )

                IconButton(
                    onClick = {
                        onFavouriteIconClicked(item.externalId)
                    }, modifier = Modifier.weight(
                        1f
                    )
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "",
                        tint = if (isSaved) Color.Red else if ( isSystemInDarkTheme()) Color.White else Color.Black
                    )

                }


            }
            Divider(
                thickness =  1.dp,
                modifier = Modifier
                    .fillMaxWidth(),
                color = if (isSystemInDarkTheme()) Color.White.copy(0.5f) else Color.Black.copy(0.6f)
            )

            Button(
                onClick = {
                    onCLick()
                }, modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                , colors = ButtonDefaults.buttonColors(Color("#5A4BDA".toColorInt())),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "LET'S STUDY",color = Color.White )
            }


        }


    }

}
