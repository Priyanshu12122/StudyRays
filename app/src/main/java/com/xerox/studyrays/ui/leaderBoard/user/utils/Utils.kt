package com.xerox.studyrays.ui.leaderBoard.user.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xerox.studyrays.ui.theme.AlegreyaFontFamily
import com.xerox.studyrays.ui.theme.AlegreyaSansFontFamily
import com.xerox.studyrays.ui.theme.DarkTeal
import com.xerox.studyrays.ui.theme.LightTeal
import com.xerox.studyrays.ui.theme.TextBlack
import com.xerox.studyrays.ui.theme.TextWhite

@Composable
fun CButton(
    onClick: () -> Unit = {},
    text: String,
    enabled: Boolean = true,
) {
    // make this button also resuable
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF7C9A92)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled
    ) {

        Text(
            text = text,
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = AlegreyaSansFontFamily,
                fontWeight = FontWeight(500),
                color = Color.White
            )
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CTextField(
    onValueChange: (String) -> Unit = {},
    hint: String,
    value: String,
    isError: Boolean,
    errorText: String,
    textColor: Color = if(isSystemInDarkTheme()) TextWhite else TextBlack
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = hint,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = AlegreyaSansFontFamily,
                    color = Color(0xFFBEC2C2)
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color(0xFFBEC2C2),
            unfocusedIndicatorColor = Color(0xFFBEC2C2),
            focusedTextColor = textColor,
            unfocusedTextColor = textColor
        ),
        supportingText = {
            if (isError) {
                Text(
                    text = errorText,
                    style = TextStyle(
                        fontFamily = AlegreyaSansFontFamily,
                        color = MaterialTheme.colorScheme.error
                    )
                )

            }
        },
        maxLines = 2

    )
}

@Composable
fun DontHaveAccountRow(
    onSignupTap: () -> Unit = {},
) {
    Row(
        modifier = Modifier.padding(top = 12.dp, bottom = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            "Already have an account? \n Login with your Unique Id. ",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = AlegreyaSansFontFamily,
                color = Color.White
            )
        )

        Text("Login",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = AlegreyaSansFontFamily,
                fontWeight = FontWeight(800),
                color = Color.White
            ),
            modifier = Modifier.clickable {
                onSignupTap()
            }
        )


    }
}

@Composable
fun TextComposable(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: Int,
    fontSize: Int,
    maxLines: Int = Integer.MAX_VALUE,
    overFlow: TextOverflow = TextOverflow.Clip,
    color: Color = TextWhite
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = fontSize.sp,
            fontFamily = AlegreyaFontFamily,
            fontWeight = FontWeight(fontWeight),
            color = color
        ),
        modifier = modifier,
        maxLines = maxLines,
        overflow = overFlow
    )

}

@Composable
fun TextComposable2(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: Int,
    fontSize: Int,
    color: Color = TextWhite
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = fontSize.sp,
            fontFamily = AlegreyaSansFontFamily,
            fontWeight = FontWeight(fontWeight),
            color = color
        ),
        modifier = modifier
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadOnlyTextField(
//    onValueChange: (String) -> Unit = {},
//    hint: String,
    value: String,
) {
    OutlinedTextField(
        value = value,
        readOnly = true,
        onValueChange = {},
//        placeholder = {
//            Text(
//                text = hint,
//                style = TextStyle(
//                    fontSize = 18.sp,
//                    fontFamily = AlegreyaSansFontFamily,
//                    color = Color(0xFFBEC2C2)
//                )
//            )
//        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color(0xFFBEC2C2),
            unfocusedIndicatorColor = Color(0xFFBEC2C2)
        ),
//        supportingText = {
//            if (isError) {
//                Text(
//                    text = errorText,
//                    style = TextStyle(
//                        fontFamily = AlegreyaSansFontFamily,
//                        color = MaterialTheme.colorScheme.error
//                    )
//                )
//
//            }
//        },
        maxLines = 2

    )
}

@Composable
fun CardComposable(
    modifier: Modifier = Modifier,
    text: String,
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = DarkTeal,

            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        modifier = modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = text,
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = AlegreyaFontFamily,
                fontWeight = FontWeight(500),
                color = Color.White
            ),
            modifier = modifier.padding(10.dp)
        )

    }

}