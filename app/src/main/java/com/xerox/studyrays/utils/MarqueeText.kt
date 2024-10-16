package com.xerox.studyrays.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay

@Composable
fun MarqueeText(
    textList: List<String>,
    initialTextIndex: Int = 0,
    displayDurationMillis: Int = 3000,
    transitionDurationMillis: Int = 1000
) {
    var currentTextIndex by remember { mutableIntStateOf(initialTextIndex) }
    var visible by remember { mutableStateOf(true) }
    val currentText = textList[currentTextIndex]

    LaunchedEffect(currentTextIndex) {
        // Initial delay for displaying the text
        delay(displayDurationMillis.toLong())
        // Start transition
        visible = false
        // Wait for the exit transition to complete
        delay(transitionDurationMillis.toLong())
        // Move to the next text
        currentTextIndex = (currentTextIndex + 1) % textList.size
        // Start the entry transition
        visible = true
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth }
            ) + fadeIn(animationSpec = tween(transitionDurationMillis)),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }
            ) + fadeOut(animationSpec = tween(transitionDurationMillis))
        ) {
            Text(
                text = currentText,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}