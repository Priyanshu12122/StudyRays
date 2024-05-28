package com.xerox.studyrays

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.xerox.studyrays.data.internetAvailability.NetworkManager
import com.xerox.studyrays.navigation.BottomBarNavigationScreen
import com.xerox.studyrays.ui.theme.StudyRaysTheme
import com.xerox.studyrays.utils.VpnActiveScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val networkManager by lazy { NetworkManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            StudyRaysTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (isSystemInDarkTheme()) Color.Black else MaterialTheme.colorScheme.background
                ) {

                    val isActive by networkManager.isVpnActive.collectAsState()

                    if (isActive) {
                        VpnActiveScreen(isActive = isActive) {
                            networkManager.checkVpnState()
                        }
                    } else {
                        BottomBarNavigationScreen()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        networkManager.checkVpnState()
    }

    override fun onStart() {
        super.onStart()
        networkManager.checkVpnState()


    }

    override fun onPause() {
        super.onPause()
        networkManager.checkVpnState()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            showSystemUI()
        }
    }

    private fun showSystemUI() {
        actionBar?.show()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        } else {
            window.insetsController?.apply {
                show(WindowInsets.Type.systemBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_DEFAULT
            }
        }
    }


    private fun hideSystemUI() {
        //Hides the ugly action bar at the top
        actionBar?.hide()
        //Hide the status bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).hide(WindowInsetsCompat.Type.systemBars())
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.apply {
                systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        } else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.systemBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}


