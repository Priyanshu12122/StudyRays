package com.xerox.studyrays

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.xerox.studyrays.data.internetAvailability.NetworkManager
import com.xerox.studyrays.navigation.BottomBarNavigationScreen
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.studyfocus.sessionScreen.StudySessionTimerService
import com.xerox.studyrays.ui.theme.StudyRaysTheme
import com.xerox.studyrays.utils.VpnActiveScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val networkManager by lazy { NetworkManager(this) }
    private val viewModel by viewModels<MainViewModel>()
    private var isBound by mutableStateOf(false)
    private lateinit var timerService: StudySessionTimerService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as StudySessionTimerService.StudySessionTimerBinder
            timerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getStartDestination()
        installSplashScreen()
//            .apply {
//          this.setKeepOnScreenCondition {
//                viewModel.isLoading.value
//            }
//            this.setOnExitAnimationListener { screen ->
//                val zoomX = ObjectAnimator.ofFloat(
//                    screen.iconView,
//                    View.SCALE_X,
//                    0.4f,
//                    0.0f
//                )
//                zoomX.interpolator = OvershootInterpolator()
//                zoomX.duration = 500L
//                zoomX.doOnEnd { screen.remove() }
//
//                val zoomY = ObjectAnimator.ofFloat(
//                    screen.iconView,
//                    View.SCALE_Y,
//                    0.4f,
//                    0.0f
//                )
//                zoomY.interpolator = OvershootInterpolator()
//                zoomY.duration = 500L
//                zoomY.doOnEnd { screen.remove() }
//
//                zoomX.start()
//                zoomY.start()
//            }
//        }
        setContent {

            if (isBound) {

                StudyRaysTheme(dynamicColor = false, darkTheme = true) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = if (isSystemInDarkTheme()) Color.Black else MaterialTheme.colorScheme.background
                    ) {

                        val startDestination by viewModel.startDestination.collectAsState()
                        val isActive by networkManager.isVpnActive.collectAsState()
                        val navController = rememberNavController()

                        if (isActive) {
                            VpnActiveScreen(isActive = isActive) {
                                networkManager.checkVpnState()
                            }
                        } else {


//                            LazyColumn {
//                                items(10) {
//                                    LoadingCourseCardInClass()
//                                }
//                            }

//                            FilterBatchUI(
//                                onCLick = {batchIdd, slug, classValuee, name ->
//
//                                }
//                            )

                            BottomBarNavigationScreen(
                                startDestination = startDestination,
                                navController1 = navController,
                                timerService = timerService
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    override fun onResume() {
        super.onResume()
        networkManager.checkVpnState()
        viewModel.getStartDestination()
    }

    override fun onStart() {
        super.onStart()
        networkManager.checkVpnState()
        Intent(this, StudySessionTimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

    }

    override fun onPause() {
        super.onPause()
        networkManager.checkVpnState()
        viewModel.getStartDestination()
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


