package com.example.todoya.syncWork

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Manager class responsible for scheduling periodic synchronization work with the server.
 * This class utilizes WorkManager to schedule periodic sync tasks at different intervals.
 */

class SyncManagerImpl @Inject constructor(@ApplicationContext private val context: Context) : SyncManager  {

    override fun scheduleSyncWork() {
        val syncWorkRequest15Min = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .build()

        val syncWorkRequest8Hours = PeriodicWorkRequestBuilder<SyncWorker>(8, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "SyncWork",
                ExistingPeriodicWorkPolicy.UPDATE,
                syncWorkRequest15Min
            )

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "SyncWork8Hours",
                ExistingPeriodicWorkPolicy.UPDATE,
                syncWorkRequest8Hours
            )
    }
}