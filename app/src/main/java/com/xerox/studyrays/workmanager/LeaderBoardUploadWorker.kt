package com.xerox.studyrays.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.data.LeaderBoardRepository
import com.xerox.studyrays.db.exampleDb.Example
import com.xerox.studyrays.utils.toReadableDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LeaderBoardUploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: LeaderBoardRepository
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
//            repository.getAndCacheAllPw()
            Result.success()
        } catch (e: Exception){
            Result.retry()
        }
    }

}