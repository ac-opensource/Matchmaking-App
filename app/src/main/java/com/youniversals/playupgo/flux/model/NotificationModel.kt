package com.youniversals.playupgo.flux.model

import com.pixplicity.easyprefs.library.Prefs
import com.youniversals.playupgo.api.RestApi
import com.youniversals.playupgo.data.Notification
import rx.Observable

/**
 * YOYO HOLDINGS

 * @author A-Ar Andrew Concepcion
 * *
 * @since 20/12/2016
 */
class NotificationModel(restApi: RestApi) {

    private var mRestApi: RestApi = restApi

    fun getNotifications(): Observable<List<Notification>> {
        return mRestApi.getNotifications(Prefs.getLong("userId", 0))
    }

}
