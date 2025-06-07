package ua.nure.mossurd.blooddonation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.edit
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ua.nure.mossurd.blooddonation.misc.Constants
import ua.nure.mossurd.blooddonation.worker.NotificationsWorker

class BloodDoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        applicationContext.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_DONOR_DATA, null) }
        applicationContext.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_MEDIC_DATA, null) }
        applicationContext.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_TOKEN_NAME, null) }
        applicationContext.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_USER_NAME, null) }
        applicationContext.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_ROLES_NAME, null) }
        applicationContext.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_PI_AGREED, null) }
        applicationContext.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_SYSTEM_LANGUAGE, null) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.default_notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(Constants.DEFAULT_NOTIFICATION_CHANNEL, name, importance)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        val notificationRequest =
            PeriodicWorkRequestBuilder<NotificationsWorker>(Constants.NOTIFICATIONS_PING_TIME)
                .build()
        WorkManager.getInstance(applicationContext).enqueue(notificationRequest)
    }
}