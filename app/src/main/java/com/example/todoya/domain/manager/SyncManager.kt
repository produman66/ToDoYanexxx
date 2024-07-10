package com.example.todoya.domain.manager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


/**
 * Manager class responsible for scheduling periodic synchronization work with the server.
 * This class utilizes WorkManager to schedule periodic sync tasks at different intervals.
 */

class SyncManager (private val context: Context) {

    fun scheduleSyncWork() {
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