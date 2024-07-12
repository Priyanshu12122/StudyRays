package com.xerox.studyrays.ui.studyfocus.sessionScreen

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.xerox.studyrays.MainActivity
import com.xerox.studyrays.utils.StudyFocusConstants.CLICK_REQUEST_CODE

object ServiceHelper {

    fun clickPendingIntent(context: Context): PendingIntent {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "study_rays://dashboard/session".toUri(),
            context,
            MainActivity::class.java
        )
        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(
                CLICK_REQUEST_CODE,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, StudySessionTimerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}