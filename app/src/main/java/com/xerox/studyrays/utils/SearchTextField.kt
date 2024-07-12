package com.xerox.studyrays.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onCrossIconClicked: () -> Unit,
    text: String,
    onSearch: (() -> Unit)? = null,
    ) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current

    OutlinedTextField(
        value = searchText,
        onValueChange = { onSearchTextChanged(it) },
        modifier = modifier
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .fillMaxWidth()
            ,
        placeholder = {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            if (onSearch != null) {
                onSearch()
            }
        }),
        singleLine = true,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            IconButton(onClick = {
                if (searchText == ""){
                    keyboardController?.hide()
                    focus.clearFocus()
                } else {
                    onCrossIconClicked()
                }
            }
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "")
            }
        }
    )

}

@Composable
fun SearchTextField2(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onCrossIconClicked: () -> Unit,
    text: String,
    onFocused: () -> Unit,
    onUnFocused: () -> Unit,
    onSearch: (() -> Unit)? = null,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            onFocused()
        } else {
            onUnFocused()
        }
    }

    OutlinedTextField(
        value = searchText,
        onValueChange = { onSearchTextChanged(it) },
        modifier = modifier
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
        ,
        placeholder = {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            if (onSearch != null) {
                onSearch()
            }
        }),
        singleLine = true,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            IconButton(onClick = {
                if (searchText == ""){
                    keyboardController?.hide()
                    focus.clearFocus()
                } else {
                    onCrossIconClicked()
                }
            }
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "")
            }
        }
    )

}