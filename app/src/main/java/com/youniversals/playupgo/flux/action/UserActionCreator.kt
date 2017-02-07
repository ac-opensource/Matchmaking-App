package com.youniversals.playupgo.flux.action

import android.support.annotation.StringDef
import com.youniversals.playupgo.flux.Action
import com.youniversals.playupgo.flux.Dispatcher
import com.youniversals.playupgo.flux.Utils
import com.youniversals.playupgo.flux.model.UserModel

/**
 * @author Gian Darren Aquino
 * *
 * @createdAt 31/08/2016
 */
class UserActionCreator(private val mDispatcher: Dispatcher, private val mUserModel: UserModel, private val mUtils: Utils) {

    companion object {
        const val ACTION_INITIALIZE_USER = "ACTION_INITIALIZE_USER"
        const val ACTION_LOGIN_SUCCESS = "ACTION_LOGIN_SUCCESS"
        const val ACTION_LOGIN_FAILED = "ACTION_LOGIN_FAILED"
    }
    @StringDef(value = *arrayOf(ACTION_INITIALIZE_USER))
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class UserAction

    init {
        preload()
    }

    fun preload() {
        mDispatcher.dispatch(Action(ACTION_INITIALIZE_USER, Unit))
    }

    fun login(accessToken: String) {
        mUserModel.login(accessToken)
                .subscribe({ user -> mDispatcher.dispatch(Action(ACTION_LOGIN_SUCCESS, user)) }, { throwable ->
                    mDispatcher.dispatch(
                            Action(ACTION_LOGIN_FAILED, mUtils.getError(throwable)))
                })

    }
}
