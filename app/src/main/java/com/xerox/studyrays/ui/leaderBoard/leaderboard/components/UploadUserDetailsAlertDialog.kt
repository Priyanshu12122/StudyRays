package com.xerox.studyrays.ui.leaderBoard.leaderboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xerox.studyrays.ui.leaderBoard.user.utils.TextComposable
import com.xerox.studyrays.ui.leaderBoard.user.utils.TextComposable2
import com.xerox.studyrays.utils.SpacerHeight

@Composable
fun UploadUserDetailsAlertDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    isOpen: Boolean,
) {

    if (isOpen) {

        AlertDialog(
            onDismissRequest = { onDismiss() }, confirmButton = {},
            text = {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TextComposable(
                        text = "Are you sure you want to upload you data?",
                        fontWeight = 800,
                        fontSize = 22
                    )

                    SpacerHeight(dp = 5.dp)


                    SpacerHeight(dp = 8.dp)


                    OutlinedButton(
                        modifier = modifier.fillMaxWidth(),
                        onClick = {
                            onConfirmClick()
                        }) {
                        TextComposable2(text = "PROCEED", fontWeight = 800, fontSize = 20)
                    }

                }
            })
    }

}