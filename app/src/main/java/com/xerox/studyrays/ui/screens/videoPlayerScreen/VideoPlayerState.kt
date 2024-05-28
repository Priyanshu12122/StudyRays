package com.xerox.studyrays.ui.screens.videoPlayerScreen

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.google.android.exoplayer2.ui.StyledPlayerView

data class VideoPlayerState(
    val trackSelector: DefaultTrackSelector,
    val qualityList: ArrayList<Pair<String, TrackSelectionOverrides.Builder>>,
    val selectedQuality: Pair<String, TrackSelectionOverrides.Builder>,
    val qualityListGenerated: Boolean,
    val selectedIndex: Int,
    val isVisible: Boolean,
    val isFullScreen: Boolean,
    var playerView: StyledPlayerView,
)