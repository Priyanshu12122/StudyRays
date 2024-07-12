package com.xerox.studyrays.alarmManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.xerox.studyrays.db.alarmDb.AlarmEntity
import java.time.ZoneId

class AlarmScheduler(
    private val context: Context
) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

     fun schedule(triggerTime: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            PendingIntent.getBroadcast(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

     fun cancel() {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                1,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}