package com.xerox.studyrays

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.utils.Constants
import com.xerox.studyrays.workmanager.FetchDataWorker
import com.xerox.studyrays.workmanager.LeaderBoardUploadWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
internal class BaseApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var repository: ApiRepository

    override fun onCreate() {
        super.onCreate()

        OneSignal.Debug.logLevel = LogLevel.VERBOSE
        OneSignal.initWithContext(this, Constants.ONESIGNAL_APP_ID)
        CoroutineScope(Dispatchers.Main).launch {
            OneSignal.Notifications.requestPermission(true)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val fetchWorkRequest = PeriodicWorkRequestBuilder<FetchDataWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                15,
                TimeUnit.MINUTES
            )
            .build()


//        val leaderBoardWorker =
//            PeriodicWorkRequestBuilder<LeaderBoardUploadWorker>(1, TimeUnit.HOURS)
//                .setConstraints(constraints)
//                .setBackoffCriteria(
//                    backoffPolicy = BackoffPolicy.LINEAR,
//                    15,
//                    TimeUnit.MINUTES
//                )
//                .build()


        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "FetchDataWork",
            ExistingPeriodicWorkPolicy.KEEP,
            fetchWorkRequest
        )

//        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
//            "Leaderboard work",
//            ExistingPeriodicWorkPolicy.KEEP,
//            leaderBoardWorker
//        )

    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
}