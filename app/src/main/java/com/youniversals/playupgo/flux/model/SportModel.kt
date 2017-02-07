package com.youniversals.playupgo.flux.model

import com.youniversals.playupgo.api.RestApi
import com.youniversals.playupgo.data.Sport
import rx.Observable

/**
 * YOYO HOLDINGS

 * @author A-Ar Andrew Concepcion
 * *
 * @since 20/12/2016
 */
class SportModel(restApi: RestApi) {

    private var mRestApi: RestApi = restApi

    fun getSports(): Observable<List<Sport>> {
        return mRestApi.getSports()
    }

}
