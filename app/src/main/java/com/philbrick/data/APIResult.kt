package com.philbrick.data
import com.philbrick.data.enum.APIErrorCode


sealed class APIResult<out T : Any> {
    data class Success<out T : Any>(val data: T, val message: String?) : APIResult<T>()
    data class Failure(val code: APIErrorCode, val message: String? = null) : APIResult<Nothing>()
    object InProgress : APIResult<Nothing>()
}
