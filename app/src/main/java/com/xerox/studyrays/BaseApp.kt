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
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.db.exampleDb.Example
import com.xerox.studyrays.utils.Constants
import com.xerox.studyrays.utils.toReadableDate
import com.xerox.studyrays.workmanager.FetchDataWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
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
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val fetchWorkRequest = PeriodicWorkRequestBuilder<FetchDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                15,
                TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "FetchDataWork",
            ExistingPeriodicWorkPolicy.KEEP,
            fetchWorkRequest
        )

        observeWorkStatus(fetchWorkRequest.id)

    }

    private fun observeWorkStatus(workId: UUID) {

        val scope = CoroutineScope(Dispatchers.IO)

        val workManager = WorkManager.getInstance(this)
        val workInfoLiveData = workManager.getWorkInfoByIdLiveData(workId)

        workInfoLiveData.observeForever { workInfo ->
            if (workInfo != null) {
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> {
                        scope.launch {
                            repository.insertExample(
                                Example(
                                    msg = "Fetching work enqueued now from Base app",
                                    time = System.currentTimeMillis().toReadableDate()
                                )
                            )

                        }
                        // Work is enqueued
                        Log.d("WorkStatus", "Work is enqueued")
                    }

                    WorkInfo.State.RUNNING -> {
                        scope.launch {
                            repository.insertExample(
                                Example(
                                    msg = "Fetching work running now from Base app",
                                    time = System.currentTimeMillis().toReadableDate()
                                )
                            )

                        }
                        // Work is running
                        Log.d("WorkStatus", "Work is running")
                    }

                    WorkInfo.State.SUCCEEDED -> {
                        scope.launch {
                            repository.insertExample(
                                Example(
                                    msg = "Fetching work succeeded now from Base app",
                                    time = System.currentTimeMillis().toReadableDate()
                                )
                            )

                        }
                        // Work succeeded
                        Log.d("WorkStatus", "Work succeeded")
                    }

                    WorkInfo.State.FAILED -> {
                        scope.launch {
                            repository.insertExample(
                                Example(
                                    msg = "Fetching work failed now from Base app",
                                    time = System.currentTimeMillis().toReadableDate()
                                )
                            )

                        }
                        // Work failed
                        Log.d("WorkStatus", "Work failed")
                    }

                    WorkInfo.State.CANCELLED -> {
                        scope.launch {
                            repository.insertExample(
                                Example(
                                    msg = "Fetching work cancelled now from Base app",
                                    time = System.currentTimeMillis().toReadableDate()
                                )
                            )

                        }
                        // Work cancelled
                        Log.d("WorkStatus", "Work cancelled")
                    }

                    WorkInfo.State.BLOCKED -> {
                        scope.launch {
                            repository.insertExample(
                                Example(
                                    msg = "Fetching work Blocked now from Base app",
                                    time = System.currentTimeMillis().toReadableDate()
                                )
                            )

                        }
                        // Work blocked
                        Log.d("WorkStatus", "Work blocked")
                    }
                }
            }
        }
    }


    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
}


