package com.test.lmaxtest.data

import io.ktor.client.features.*

sealed class Resource<out R>{
    data class Success<out R>(val result: R): Resource<R>()
    data class Failure(val exception: Exception): Resource<Nothing>()
    data class Unauthorised(val exception: ResponseException): Resource<Nothing>()
    object Loading: Resource<Nothing>()
}
