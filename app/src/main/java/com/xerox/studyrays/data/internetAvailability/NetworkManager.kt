package com.xerox.studyrays.data.internetAvailability

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NetworkManager(private val context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isVpnActive: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isVpnActive = _isVpnActive.asStateFlow()


    init {
        checkVpnState()
        observeNetworkState()
    }

    fun checkVpnState() {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val vpnActive = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: false
        _isVpnActive.value = vpnActive
    }

//    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
//    val isConnected = _isConnected.asStateFlow()
//
//
//    suspend fun observeInternetAccessibility() {
//            observeNetworkState().collect(_isConnected)
//    }

    fun observeNetworkState(): Flow<Boolean> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(element = true) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(element = true) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(element = false) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(element = false) }
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()

    }

}
