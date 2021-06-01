package com.dbtechprojects.cloudstatustest.util

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dbtechprojects.cloudstatustest.R
import com.dbtechprojects.cloudstatustest.repository.MainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

private const val NOTIFICATION_ID = 2

@HiltWorker
class AwsWorker
@AssistedInject
constructor(@Assisted context: Context, @Assisted workerParams: WorkerParameters, private val repository: MainRepository) :
    CoroutineWorker(context, workerParams) {

    private val preferences = (applicationContext as BaseApplication).preferences

    override suspend fun doWork() = withContext(IO) {

        val shouldShowNotification = preferences.getBoolean(applicationContext.getString(R.string.aws_notif_pref_key), true)

        val dao = repository.getCacheDatabaseDao()
        val oldList = dao.getAwsEvents()
        repository.fetchFromAwsApi()
        val newList = dao.getAwsEvents()

        if (newList != oldList && shouldShowNotification) {
            createNotification()
        }

        Result.success()
    }

    private fun createNotification() {
        val notification = NotificationCompat
            .Builder(applicationContext, Constants.AWS_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_new_releases_24)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_text, "AWS"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
    }

}
