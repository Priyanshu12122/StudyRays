package com.xerox.studyrays.ui.leaderBoard.user.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xerox.studyrays.ui.theme.AlegreyaSansFontFamily
import com.xerox.studyrays.ui.theme.TextWhite


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    expanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    selectedOptionText: String,
    isError: Boolean,
    errorText: String,
) {

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            onExpandedChanged(!expanded)
        }

    ) {


        OutlinedTextField(
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            placeholder = {
                Text(
                    text = "Select your Goal Exam",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = AlegreyaSansFontFamily,
                        color = Color(0xFFBEC2C2)
                    )
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .menuAnchor(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFFBEC2C2),
                unfocusedIndicatorColor = Color(0xFFBEC2C2),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            supportingText = {
                if (isError){
                    Text(
                        text = errorText,
                        style = TextStyle(
                            fontFamily = AlegreyaSansFontFamily,
                            color = MaterialTheme.colorScheme.error
                        )
                    )

                }
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onExpandedChanged(false)
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(selectionOption)
                        onExpandedChanged(false)
                    },
                    text = {
                        Text(text = selectionOption)
                    }
                )
            }
        }
    }
}