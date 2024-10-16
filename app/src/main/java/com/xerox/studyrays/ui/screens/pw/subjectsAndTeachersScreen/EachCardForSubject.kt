package com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.xerox.studyrays.utils.shimmerEffect


@Composable
fun EachCardForSubject(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    text: String,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .padding(10.dp)
            .height(120.dp)
            .width(80.dp)
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = imageUrl ?: "https://www.pw.live/study/assets/batches-new/Physics.svg",
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .background(Color.White),

                )
            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = text,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 4.dp)


            )

        }

    }

}

@Composable
fun EachCardForSubjectLoading(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .padding(10.dp)
            .height(120.dp)
            .width(80.dp)
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = "",
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .shimmerEffect(),

                )
            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "",
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .shimmerEffect()


            )

        }
    }


}