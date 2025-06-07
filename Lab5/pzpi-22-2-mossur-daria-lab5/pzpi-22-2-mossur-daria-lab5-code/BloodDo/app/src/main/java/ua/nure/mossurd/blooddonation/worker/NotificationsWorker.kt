package ua.nure.mossurd.blooddonation.worker

import android.Manifest
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.mossurd.blooddonation.R
import ua.nure.mossurd.blooddonation.dto.NotificationDto
import ua.nure.mossurd.blooddonation.misc.Constants
import ua.nure.mossurd.blooddonation.utils.NotificationsUtils
import java.util.Optional

class NotificationsWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val token = applicationContext.getSharedPreferences(
            Constants.BLOODDO_CONTEXT_PREFS,
            MODE_PRIVATE
        )
            ?.getString(Constants.BLOODDO_TOKEN_NAME, "")
            .orEmpty()

        val language = Optional.ofNullable(
            applicationContext.getSharedPreferences(
                Constants.BLOODDO_CONTEXT_PREFS,
                MODE_PRIVATE
            )
                ?.getString(Constants.BLOODDO_LANGUAGE_NAME, "uk")
        )
            .orElse("uk")

        if (token != "") {
            Constants.API_SERVICE.getNewNotifications(token)
                .enqueue(object : Callback<List<NotificationDto>> {
                    override fun onResponse(
                        call: Call<List<NotificationDto>?>,
                        response: Response<List<NotificationDto>?>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.forEach { it ->
                                val header = NotificationsUtils.getNotificationText(
                                    it.header,
                                    applicationContext.getString(R.string.notification_empty_header),
                                    language,
                                    applicationContext
                                )
                                val body = NotificationsUtils.getNotificationText(
                                    it.body,
                                    applicationContext.getString(R.string.notification_empty_header),
                                    language,
                                    applicationContext
                                )
                                val nBuilder = NotificationCompat.Builder(applicationContext, Constants.DEFAULT_NOTIFICATION_CHANNEL)
                                    .setContentTitle(header)
                                    .setContentText(body)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .build()
                                val nManager = NotificationManagerCompat.from(applicationContext)

                                try {
                                    nManager.notify(it.id.toInt(), nBuilder)
                                } catch (e: SecurityException) {
                                    Log.e(
                                        this.javaClass.name,
                                        "Failed to send notification; probably due to lacking permission; message: ${e.message}"
                                    )
                                }
                            }
                        } else {
                            Log.e(
                                this.javaClass.name,
                                "Get new notifications request failure. Code: ${response.code()}; message: ${response.message()}"
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<List<NotificationDto>?>,
                        t: Throwable
                    ) {
                        Log.e(
                            this.javaClass.name,
                            "Get new notifications request failure. Message: ${t.message}"
                        )
                    }
                })
        }
        return Result.success()
    }
}