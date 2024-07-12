package com.example.feature.data.remote.model


/**
 * Generic request class holding an element of type T.
 */
data class Request<T> (
    val element: T?
)