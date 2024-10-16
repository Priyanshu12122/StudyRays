package com.xerox.studyrays.ui.screens.ak

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.google.gson.Gson
import com.xerox.studyrays.data.AkRepository
import com.xerox.studyrays.model.akModel.aKVideoU.AkUrl
import com.xerox.studyrays.model.akModel.akIndex.Index
import com.xerox.studyrays.model.akModel.akLessons.AkLesson
import com.xerox.studyrays.model.akModel.akNotes.AkNotes
import com.xerox.studyrays.model.akModel.akSubjects.AkSubject
import com.xerox.studyrays.model.akModel.akVideo.AkVideo
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.screens.ak.akVideoPlayer.AkVideoState
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoPlayerListener
import com.xerox.studyrays.ui.screens.videoPlayerScreen.generateQualityList
import com.xerox.studyrays.ui.screens.videoPlayerScreen.setScreenOrientation
import com.xerox.studyrays.utils.decrypt
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class AkViewModel @Inject constructor(
    private val repository: AkRepository,
    @ApplicationContext context: Context,
) : ViewModel() {


    private val key = MainViewModel.Keys.kkk()
    private val iv = MainViewModel.Keys.ivv()
    private val gson = Gson()

    private val noInternetMsg = "Internet Unavailable."
    private val socketErrorMsg = "Socket timeout: Either slow internet or server issue."
    private val refreshMsg = "Refreshed Successfully!"

    fun showSnackBar(snackBarHostState: SnackbarHostState) {
        viewModelScope.launch {
            snackBarHostState.showSnackbar(message = refreshMsg, withDismissAction = true)
        }
    }

    private val _index: MutableStateFlow<Response<Index>> = MutableStateFlow(Response.Loading())
    val index = _index.asStateFlow()

    var isRefreshing by mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getIndex(
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _index.value = Response.Loading()
                }
                val response = repository.getIndex()
                val decodedData: ByteArray = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: Index = gson.fromJson(decryptedData, Index::class.java)
                _index.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _index.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _index.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _index.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshIndex(onComplete: () -> Unit) {
        isRefreshing = true
        getIndex(
            isRefresh = true,
            onComplete = {
                isRefreshing = false
                onComplete()
            }
        )
    }


    private val _akSubject: MutableStateFlow<Response<AkSubject>> =
        MutableStateFlow(Response.Loading())
    val akSubject = _akSubject.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAkSubject(
        id: Int,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _akSubject.value = Response.Loading()
                }
                val response = repository.getAkSubjects(id)
                val decodedData: ByteArray = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: AkSubject =
                    gson.fromJson(decryptedData, AkSubject::class.java)
                _akSubject.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _akSubject.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _akSubject.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _akSubject.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAkSubjects(
        id: Int,
        onComplete: () -> Unit,
    ) {

        isRefreshing = true
        getAkSubject(id = id, isRefresh = true) {
            isRefreshing = false
            onComplete()
        }

    }


    private val _akLesson: MutableStateFlow<Response<AkLesson>> =
        MutableStateFlow(Response.Loading())
    val akLesson = _akLesson.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAkLesson(
        sid: Int,
        bid: Int,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _akLesson.value = Response.Loading()
                }
                val response = repository.getAkLessons(sid = sid, bid = bid)
                val decodedData: ByteArray = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: AkLesson =
                    gson.fromJson(decryptedData, AkLesson::class.java)
                _akLesson.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _akLesson.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _akLesson.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _akLesson.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAkLesson(
        bid: Int,
        sid: Int,
        onComplete: () -> Unit
    ) {
        isRefreshing = true
        getAkLesson(
            sid = sid,
            bid = bid,
            isRefresh = true
        ) {
            isRefreshing = false
            onComplete()
        }
    }


    private val _akVideo: MutableStateFlow<Response<AkVideo>> = MutableStateFlow(Response.Loading())
    val akVideo = _akVideo.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAkVideo(
        sid: Int,
        tid: Int,
        bid: Int,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _akVideo.value = Response.Loading()
                }
                val response = repository.getAkVideos(sid = sid, bid = bid, tid = tid)
                val decodedData: ByteArray = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: AkVideo =
                    gson.fromJson(decryptedData, AkVideo::class.java)
                _akVideo.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _akVideo.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _akVideo.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _akVideo.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAkVideo(
        bid: Int,
        sid: Int,
        tid: Int,
        onComplete: () -> Unit
    ) {
        isRefreshing = true
        getAkVideo(sid = sid, bid = bid, tid = tid, isRefresh = true) {
            isRefreshing = false
            onComplete()
        }
    }

    private val _akNotes: MutableStateFlow<Response<AkNotes>> = MutableStateFlow(Response.Loading())
    val akNotes = _akNotes.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAkNotes(
        sid: Int, tid: Int, bid: Int, isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _akNotes.value = Response.Loading()
                }
                val response = repository.getAkNotes(sid = sid, bid = bid, tid = tid)
                val decodedData: ByteArray = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: AkNotes =
                    gson.fromJson(decryptedData, AkNotes::class.java)
                _akNotes.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _akNotes.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _akNotes.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _akNotes.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAkNote(
        bid: Int,
        sid: Int,
        tid: Int,
        onComplete: () -> Unit
    ) {
        isRefreshing = true
        getAkNotes(sid = sid, bid = bid, tid = tid, isRefresh = true) {
            isRefreshing = false
            onComplete()
        }
    }

    private val _akVideoU: MutableStateFlow<Response<AkUrl>> = MutableStateFlow(Response.Loading())
    val akVideoU = _akVideoU.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAkVideoU(sid: Int, tid: Int, bid: Int, key: Int, lid: String) {
        viewModelScope.launch {
            try {
                _akVideoU.value = Response.Loading()
                val response =
                    repository.getAkVideoU(sid = sid, bid = bid, tid = tid, key = key, lid = lid)
                _akVideoU.value = Response.Success(response)
            } catch (e: SocketTimeoutException) {
                _akVideoU.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _akVideoU.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _akVideoU.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }


//    Video player

    private var playerr: ExoPlayer? = null

    private val _state: MutableStateFlow<AkVideoState> = MutableStateFlow(
        AkVideoState(
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


    fun initialisePlayer(context: Context, url: String) {
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
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
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


    override fun onCleared() {
        super.onCleared()
        playerr?.release()
    }
}