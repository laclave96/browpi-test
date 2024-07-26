package com.bowpi.test.utils

sealed class _Result<T> {
    class Success<T>(val data: T): _Result<T>()

    class Error<T>(val message: Message): _Result<T>()
}
