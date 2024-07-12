package com.xerox.studyrays.ui.screens.videoPlayerScreen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.db.alarmDb.AlarmEntity
import com.xerox.studyrays.db.downloadsDb.DownloadNumberEntity
import com.xerox.studyrays.db.taskDb.TaskEntity
import com.xerox.studyrays.db.videoNotesDb.VideoNoteEntity
import com.xerox.studyrays.db.videoProgress.VideoProgressEntity
import com.xerox.studyrays.network.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.NetworkInterface
import java.util.Collections
import javax.inject.Inject


@HiltViewModel
class VideoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: ApiRepository,
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

    fun changeFullScreenState(isFullScreen: Boolean) {
        _state.value = _state.value.copy(
            isFullScreen = isFullScreen
        )
    }

    fun changeVisibleState(isVisible: Boolean) {
        _state.value = _state.value.copy(
            isVisible = isVisible
        )
    }

    fun seekTo(time: Long){
        playerr?.seekTo(time)
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


    fun initialisePlayer(context: Context, url: String, videoId: String, position: Long) {
        playerr?.release()
        playerr = ExoPlayer.Builder(context)
            .setTrackSelector(_state.value.trackSelector)
            .build().apply {

                val mediaItem = MediaItem.Builder()
                    .setUri(Uri.parse(url))
                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                    .build()

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


            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                val duration = player.duration
                val positionn = player.currentPosition
                val progress = positionn.toFloat() / duration
                viewModelScope.launch {
                    repository.upsertVideoProgress(
                        VideoProgressEntity(
                            videoId, progress, positionn
                        )
                    )
                }
            }

        })
    }

    fun initialiseDrmPlayer(
        context: Context,
        url: String,
        licenseUrl: String,
        videoId: String,
        position: Long,
    ) {
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

            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                val duration = player.duration
                val positionn = player.currentPosition
                val progress = positionn.toFloat() / duration

                viewModelScope.launch {
                    repository.upsertVideoProgress(
                        VideoProgressEntity(
                            videoId, progress, position = positionn
                        )
                    )
                }

            }

        })
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }


//    fun playerViewBuilder(ctx: Context): StyledPlayerView {
//        _state.value.playerView = StyledPlayerView(ctx).apply {
//            player = playerr
//            layoutParams = FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//
//            setShowNextButton(false)
//            setShowPreviousButton(false)
//            setShowBuffering(StyledPlayerView.SHOW_BUFFERING_WHEN_PLAYING)
//            keepScreenOn = true
//            setControllerOnFullScreenModeChangedListener {
//                _state.value = _state.value.copy(isFullScreen = it)
//                with(ctx) {
//                    if (it) {
//                        setScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
//                    } else {
//                        setScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                    }
//                }
//            }
//
//            setControllerVisibilityListener { visibility ->
//                _state.value = _state.value.copy(isVisible = visibility == View.VISIBLE)
//            }
//
//            setOnClickListener {
//
//            }
//        }
//        return _state.value.playerView
//    }


    fun playerViewBuilder(ctx: Context, position: Long): StyledPlayerView {
        _state.value.playerView = StyledPlayerView(ctx).apply {
            this.useController = false
            this.player = playerr
            playerr!!.seekTo(position)
        }
        return _state.value.playerView
    }

    fun play() {
        playerr?.play()
    }

    fun pause() {
        playerr?.pause()
    }

    fun getPlayer(): ExoPlayer? {
        return playerr
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


    fun insertTask(item: TaskEntity) {
        viewModelScope.launch {
            repository.insertTask(item)
        }
    }

    fun deleteTask(item: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(item)
        }
    }

    suspend fun getTask(id: Int): TaskEntity? {
        return repository.getTask(id)
    }

    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.uppercase() else sAddr.substring(
                                    0,
                                    delim
                                ).uppercase()
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }


    fun insertDownloadNumber(item: DownloadNumberEntity) {
        viewModelScope.launch {
            repository.insertDownloadNumber(item)
        }
    }

    fun deleteDownloadNumber(item: DownloadNumberEntity) {
        viewModelScope.launch {
            repository.deleteDownloadNumber(item)
        }
    }

    suspend fun getDownloadNumberById(id: Int): DownloadNumberEntity? {
        return repository.getDownloadNumberById(id)
    }


    fun insertAlarm(item: AlarmEntity) {
        viewModelScope.launch {
            repository.insertAlarm(item)
        }
    }

    suspend fun getAlarmById(id: Int): AlarmEntity? {
        return repository.getAlarmById(id)
    }

    fun deleteAlarmItem(item: AlarmEntity) {
        viewModelScope.launch {
            repository.deleteAlarmItem(item)
        }
    }

    suspend fun getVideoProgressById(videoId: String): VideoProgressEntity? {
        return repository.getVideoProgressById(videoId)
    }


    private val _videoNotes: MutableStateFlow<Response<List<VideoNoteEntity>?>> =
        MutableStateFlow(Response.Loading())
    val videoNotes = _videoNotes.asStateFlow()

    fun getVideoNotesById(videoId: String) {
        viewModelScope.launch {
            _videoNotes.value = Response.Loading()
            try {
                repository.getVideoNoteById(videoId).collect {
                    _videoNotes.value = Response.Success(it)
                }

            } catch (e: Exception) {
                _videoNotes.value = Response.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }

    fun insertVideoNote(item: VideoNoteEntity) {
        viewModelScope.launch {
            repository.insertVideoNotes(item)
        }
    }

    fun deleteVideoNote(id: Int) {
        viewModelScope.launch {
            repository.deleteVideoNote(id)
        }
    }

}
