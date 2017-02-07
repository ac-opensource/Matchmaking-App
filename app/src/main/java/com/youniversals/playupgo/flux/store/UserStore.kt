package com.youniversals.playupgo.flux.store

import com.youniversals.playupgo.data.UserIdentity
import com.youniversals.playupgo.flux.Action
import com.youniversals.playupgo.flux.AppError
import com.youniversals.playupgo.flux.Store
import com.youniversals.playupgo.flux.action.UserActionCreator
import com.youniversals.playupgo.flux.action.UserActionCreator.Companion.ACTION_INITIALIZE_USER
import com.youniversals.playupgo.flux.action.UserActionCreator.Companion.ACTION_LOGIN_SUCCESS
import rx.Observable

/**
 * @author Gian Darren Aquino
 * *
 * @createdAt 17/08/2016
 */
class UserStore : Store<UserStore>() {

    private var mUser: UserIdentity? = null
    private var mError: AppError? = null
    @UserActionCreator.UserAction
    private var mAction = Action.ACTION_NO_ACTION

    fun user(): UserIdentity? {
        return mUser
    }

    fun error(): AppError? {
        return mError
    }

    @UserActionCreator.UserAction
    fun action(): String {
        return mAction
    }

    fun observableWithFilter(action: String): Observable<UserStore> {
        return observable().filter { action == it.action() }
    }

    override fun onReceiveAction(action: Action) {
        when (action.type) {
            ACTION_INITIALIZE_USER, ACTION_LOGIN_SUCCESS -> {
                updateState()
                updateUser(action)
                updateError(action)
                updateAction(action)
                notifyStoreChanged(this)
            }
        }
    }

    private fun updateState() {
        mError = null
    }

    private fun updateUser(action: Action) {
        if (action.data != null && action.data is UserIdentity) {
            mUser = action.data as UserIdentity?
        }
    }

    private fun updateError(action: Action) {
        if (action.data != null && action.data is AppError) {
            mError = action.data as AppError?
        }
    }

    private fun updateAction(action: Action) {
        mAction = action.type
    }
}
