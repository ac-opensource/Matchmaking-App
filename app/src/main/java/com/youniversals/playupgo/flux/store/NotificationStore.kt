package com.youniversals.playupgo.flux.store

import com.youniversals.playupgo.data.Notification
import com.youniversals.playupgo.flux.Action
import com.youniversals.playupgo.flux.AppError
import com.youniversals.playupgo.flux.Store
import com.youniversals.playupgo.flux.action.NotificationActionCreator
import com.youniversals.playupgo.flux.action.NotificationActionCreator.Companion.ACTION_GET_NOTIF_S
import rx.Observable

/**
 * @author Gian Darren Aquino
 * *
 * @createdAt 17/08/2016
 */
class NotificationStore : Store<NotificationStore>() {

    var notifications: List<Notification>? = null
        private set

    var error: AppError? = null
        private set

    @NotificationActionCreator.NotificationAction
    var action = Action.ACTION_NO_ACTION
        private set

    fun observableWithFilter(action: String): Observable<NotificationStore> {
        return observable().filter { action == it.action }
    }

    override fun onReceiveAction(action: Action) {
        when (action.type) {
            ACTION_GET_NOTIF_S -> {
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
        notifications = null
        action.data ?: return
        if (action.data is List<*> && action.data.size > 0) {
            if (action.data[0] is Notification) notifications = action.data as List<Notification>?
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
