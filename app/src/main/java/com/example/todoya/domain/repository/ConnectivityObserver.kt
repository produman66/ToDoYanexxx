package com.example.todoya.domain.repository

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