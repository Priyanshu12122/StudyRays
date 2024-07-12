package com.xerox.studyrays.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.db.exampleDb.Example
import com.xerox.studyrays.utils.toReadableDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AkWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ApiRepository
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            repository.getAndCacheAllAk()
            Result.success()
        } catch (e: Exception){
            Result.retry()
        }
    }

}