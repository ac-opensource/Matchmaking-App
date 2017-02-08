package com.youniversals.playupgo.flux.model

import com.youniversals.playupgo.api.RestApi
import com.youniversals.playupgo.data.Match
import com.youniversals.playupgo.data.UserMatch
import rx.Observable

/**
 * YOYO HOLDINGS

 * @author A-Ar Andrew Concepcion
 * *
 * @since 20/12/2016
 */
class MatchModel(val restApi: RestApi) {

    fun getUsersByMatchId(matchId: Long): Observable<List<UserMatch>> {
        val filterString = """{"where":{"matchId": $matchId}, "include":[{"relation": "user", "scope": {"include": [ "identities" ]}}] }"""
        return restApi.getUsersByMatchId(filterString)
    }

    fun getNearbyMatches(latLng: String, maxDistance: Int): Observable<List<Match>> {
//        return restApi.nearMatches(latLng, maxDistance)
        return restApi.nearMatches("0,0", maxDistance)
    }

    fun joinMatch(matchId: Long) : Observable<UserMatch> {
        val userMatch = UserMatch(matchId, 1)
        return restApi.joinMatch(userMatch)
    }

}
