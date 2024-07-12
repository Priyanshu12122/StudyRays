package com.xerox.studyrays.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.xerox.studyrays.data.ApiRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FetchDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ApiRepository
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            repository.fetchAndCacheTotalFee()
            repository.getNavItems()
            repository.fetchAndCachePromoItems()
            repository.cacheAllKhazana()
            Result.success()
        } catch (e: Exception){
            Result.retry()
        }
    }

}