package com.xerox.studyrays.ui.screens.videoPlayerScreen

import com.google.android.exoplayer2.Player

class VideoPlayerListener(val onPlayerReady: () -> Unit) : Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        if (playbackState == Player.STATE_READY) {
            onPlayerReady()
        }
    }
}