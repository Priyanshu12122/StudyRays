package com.xerox.studyrays.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.xerox.studyrays.cacheDb.pwCache.courseDb.PwCourseEntity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun navigateTo(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(0) {
            inclusive = true
        }
//        launchSingleTop = true
    }
}

fun navigateTo2(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(route)
    }
}

fun navigateTo3(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(route){
            inclusive = true
        }
    }
}



data class EachCardForClass(
    val image: Int,
    val text: String,
)

data class Category(
    val name: String,
    val category: MutableList<PwCourseEntity>,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryTabRow(
    pagerState: PagerState,
    categories: List<Category>,
    onTabClicked: (Int) -> Unit,
) {
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        edgePadding = 2.dp
    ) {
        categories.forEachIndexed { index, category ->

            Tab(selected = pagerState.currentPage == index, onClick = { onTabClicked(index) }) {
                Text(
                    text = category.name,
                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp)
                )

            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryTabRow2(
    pagerState: PagerState,
    categories: List<String>,
    modifier: Modifier = Modifier,
    onTabClicked: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
    ) {
        categories.forEachIndexed { index, category ->

            Tab(selected = pagerState.currentPage == index, onClick = { onTabClicked(index) }) {
                Text(
                    text = category,
                    modifier = modifier.padding(horizontal = 2.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryTabRow3(
    pagerState: PagerState,
    categories: List<ImageVector>,
    modifier: Modifier = Modifier,
//    onPageClick: @Composable (Int) -> Unit,
    onTabClicked: (Int) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            categories.forEachIndexed { index, category ->

                Tab(selected = pagerState.currentPage == index, onClick = { onTabClicked(index) }) {
                    Icon(
                        imageVector = category,
                        contentDescription = "",
                        modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp)
                    )
                }
            }
        }

//        HorizontalPager(state = pagerState) {
//            onPageClick(it)
//
//        }

    }
}

fun Int.formatToIndianRupees(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    format.currency = Currency.getInstance("INR")
    return format.format(this)
}


fun String.toRelativeTime(): String {
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(this)
    val currentTime = Calendar.getInstance().time
    val difference = currentTime.time - date.time

    val daysAgo = difference / (1000 * 60 * 60 * 24)
    val hoursAgo = difference / (1000 * 60 * 60)
    val minutesAgo = difference / (1000 * 60)

    return when {
        daysAgo > 0 -> "$daysAgo days ago"
        hoursAgo > 0 -> "$hoursAgo hours ago"
        else -> "$minutesAgo minutes ago"
    }
}

@Composable
fun SpacerHeight(dp: Dp) {
    Spacer(modifier = Modifier.height(dp))
}

@Composable
fun SpacerWidth(dp: Dp) {
    Spacer(modifier = Modifier.width(dp))
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000)
        ), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                if (isSystemInDarkTheme()) Color(0xFF535151) else Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                if (isSystemInDarkTheme()) Color(0xFF535151) else Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

fun decrypt(data: ByteArray, key: String, iv: String): String {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val secretKeySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
    val ivParameterSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
    val decryptedBytes = cipher.doFinal(data)
    return String(decryptedBytes, Charsets.UTF_8)
}

fun Long.toReadableDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
    return format.format(date)
}


@Composable
fun BadgeWithCrossIcon(text: String,modifier: Modifier = Modifier,onClick: (String) -> Unit, onCrossClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                onClick(text)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onCrossClick, modifier = Modifier.size(16.dp)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }
        }
    }
}

fun String.toTimeAgo(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = dateFormat.parse(this)
    val now = Date()
    val duration = now.time - date.time

    val days = TimeUnit.MILLISECONDS.toDays(duration)
    val hours = TimeUnit.MILLISECONDS.toHours(duration)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)

    return when {
        days > 30 -> {
            val months = days / 30
            if (months == 1L) "1 month ago" else "$months months ago"
        }
        days > 7 -> {
            val weeks = days / 7
            if (weeks == 1L) "1 week ago" else "$weeks weeks ago"
        }
        days > 0 -> {
            if (days == 1L) "1 day ago" else "$days days ago"
        }
        hours > 0 -> {
            if (hours == 1L) "1 hour ago" else "$hours hours ago"
        }
        minutes > 0 -> {
            if (minutes == 1L) "1 minute ago" else "$minutes minutes ago"
        }
        else -> "Just now"
    }
}






