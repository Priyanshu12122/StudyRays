package com.xerox.studyrays.alarmManager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.db.exampleDb.Example
import com.xerox.studyrays.utils.toReadableDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: ApiRepository

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    override fun onReceive(context: Context?, intent: Intent?) {
        scope.launch {
            val item = repository.getTask(1)
            val downloadNumber = repository.getDownloadNumberById(1)
            val alarmItem = repository.getAlarmById(1)
            if (item != null ) {
                repository.deleteTask(item)
            }

            if (downloadNumber != null){
                repository.deleteDownloadNumber(downloadNumber)
            }

            if (alarmItem != null){
                repository.deleteAlarmItem(alarmItem)
            }
        }
    }
}