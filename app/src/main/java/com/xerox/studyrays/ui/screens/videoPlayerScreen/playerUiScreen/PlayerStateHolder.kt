package com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable

class PlayerStateHolder(savedStateHandle: SavedStateHandle) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var playerState: PlayerState by savedStateHandle.saveable {
        mutableStateOf(PlayerState())
    }

    fun onPlayerStateChange(newPlayerState: PlayerState) {
        playerState = newPlayerState
    }
}