package com.xerox.studyrays.ui.screens.videoPlayerScreen

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.google.android.exoplayer2.ui.StyledPlayerView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class VideoViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    private var playerr: ExoPlayer? = null

    private val _state: MutableStateFlow<VideoPlayerState> = MutableStateFlow(
        VideoPlayerState(
            trackSelector = DefaultTrackSelector(context, AdaptiveTrackSelection.Factory()),
            qualityList = ArrayList(),
            selectedQuality = "" to TrackSelectionOverrides.Builder(),
            qualityListGenerated = false,
            selectedIndex = 0,
            isVisible = false,
            isFullScreen = false,
            playerView = StyledPlayerView(context)
        )
    )

    val state = _state.asStateFlow()

    fun onBackButtonClicked() {
        _state.value = _state.value.copy(
            isFullScreen = false
        )
    }

    fun onPlayBackSpeedChanged(speed: Float) {
        playerr?.setPlaybackSpeed(speed)
    }

    fun findPlayBackSpeed(): Float {
        return playerr?.playbackParameters?.speed ?: 1.0f
    }


    private val playerListener = VideoPlayerListener() {
        if (!_state.value.qualityListGenerated) {
            _state.value.trackSelector.generateQualityList().let {
                _state.value = _state.value.copy(
                    qualityList = it,
                    selectedQuality = it.firstOrNull() ?: ("" to TrackSelectionOverrides.Builder()),
                    qualityListGenerated = true
                )
            }
        }


    }


    fun initialisePlayer(context: Context, url: String, licenseUrl: String) {
        playerr?.release()
        playerr = ExoPlayer.Builder(context)
            .setTrackSelector(_state.value.trackSelector)
            .build().apply {
                val drmConfig = MediaItem.DrmConfiguration.Builder(C.CLEARKEY_UUID)
                    .setLicenseUri(licenseUrl)

                val mediaItem = MediaItem.Builder()
                    .setUri(Uri.parse(url))
                    .setDrmConfiguration(drmConfig.build())
                    .build()

                val mediaSource = buildMediaSource(
                    context,
                    uri = url,
                    drmConfig.build()
                )
                setMediaSource(mediaSource)
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
        playerr!!.addListener(playerListener)
        playerr!!.addListener(object : Player.Listener {

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)

                showToast(
                    context,
                    "Error in playing video, error = ${error.message}. \n Please try again later"
                )

            }

            override fun onPlayerErrorChanged(error: PlaybackException?) {
                super.onPlayerErrorChanged(error)
                showToast(
                    context,
                    "Error in playing video, error = ${error?.message}. \n Please try again later"
                )
            }

        })
    }


    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }


    fun playerViewBuilder(ctx: Context): StyledPlayerView {
        _state.value.playerView = StyledPlayerView(ctx).apply {
            player = playerr
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setShowNextButton(false)
            setShowPreviousButton(false)
            setShowBuffering(StyledPlayerView.SHOW_BUFFERING_WHEN_PLAYING)
            keepScreenOn = true
            setControllerOnFullScreenModeChangedListener {
                _state.value = _state.value.copy(isFullScreen = it)
                with(ctx) {
                    if (it) {
                        setScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
                    } else {
                        setScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    }
                }
            }

            setControllerVisibilityListener { visibility ->
                _state.value = _state.value.copy(isVisible = visibility == View.VISIBLE)
            }
        }
        return _state.value.playerView
    }

    fun onQualitySelected(index: Int, quality: Pair<String, TrackSelectionOverrides.Builder>) {
        _state.value = _state.value.copy(selectedQuality = quality)

        playerr?.trackSelectionParameters = playerr?.trackSelectionParameters?.buildUpon()
            ?.setTrackSelectionOverrides(quality.second.build())?.build()!!

        _state.value.trackSelector.parameters = _state.value.trackSelector.parameters
            .buildUpon()
            .setTrackSelectionOverrides(quality.second.build())
            .setTunnelingEnabled(true)
            .build()

        _state.value = _state.value.copy(selectedIndex = index)
    }

    fun releasePlayer() {
        playerr?.release()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }


}

//  screenRotateBtn.setOnClickListener(view -> {
//            int currentOrientation = getResources().getConfiguration().orientation;
//
//            if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//            } else if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//            }
//        });