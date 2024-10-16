package com.xerox.studyrays.ui.screens.pw.coursesscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import com.xerox.studyrays.R
import com.xerox.studyrays.model.pwModel.CourseItem
import com.xerox.studyrays.model.pwModel.CourseItemX
import com.xerox.studyrays.utils.formatDateString
import com.xerox.studyrays.utils.shimmerEffect

@Composable
fun EachCourseCardInClass(
    item: CourseItemX,
    isSaved: Boolean = false,
    checkIfSaved: (String) -> Unit,
    onFavouriteIconClicked: (String) -> Unit,
    onCLick: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        checkIfSaved(item.batch_id)
    }

    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 15.sp)) {
            append("Stats On : ")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)) {
            append(formatDateString(item.startDate))
        }
        withStyle(style = SpanStyle(fontSize = 15.sp)) {
            append(" | Class : ")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)) {
            append(item.`class`)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
//            Box(modifier = Modifier.fillMaxWidth()) {

            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
//                        .align(Alignment.CenterStart)
                ,
                text = item.batch_name,
                maxLines = 2,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )

//                Badge(
//                    text = item.language, modifier = Modifier
//                        .padding(10.dp)
//                        .align(Alignment.CenterEnd)
//                )


//            }

            AsyncImage(
                model = item.previewImage,
                contentDescription = "Image",
                placeholder = painterResource(id = R.drawable.pwrectangle),
                error = painterResource(id = R.drawable.pwrectangle),
                fallback = painterResource(id = R.drawable.pwrectangle),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillWidth
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.student),
                    contentDescription = "",
                    modifier = Modifier
                        .size(28.dp)
                        .weight(1f)
                )
                Text(
                    modifier = Modifier
                        .weight(8f),
                    text = item.byName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )

                IconButton(
                    onClick = {
                        onFavouriteIconClicked(item.batch_id)
                    }, modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "",
                        tint = if (isSaved) Color.Red else Color.White
                    )

                }


            }

            Text(text = text, maxLines = 1, overflow = TextOverflow.Ellipsis)

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = if (isSystemInDarkTheme()) Color.White.copy(0.5f) else Color.Black.copy(0.6f)
            )

            Button(
                onClick = {
                    onCLick()
                },
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color("#5A4BDA".toColorInt())),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "LET'S STUDY", color = Color.White)
            }
        }
    }

}

@Composable
fun LoadingCourseCardInClass(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {

//            Text("text1")


            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
//                        .align(Alignment.CenterStart)
                ,
                text = "               ",
                maxLines = 2,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )




            AsyncImage(
                model = "",
                contentDescription = "Image",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
                contentScale = ContentScale.FillWidth
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmerEffect(),
            ) {

            }


            Text(
                text = "  ",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color.White.copy(0.5f)
            )

            Text(
                text = "",
                minLines = 2,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun Previeww(modifier: Modifier = Modifier) {

    LoadingCourseCardInClass()
}


@Composable
fun EachCourseCardInClass2(
    item: CourseItem,
    isSaved: Boolean = false,
    checkIfSaved: (String) -> Unit,
    onFavouriteIconClicked: (String) -> Unit,
    onCLick: () -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        checkIfSaved(item.external_id)
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
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
//            Box(modifier = Modifier.fillMaxWidth()) {

            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                text = item.name,
                maxLines = 1,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )

//                Badge(
//                    text = item.language, modifier = Modifier
//                        .padding(10.dp)
//                        .align(Alignment.CenterEnd)
//                )
//
//
//            }

            AsyncImage(
                model = item.previewImage_baseUrl + item.previewImage_key,
                contentDescription = "Image",
                placeholder = painterResource(id = R.drawable.pwrectangle),
                error = painterResource(id = R.drawable.pwrectangle),
                fallback = painterResource(id = R.drawable.pwrectangle),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillWidth
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.student),
                    contentDescription = "",
                    modifier = Modifier
                        .size(28.dp)
                        .weight(1f)
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
                        onFavouriteIconClicked(item.external_id)
                    }, modifier = Modifier.weight(
                        1f
                    )
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "",
                        tint = if (isSaved) Color.Red else if (isSystemInDarkTheme()) Color.White else Color.Black
                    )

                }


            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = if (isSystemInDarkTheme()) Color.White.copy(0.5f) else Color.Black.copy(0.6f)
            )

            Text(text = "Class : ${item.`class`}", fontWeight = FontWeight.Bold)

            Button(
                onClick = {
                    onCLick()
                },
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color("#5A4BDA".toColorInt())),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "LET'S STUDY", color = Color.White)
            }
        }
    }

}


@Composable
fun Badge(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isSystemInDarkTheme()) Color.White.copy(0.5f) else Color.DarkGray,
                    shape = RoundedCornerShape(12)
                )
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSystemInDarkTheme()) Color.Black else Color.White,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .align(Alignment.Center)
            )
        }
    }
}