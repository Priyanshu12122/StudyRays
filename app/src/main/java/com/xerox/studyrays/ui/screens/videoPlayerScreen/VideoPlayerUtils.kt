package com.xerox.studyrays.ui.screens.videoPlayerScreen

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.Log
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.drm.DefaultDrmSessionManagerProvider
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource


fun DefaultTrackSelector.generateQualityList(): ArrayList<Pair<String, TrackSelectionOverrides.Builder>> {
    val trackOverrideList = ArrayList<Pair<String, TrackSelectionOverrides.Builder>>()

    val renderTrack = this.currentMappedTrackInfo
    val renderCount = renderTrack?.rendererCount ?: 0
    for (rendererIndex in 0 until renderCount) {
        if (isSupportedFormat(renderTrack, rendererIndex)) {
            val trackGroupType = renderTrack?.getRendererType(rendererIndex)
            val trackGroups = renderTrack?.getTrackGroups(rendererIndex)
            val trackGroupsCount = trackGroups?.length!!
            if (trackGroupType == C.TRACK_TYPE_VIDEO) {
                for (groupIndex in 0 until trackGroupsCount) {
                    val videoQualityTrackCount = trackGroups[groupIndex].length
                    for (trackIndex in 0 until videoQualityTrackCount) {
                        val isTrackSupported = renderTrack.getTrackSupport(
                            rendererIndex,
                            groupIndex,
                            trackIndex
                        ) == C.FORMAT_HANDLED
                        if (isTrackSupported) {
                            val track = trackGroups[groupIndex]
                            val trackName =
                                "${track.getFormat(trackIndex).width} x ${
                                    track.getFormat(
                                        trackIndex
                                    ).height
                                }"
                            if (track.getFormat(trackIndex).selectionFlags == C.SELECTION_FLAG_AUTOSELECT) {
                                trackName.plus(" (Default)")
                            }
                            val trackBuilder =
                                TrackSelectionOverrides.Builder()
                                    .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                                    .addOverride(
                                        TrackSelectionOverrides.TrackSelectionOverride(
                                            track,
                                            listOf(trackIndex)
                                        )
                                    )
                            trackOverrideList.add(Pair(trackName, trackBuilder))
                        }
                    }
                }
            }
        }
    }
    Log.d("TAG", "generateQualityList: $trackOverrideList")
    return trackOverrideList
}


fun isSupportedFormat(
    mappedTrackInfo: MappingTrackSelector.MappedTrackInfo?,
    rendererIndex: Int
): Boolean {
    val trackGroupArray = mappedTrackInfo?.getTrackGroups(rendererIndex)
    return if (trackGroupArray?.length == 0) {
        false
    } else mappedTrackInfo?.getRendererType(rendererIndex) == C.TRACK_TYPE_VIDEO || mappedTrackInfo?.getRendererType(
        rendererIndex
    ) == C.TRACK_TYPE_AUDIO || mappedTrackInfo?.getRendererType(rendererIndex) == C.TRACK_TYPE_TEXT
}

fun buildMediaSource(
    context: Context,
    uri: String,
    drmConfiguration: MediaItem.DrmConfiguration
): MediaSource {
    val httpDataSourceFactory = DefaultHttpDataSource.Factory()
    val dataSourceFactory = DefaultDataSource.Factory(context, httpDataSourceFactory)
    val mediaItem = MediaItem.Builder()
        .setUri(Uri.parse(uri))
        .setDrmConfiguration(drmConfiguration)
        .build()
    return DashMediaSource.Factory(dataSourceFactory)
        .setDrmSessionManagerProvider(DefaultDrmSessionManagerProvider())
        .createMediaSource(mediaItem)
}

fun getCurrentQuality(player: ExoPlayer): String {
    val videoFormat = player.videoFormat
    return if (videoFormat != null) {
        "${videoFormat.width} x ${videoFormat.height}"
    } else {
        "Unknown"
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.setScreenOrientation(orientation: Int) {
    val activity = this.findActivity() ?: return
    activity.requestedOrientation = orientation
//    if (orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
//        hideSystemUi()
//    } else {
//        showSystemUi()
//    }
}

fun Context.hideSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
    Log.d("TAG", "hideSystemUi: hiding system ui")
}

fun Context.showSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).show(WindowInsetsCompat.Type.systemBars())
    Log.d("TAG", "showing: showing system ui")
}

