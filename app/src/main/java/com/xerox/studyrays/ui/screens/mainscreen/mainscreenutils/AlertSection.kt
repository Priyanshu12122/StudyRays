package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.xerox.studyrays.R
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.Constants


@Composable
fun AlertSection(
    vm: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val alertItem by vm.alert.collectAsState()
    when (val alertResult = alertItem) {
        is Response.Error -> {

        }

        is Response.Loading -> {

        }

        is Response.Success -> {
            var isOpen by rememberSaveable {
                mutableStateOf(true)
            }
            if (alertResult.data.status == "1" && alertResult.data.versions.contains(Constants.VERSION.toString())){
                AlertDialogForMainScreen(
                    isOpen = isOpen,
                    isDismissable = alertResult.data.dismissable != "yes",
                    imageUrl = alertResult.data.image,
                    text = alertResult.data.description,
                    isRedirectLinkAvailable = alertResult.data.redirect.isNotEmpty(),
                    onDismissClicked = {
                        isOpen = false
                    },
                    onConfirmButtonClicked = {
                        vm.openUrl(
                            context,
                            alertResult.data.redirect
                        )
                    })

            }
        }
    }
}

@Composable
fun AlertDialogForMainScreen(
    isOpen: Boolean,
    isDismissable: Boolean,
    imageUrl: String,
    text: String,
    isRedirectLinkAvailable: Boolean,
    onDismissClicked: () -> Unit,
    onConfirmButtonClicked: () -> Unit,
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = {
                if (!isDismissable) {
                    onDismissClicked()
                }
            },
            confirmButton = {
                if (isRedirectLinkAvailable) {
                    OutlinedButton(
                        onClick = { onConfirmButtonClicked() },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "PROCEED")
                    }

                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.siren),
                            contentDescription = "",
                            modifier = Modifier
                                .size(30.dp)
                                .weight(1f)
                        )

                        Text(
                            text = "Important Notice",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(5f)
                        )


                        if (!isDismissable) {
                            IconButton(onClick = {
                                onDismissClicked()
                            }, modifier = Modifier.weight(1f)) {

                                Icon(imageVector = Icons.Default.Clear, contentDescription = "")
                            }
                        }
                    }

                    HorizontalDivider()

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(130.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = text,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    HorizontalDivider()

                }
            })
    }

}