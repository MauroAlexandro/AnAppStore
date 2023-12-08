package com.mauroalexandro.anappstore.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.fragment.app.FragmentActivity
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mauroalexandro.anappstore.R
import com.mauroalexandro.anappstore.models.App
import com.mauroalexandro.anappstore.views.DetailsFragment
import java.util.concurrent.TimeUnit

class Utils {
    companion object {
        private var INSTANCE: Utils? = null
        fun getInstance(): Utils {
            if (INSTANCE == null)
                INSTANCE = Utils()

            return INSTANCE as Utils
        }
    }

    val WM_KEY = "WORK_MANAGER_KEY"

    /**
     * Show DetailsFragment
     */
    fun openDetailsFragment(activity: FragmentActivity, app: App, utils: Utils) {
        val detailsFragment = DetailsFragment(activity, app, utils)
        if(!activity.supportFragmentManager.isDestroyed)
            detailsFragment.show(
                activity.supportFragmentManager,
                "app_detail_dialog_fragment"
            )
    }

    /**
     * Show Alert Dialog that in demo mode Download Cannot be done
     */
    fun showAlertDialog(activity: FragmentActivity) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder
            .setTitle(activity.resources.getString(R.string.app_detail_download_dialog_title_text))
            .setMessage(activity.resources.getString(R.string.app_detail_download_dialog_message_text))
            .setPositiveButton(activity.resources.getString(R.string.app_detail_download_dialog_positive_button_text)) { _, _ ->
                //dismiss
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Work Manager to show every 30minutes a Notification
     */
    fun createWorkManager(context: Context) {
        val workManager = WorkManager.getInstance(context)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val data = Data.Builder()

        data.putString(WM_KEY, context.getString(R.string.app_workmanager_message))

        val work = PeriodicWorkRequestBuilder<FetchDataWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(data.build())
            .setInitialDelay(30,TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork("Some-Unique-Name",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            work)
    }

    /**
     * Creation of a Simple Notification
     */
    fun createSimpleNotificationTest(context: Context, endPoint: String?) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "my_channel_id_01"
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "My Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )

        // Configure the notification channel.
        notificationChannel.description = "Channel description"
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)
        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(Notification.DEFAULT_ALL)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(endPoint)
        notificationManager.notify( 99999, notificationBuilder.build())
    }
}