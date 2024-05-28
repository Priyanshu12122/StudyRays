package com.xerox.studyrays.network

sealed class Response<T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error<T>(val msg: String) : Response<T>()
    data class Loading<T>(val data: T? = null) : Response<T>()
}


sealed class ResponseTwo<T> {
    data class Success<T>(val data: T) : ResponseTwo<T>()
    data class Error<T>(val msg: String) : ResponseTwo<T>()
    data class Loading<T>(val data: T? = null) : ResponseTwo<T>()
    data class Nothing<T>(val data: T? = null) : ResponseTwo<T>()
}

