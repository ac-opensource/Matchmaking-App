package com.youniversals.playupgo.flux.model

import com.onesignal.OneSignal
import com.pixplicity.easyprefs.library.Prefs
import com.youniversals.playupgo.api.RestApi
import com.youniversals.playupgo.data.Match
import com.youniversals.playupgo.data.MatchJson
import com.youniversals.playupgo.data.User
import com.youniversals.playupgo.data.UserMatch
import rx.Observable
import java.util.concurrent.TimeUnit

/**
 * YOYO HOLDINGS

 * @author A-Ar Andrew Concepcion
 * *
 * @since 20/12/2016
 */
class MatchModel(val restApi: RestApi) {

    fun getUsersByMatchId(matchId: Long): Observable<List<UserMatch>> {
        val filterString = """{"where":{"matchId": $matchId}, "include":[{"relation": "user"}]}"""
        return restApi.getUsersByMatchId(filterString)
    }

    fun getNearbyMatches(latLng: String, maxDistance: Int, sportId: Long): Observable<List<Match>> {
        return restApi.nearMatches(location=latLng, maxDistance = maxDistance, sportId = sportId).timeout(3000, TimeUnit.SECONDS)
    }

    fun joinMatch(matchId: Long, group: Long) : Observable<UserMatch> {
        val userMatch = UserMatch(matchId, Prefs.getLong("userId", 0), group = group)
        return restApi.joinMatch(userMatch).map {
            OneSignal.sendTag("matchId", matchId.toString())
            it.copy(user = User(
                    id = Prefs.getLong("userId", 0),
                    username = Prefs.getString("username", "")))
        }
    }

    fun acceptJoinMatch(um: UserMatch) : Observable<UserMatch> {
        val userMatch = UserMatch(um.matchId, um.userId, id = um.id!!, group = um.group, isApproved = true)
        return restApi.acceptJoinMatch(userMatch).map {
            OneSignal.sendTag("matchId", um.matchId.toString())
            it.copy(user = User(
                    id = um.userId,
                    username = um.user!!.username))
        }
    }

    fun createMatch(newMatch: MatchJson): Observable<Match> {
        return restApi.createMatch(newMatch).flatMap { createdMatch ->
            OneSignal.sendTag("matchId", createdMatch.id.toString())
            joinMatch(createdMatch.id, group = 1).map { createdMatch }
        }
    }

    fun getMatches(userId: Long): Observable<List<Match>> {
        return restApi.getMatches(userId)
    }

}
