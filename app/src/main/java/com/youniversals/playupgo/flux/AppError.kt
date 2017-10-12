package com.youniversals.playupgo.flux

/**
 * @author Gian Darren Aquino
 * *
 * @createdAt 01/09/2016
 */
class AppError(
        val statusCode: Int,
        val errorCode: Int,
        val errorMessage: String,
        val isNetwork: Boolean
) {
    companion object {
        fun createNetwork(errorMessage: String) = AppError(-1, -1, errorMessage, true)
        fun createHttp(errorMessage: String) = AppError(-1, -1, errorMessage, false)
        fun createHttp(statusCode: Int, errorCode: Int, errorMessage: String) = AppError(statusCode, errorCode, errorMessage, false)
    }
}