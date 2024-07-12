package com.example.todoya.domain.model


/**
 * Generic request class holding an element of type T.
 */
data class Request<T> (
    val element: T?
)