package com.xerox.studyrays.ui.leaderBoard.user.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xerox.studyrays.ui.theme.AlegreyaFontFamily
import com.xerox.studyrays.ui.theme.AlegreyaSansFontFamily
import com.xerox.studyrays.ui.theme.DarkTeal
import com.xerox.studyrays.utils.SpacerHeight

@Composable
fun WarningAlertDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onProceed: () -> Unit,
    isLoading: Boolean,
    loadingScreen: @Composable () -> Unit,
) {

    if (isOpen) {
        AlertDialog(
            containerColor = DarkTeal,
            onDismissRequest = { onDismiss() },
            confirmButton = {},
            text = {

            if(!isLoading){
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Warning!!!", style = TextStyle(
                            fontSize = 25.sp,
                            fontFamily = AlegreyaFontFamily,
                            fontWeight = FontWeight(800),
                            color = MaterialTheme.colorScheme.error
                        )
                    )
                    SpacerHeight(dp = 2.dp)
                    Text(
                        text = "Be Careful, as Using bad Names will get you banned!! \n Do you wish to Proceed With Your Current name?",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = AlegreyaSansFontFamily,
                            fontWeight = FontWeight(500),
                            color = Color.White
                        ),
                        modifier = Modifier.padding(10.dp)
                    )

                    SpacerHeight(dp = 16.dp)

                    OutlinedButton(
                        onClick = {
                            onProceed()
                        },
                        modifier = modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "CREATE",
                            style = TextStyle(
                                fontFamily = AlegreyaFontFamily,
                                fontWeight = FontWeight(500),
                                color = Color.White
                            )
                        )

                    }

                }

            } else {
                loadingScreen()
            }

//                }

            }
        )
    }

}