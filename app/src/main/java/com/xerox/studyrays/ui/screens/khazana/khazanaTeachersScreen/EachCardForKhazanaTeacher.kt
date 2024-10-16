package com.xerox.studyrays.ui.screens.khazana.khazanaTeachersScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.xerox.studyrays.R
import com.xerox.studyrays.model.khazanaModel.khazanaTeachersItem.KhazanaTeacherItem
import com.xerox.studyrays.utils.shimmerEffect

@Composable
fun EachCardForKhazanaTeacher(
    item: KhazanaTeacherItem,
    isSaved: Boolean,
    onFavouriteIconClicked: (String) -> Unit,
    checkIfSaved: (String) -> Unit,
    onClick: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        checkIfSaved(item.external_id)
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .height(250.dp)
            .width(150.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.LightGray
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
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(modifier = Modifier.wrapContentSize()) {

                AsyncImage(
                    model = item.imageId_baseUrl + item.imageId_key,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.pwrectangle),
                    error = painterResource(id = R.drawable.pwrectangle),
                    fallback = painterResource(id = R.drawable.pwrectangle),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(90.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                )

                IconButton(
                    onClick = {
                        onFavouriteIconClicked(item.external_id)
                    }, modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(5.dp)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "",
                        tint = if (isSaved) Color.Red else if (isSystemInDarkTheme()) Color.White else Color.Black
                    )

                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = item.name + " " + item.description.substringBefore(";"),
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(horizontal = 10.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(15.dp))

            Text(
                text = "Total Lectures: ${item.totalLectures}", style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

        }

    }
}


@Composable
fun EachCardForKhazanaTeacherLoading() {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .height(250.dp)
            .width(150.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.LightGray
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
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(modifier = Modifier.wrapContentSize()) {

                AsyncImage(
                    model = "",
                    contentDescription = "",
//                    contentScale = ContentScale.Crop,
//                    placeholder = painterResource(id = R.drawable.pwrectangle),
//                    error = painterResource(id = R.drawable.pwrectangle),
//                    fallback = painterResource(id = R.drawable.pwrectangle),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(90.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .shimmerEffect()
                )

                IconButton(
                    onClick = {

                    }, modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "",
                        tint = Color.White
                    )

                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
//                maxLines = 3,
//                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(15.dp))

            Text(
                text = "",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
            )

        }

    }
}

