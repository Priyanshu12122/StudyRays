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

//@HiltWorker
//class TaskWorker @AssistedInject constructor(
//    @Assisted context: Context,
//    @Assisted workerParams: WorkerParameters,
//    private val repository: ApiRepository
//): CoroutineWorker(context, workerParams) {
//    override suspend fun doWork(): Result {
//        return try {
//            val task = repository.getTask(1)
//            if(task == null){
//                repository.insertExample(Example(msg = "TAsk is null", time = System.currentTimeMillis().toReadableDate()))
//                Result.success()
//            } else {
//                val expirationTime = System.currentTimeMillis() - 10 * 60 * 1000
//                if (task.timeStamp < expirationTime){
//                    repository.insertExample(Example(msg = "deleted task $task", time = System.currentTimeMillis().toReadableDate()))
//                    repository.deleteTask(task)
//                    Result.success()
//                } else {
//                    repository.insertExample(Example(msg = "Task has'nt reached expiration time", time = System.currentTimeMillis().toReadableDate()))
//                    Result.success()
//                }
//            }
//        } catch (e: Exception){
//            Result.retry()
//        }
//    }
//
//}