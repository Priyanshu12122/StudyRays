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
class BootCompletedReceiver: BroadcastReceiver() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    @Inject
    lateinit var repository: ApiRepository
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            scope.launch {
                val alarmItem = repository.getAlarmById(1)
                if (alarmItem != null) {
                    repository.alarmScheduler.schedule(alarmItem.timeToTriggerAt)
                }
            }
        }
    }
}