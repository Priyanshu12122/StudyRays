package com.xerox.studyrays.ui.leaderBoard.user.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xerox.studyrays.ui.theme.AlegreyaSansFontFamily


@Composable
fun GenderSelection(
    gender: String,
    onGenderSelected: (String) -> Unit,
    isError: Boolean,
    errorText: String,
) {



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (!isError){
            Text(text = "Select Gender",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = AlegreyaSansFontFamily,
                    color = Color(0xFFBEC2C2)
                ),
                modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
            )

        } else {
            Text(text = errorText,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = AlegreyaSansFontFamily,
                    color = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
            )
        }


        GenderOption(
            label = "Male",
            selected = gender == "Male",
            onClick = { onGenderSelected("Male") }
        )

        GenderOption(
            label = "Female",
            selected = gender == "Female",
            onClick = { onGenderSelected("Female") }
        )
    }
}

@Composable
fun GenderOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
            .background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
            .padding(8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label,
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = AlegreyaSansFontFamily,
                color = if(selected) Color(0xFFBEC2C2).copy(alpha = 0.5f) else Color(0xFFBEC2C2)
            )
        )

    }
}