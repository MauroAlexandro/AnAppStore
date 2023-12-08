package com.mauroalexandro.anappstore.utils

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FetchDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val utils = Utils.getInstance()
        val endPoint = inputData.getString(utils.WM_KEY)
        val outputData = Data.Builder()
            .putString(utils.WM_KEY, endPoint)
            .build()

        utils.createSimpleNotificationTest(applicationContext,endPoint)
        return Result.success(outputData)
    }
}