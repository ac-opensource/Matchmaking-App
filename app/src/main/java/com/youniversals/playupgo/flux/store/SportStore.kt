package com.youniversals.playupgo.flux.store

import com.youniversals.playupgo.data.Sport
import com.youniversals.playupgo.flux.Action
import com.youniversals.playupgo.flux.AppError
import com.youniversals.playupgo.flux.Store
import com.youniversals.playupgo.flux.action.SportActionCreator
import com.youniversals.playupgo.flux.action.SportActionCreator.Companion.ACTION_GET_SPORTS_S
import rx.Observable

/**
 * @author Gian Darren Aquino
 * *
 * @createdAt 17/08/2016
 */
class SportStore : Store<SportStore>() {

    var sports: List<Sport>? = null
        private set

    var error: AppError? = null
        private set

    @SportActionCreator.SportAction
    var action = Action.ACTION_NO_ACTION
        private set

    fun observableWithFilter(action: String): Observable<SportStore> {
        return observable().filter { action == it.action }
    }

    override fun onReceiveAction(action: Action) {
        when (action.type) {
            ACTION_GET_SPORTS_S -> {
                updateState()
                updateData(action)
                updateError(action)
                updateAction(action)
                notifyStoreChanged(this)
            }
        }
    }

    private fun updateState() {
        error = null
    }


    private fun updateData(action: Action) {
        sports = null
        action.data ?: return
        if (action.data is List<*> && action.data.size > 0) {
            if (action.data[0] is Sport) {
                sports = action.data as List<Sport>?
            }
        }
    }

    private fun updateError(action: Action) {
        if (action.data != null && action.data is AppError) {
            error = action.data as AppError?
        }
    }

    private fun updateAction(action: Action) {
        this.action = action.type
    }
}
