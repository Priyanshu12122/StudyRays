package com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.xerox.studyrays.ui.theme.AquaBlue
import com.xerox.studyrays.ui.theme.Beige1
import com.xerox.studyrays.ui.theme.Beige2
import com.xerox.studyrays.ui.theme.Beige3
import com.xerox.studyrays.ui.theme.BlueViolet1
import com.xerox.studyrays.ui.theme.BlueViolet2
import com.xerox.studyrays.ui.theme.BlueViolet3
import com.xerox.studyrays.ui.theme.ButtonBlue
import com.xerox.studyrays.ui.theme.LightGreen1
import com.xerox.studyrays.ui.theme.LightGreen2
import com.xerox.studyrays.ui.theme.LightGreen3
import com.xerox.studyrays.ui.theme.LightRed
import com.xerox.studyrays.ui.theme.OrangeYellow1
import com.xerox.studyrays.ui.theme.OrangeYellow2
import com.xerox.studyrays.ui.theme.OrangeYellow3
import com.xerox.studyrays.utils.SpacerHeight
import com.xerox.studyrays.utils.shimmerEffect


@Composable
fun EachCardForTeacher(
    imageUrl: String,
    teacherName: String,
    featuredLine: String,
    qualifications: String,
    experience: String,
    onClick: () -> Unit,
) {

    val colors = listOf(
        ButtonBlue,
        LightRed,
        AquaBlue,
        OrangeYellow1,
        OrangeYellow2,
        OrangeYellow3,
        Beige1,
        Beige2,
        Beige3,
        LightGreen1,
        LightGreen2,
        LightGreen3,
        BlueViolet1,
        BlueViolet2,
        BlueViolet3
    )



    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
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
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            colors.random(),
                            colors.random(),
                            colors.random()
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SpacerHeight(dp = 6.dp)

            AsyncImage(
                model = imageUrl, contentDescription = "", modifier = Modifier
                    .clip(CircleShape)
                    .size(175.dp)
                    .border(
                        1.dp,
                        shape = CircleShape,
                        brush = Brush.horizontalGradient(
                            listOf(
                                colors.random(),
                                colors.random()
                            )
                        )
                    )
            )
            SpacerHeight(dp = 12.dp)
            Text(text = teacherName, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            SpacerHeight(dp = 12.dp)
            Text(
                text = "Qualifications: $qualifications",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            SpacerHeight(dp = 12.dp)
            Text(
                text = "Experience: $experience years",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            SpacerHeight(dp = 18.dp)
            Text(
                text = "Featured line: $featuredLine",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}


@Composable
fun EachCardForLoadingTeacher(modifier: Modifier = Modifier) {

    val colors = listOf(
        ButtonBlue,
        LightRed,
        AquaBlue,
        OrangeYellow1,
        OrangeYellow2,
        OrangeYellow3,
        Beige1,
        Beige2,
        Beige3,
        LightGreen1,
        LightGreen2,
        LightGreen3,
        BlueViolet1,
        BlueViolet2,
        BlueViolet3
    )

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.DarkGray
            )
            .clip(RoundedCornerShape(10.dp))
            .then(
                Modifier.border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            colors.random(),
                            colors.random(),
                            colors.random()
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SpacerHeight(dp = 6.dp)

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(175.dp)
                    .border(
                        1.dp,
                        shape = CircleShape,
                        brush = Brush.horizontalGradient(
                            listOf(
                                colors.random(),
                                colors.random()
                            )
                        )
                    )
                    .shimmerEffect()
            )

            Box(
                modifier = modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxSize()
                    .shimmerEffect()
            )


        }
    }
}
