package com.xerox.studyrays.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.db.exampleDb.Example
import com.xerox.studyrays.utils.toReadableDate
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
            repository.insertExample(Example(msg = "Fetching work completed now from Fetch data worker",time = System.currentTimeMillis().toReadableDate()))
            repository.fetchAndCachePromoItems()
            repository.insertExample(Example(msg = "Fetching work for promo item completed now from Fetch data worker",time = System.currentTimeMillis().toReadableDate()))
            Result.success()
        } catch (e: Exception){
            repository.insertExample(Example(msg = "Fetching work failed now from Fetch data worker, reason = ${e.localizedMessage}",time = System.currentTimeMillis().toReadableDate()))
            Log.d("WorkStatus", "doWork: error = ${e.localizedMessage}")
            Result.retry()
        }
    }

}