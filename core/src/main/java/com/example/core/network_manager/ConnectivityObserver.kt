package com.example.core.network_manager

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    enum class Status{
        Available,
        Unavailable,
        Losing,
        Lost
    }
}