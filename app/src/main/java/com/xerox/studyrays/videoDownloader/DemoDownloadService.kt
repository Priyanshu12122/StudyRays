package com.xerox.studyrays.videoDownloader

import android.app.Notification
import android.os.Environment
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.xerox.studyrays.R
import com.xerox.studyrays.utils.Constants.DOWNLOAD_NOTIFICATION_CHANNEL_ID
import com.xerox.studyrays.utils.Constants.FOREGROUND_NOTIFICATION_ID
import java.io.File
import java.util.concurrent.Executor




class DemoDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
) {

    private val downloadDirectory = File(
        Environment.getExternalStorageDirectory().toString() + "/Download/Videos"
    )
    private val JOB_ID = 1

    val requirements = DownloadManager.DEFAULT_REQUIREMENTS

    override fun getDownloadManager(): DownloadManager {
// Note: This should be a singleton in your app.
        val databaseProvider = StandaloneDatabaseProvider(this)

// A download cache should not evict media, so should use a NoopCacheEvictor.
        val downloadCache = SimpleCache(downloadDirectory, NoOpCacheEvictor(), databaseProvider)

// Create a factory for reading the data from the network.
        val dataSourceFactory = DefaultHttpDataSource.Factory()

// Choose an executor for downloading data. Using Runnable::run will cause each download task to
// download data on its own thread. Passing an executor that uses multiple threads will speed up
// download tasks that can be split into smaller parts for parallel execution. Applications that
// already have an executor for background downloads may wish to reuse their existing executor.
        val downloadExecutor = Executor(Runnable::run)

// Create the download manager.
        val downloadManager =
            DownloadManager(
                this,
                databaseProvider,
                downloadCache,
                dataSourceFactory,
                downloadExecutor
            )

// Optionally, properties can be assigned to configure the download manager.
        downloadManager.requirements = requirements
        downloadManager.maxParallelDownloads = 3

        return downloadManager
    }

    override fun getScheduler(): Scheduler? {
        return if (Util.SDK_INT >= 21) PlatformScheduler(this, JOB_ID) else null
    }

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int,
    ): Notification {
        return DownloadNotificationHelper(this, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .buildProgressNotification(
                this,
                R.drawable.download,
                null,
                null,
                downloads,
                notMetRequirements
            )

    }
}
