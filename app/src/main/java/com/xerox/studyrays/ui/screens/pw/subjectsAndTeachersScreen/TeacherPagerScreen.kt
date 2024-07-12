package com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.xerox.studyrays.model.pwModel.batchDetailss.Teacher


@Composable
fun TeacherPagerScreen(
    list: List<Teacher?>,
) {

    LazyColumn {
        items(list) {

            var isOpen by remember {
                mutableStateOf(false)
            }

            if (isOpen) {
                TeacherAlertDialog(
                    imageUrl = it?.image ?: "",
                    teacherName = it?.name ?: "Not provided",
                    qualifications = it?.qualification ?: "Not provided",
                    experience = it?.experience ?: "Not provided"
                ) {
                    isOpen = false
                }
            }

            EachCardForTeacher(
                imageUrl = it?.image ?: "",
                teacherName = it?.name ?: "Not provided",
                qualifications = it?.qualification ?: "Not provided",
                featuredLine = "Hello hello, nazre screen par!",
                experience = it?.experience ?: "Not provided"
            ) {
                isOpen = true

            }

        }
    }
}

@Composable
fun TeacherAlertDialog(
    imageUrl: String,
    teacherName: String,
    qualifications: String,
    experience: String,
    onDismissClicked: () -> Unit,
) {
    AlertDialog(onDismissRequest = { onDismissClicked() }, confirmButton = { }, text = {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "",
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .height(250.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = teacherName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Qualifications: $qualifications",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Experience: $experience years",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp)
            )

        }
    }
    )
}
