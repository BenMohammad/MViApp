package com.fukuni.mviapp.util

import android.util.Log
import retrofit2.Response

sealed class GenericApiResponse<T> {

    companion object {
        private val TAG : String = "AppDebug"


        fun <T> create(error: Throwable) : ApiErrorResponse<T> {
            return ApiErrorResponse(error.message?: "unknown error")
        }

        fun <T> create(response : Response<T>): GenericApiResponse<T> {
            Log.d(TAG, "GenericApiResponse: response: $response")
            Log.d(TAG, "GenericApiResponse: raw: ${response.raw()}")
            Log.d(TAG, "GenericApiResponse: headers: ${response.headers()}")
            Log.d(TAG, "GenericApiResponse: message: ${response.message()}")

            if(response.isSuccessful) {
                val body = response.body()
                if(body == null || response.code() == 204) {
                    return ApiEmptyResponse()
                }
                else if (response.code() == 401) {
                    return ApiErrorResponse("401 unauthorized, Token may be invalid")
                } else {
                    return ApiSuccessResponse(body = body)
                }
            }

            else {
                val msg = response.errorBody()?.string()
                val errorMsg = if(msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                return ApiErrorResponse(errorMsg?: "Unknown error")

            }
        }
    }




}
class ApiEmptyResponse<T> : GenericApiResponse<T>()
data class ApiSuccessResponse<T>(val body: T) : GenericApiResponse<T>()
data class ApiErrorResponse<T>(val errorMessage: String) : GenericApiResponse<T>()