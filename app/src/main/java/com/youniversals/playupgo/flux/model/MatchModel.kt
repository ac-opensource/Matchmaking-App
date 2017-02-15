package com.youniversals.playupgo.flux.model

import com.onesignal.OneSignal
import com.pixplicity.easyprefs.library.Prefs
import com.youniversals.playupgo.api.RestApi
import com.youniversals.playupgo.data.Match
import com.youniversals.playupgo.data.MatchJson
import com.youniversals.playupgo.data.User
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
        return restApi.nearMatches(latLng, maxDistance)
    }

    fun joinMatch(matchId: Long) : Observable<UserMatch> {
        val userMatch = UserMatch(matchId, Prefs.getLong("userId", 0))
        return restApi.joinMatch(userMatch).map {
            OneSignal.sendTag("matchId", matchId.toString())
            it.copy(user = User(
                    id = Prefs.getLong("userId", 0),
                    username = Prefs.getString("username", "")))
        }
    }

    fun createMatch(newMatch: MatchJson): Observable<Match> {
        return restApi.createMatch(newMatch).flatMap { createdMatch ->
            OneSignal.sendTag("matchId", createdMatch.id.toString())
            joinMatch(createdMatch.id).map { createdMatch }
        }
    }

}
