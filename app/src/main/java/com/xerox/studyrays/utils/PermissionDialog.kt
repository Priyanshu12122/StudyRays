package com.xerox.studyrays.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionDialog(
    showNotificationDialog: MutableState<Boolean>,
    permissionState: PermissionState,
    text: String
) {
    if (showNotificationDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showNotificationDialog.value = false
                permissionState.launchPermissionRequest()
            },
            title = { Text(text = "Permission Required") },
            text = { Text(text = text) },
            confirmButton = {
                TextButton(onClick = {
                    showNotificationDialog.value = false
                    permissionState.launchPermissionRequest()
                }) {
                    Text(text = "OK")
                }
            }
        )
    }
}