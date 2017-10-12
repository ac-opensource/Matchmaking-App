package com.youniversals.playupgo.flux.action

import android.support.annotation.StringDef
import com.youniversals.playupgo.flux.Action
import com.youniversals.playupgo.flux.Dispatcher
import com.youniversals.playupgo.flux.Utils
import com.youniversals.playupgo.flux.model.NotificationModel

/**
 * @author Gian Darren Aquino
 * *
 * @createdAt 31/08/2016
 */
class NotificationActionCreator(private val mDispatcher: Dispatcher, private val mNotificationModel: NotificationModel, private val mUtils: Utils) {

    companion object {
        const val ACTION_GET_NOTIF_S = "ACTION_GET_NOTIF_S"
        const val ACTION_GET_NOTIF_F = "ACTION_GET_NOTIF_F"
    }
    @StringDef(value = *arrayOf(ACTION_GET_NOTIF_S, ACTION_GET_NOTIF_F))
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class NotificationAction

    fun getNotifications() {
        mNotificationModel.getNotifications()
                .subscribe({
                    mDispatcher.dispatch(Action(ACTION_GET_NOTIF_S, it))
                }, { throwable ->
                    mDispatcher.dispatch(Action(ACTION_GET_NOTIF_F, mUtils.getError(throwable)))
                })
    }
}
