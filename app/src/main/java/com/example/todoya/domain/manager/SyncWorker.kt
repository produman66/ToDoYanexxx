package com.example.todoya.domain.manager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoya.TodoApplication
import com.example.todoya.domain.model.RepositoryException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


/**
 * Worker class responsible for synchronizing local Todo data with the server.
 *
 * This class executes background synchronization tasks using WorkManager.
 */
class SyncWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {
        return try {
            val repository = (applicationContext as TodoApplication).repository
            CoroutineScope(Dispatchers.IO).launch {
                repository.syncWithServer()
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        } catch (e: IOException) {
            throw RepositoryException(6, "Network error while fetching server revision")
        }
    }

}