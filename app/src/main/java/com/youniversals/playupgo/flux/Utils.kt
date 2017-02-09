package com.youniversals.playupgo.flux

import android.content.Context
import android.util.Log
import com.google.firebase.crash.FirebaseCrash
import retrofit2.adapter.rxjava.HttpException

/**
 * @author Gian Darren Aquino
 * *
 * @createdAt 31/08/2016
 */
class Utils(context: Context) {

    companion object {
        val MSG_ERROR_DEFAULT = "Something went wrong."
        val MSG_ERROR_NETWORK = "No network connection."
    }

    fun getError(throwable: Throwable): AppError {
        FirebaseCrash.logcat(Log.ERROR, "FLUX ERROR", throwable.message)
        FirebaseCrash.report(throwable)
        if (throwable !is HttpException) {
            return AppError.createNetwork(MSG_ERROR_NETWORK)
        }
        val response = throwable.response() ?: return AppError.createHttp(MSG_ERROR_DEFAULT)
        return AppError.createHttp(response.code(), -1, MSG_ERROR_DEFAULT)
    }
}
