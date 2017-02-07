package com.youniversals.playupgo.flux.model

import com.youniversals.playupgo.api.RestApi
import com.youniversals.playupgo.data.UserIdentity
import rx.Observable

/**
 * YOYO HOLDINGS

 * @author A-Ar Andrew Concepcion
 * *
 * @since 20/12/2016
 */
class UserModel(restApi: RestApi) {

    private var mRestApi: RestApi = restApi

    fun login(accessToken: String): Observable<UserIdentity> {
        return mRestApi.login(accessToken).map { it.userIdentity }
    }

}
