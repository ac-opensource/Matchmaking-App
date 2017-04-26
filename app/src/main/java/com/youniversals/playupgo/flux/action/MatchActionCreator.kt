package com.youniversals.playupgo.flux.action

import android.support.annotation.StringDef
import com.youniversals.playupgo.data.MatchJson
import com.youniversals.playupgo.data.UserMatch
import com.youniversals.playupgo.flux.Action
import com.youniversals.playupgo.flux.Dispatcher
import com.youniversals.playupgo.flux.Utils
import com.youniversals.playupgo.flux.model.MatchModel

/**
 * YOYO HOLDINGS

 * @author A-Ar Andrew Concepcion
 * *
 * @since 28/01/2017
 */
class MatchActionCreator(private val mDispatcher: Dispatcher, private val matchModel: MatchModel, private val mUtils: Utils) {

    companion object {
        const val ACTION_GET_NEARBY_MATCHES_S = "ACTION_GET_NEARBY_MATCHES_S"
        const val ACTION_GET_NEARBY_MATCHES_F = "ACTION_GET_NEARBY_MATCHES_F"
        const val ACTION_GET_LATEST_USER_MATCHES_S = "ACTION_GET_LATEST_USER_MATCHES_S"
        const val ACTION_GET_LATEST_USER_MATCHES_F = "ACTION_GET_LATEST_USER_MATCHES_F"
        const val ACTION_GET_USER_MATCHES_S = "ACTION_GET_USER_MATCHES_S"
        const val ACTION_GET_USER_MATCHES_F = "ACTION_GET_USER_MATCHES_F"
        const val ACTION_UPDATE_NEW_MATCH_S = "ACTION_UPDATE_NEW_MATCH_S"
        const val ACTION_UPDATE_NEW_MATCH_F = "ACTION_UPDATE_NEW_MATCH_F"
        const val ACTION_ACCEPT_JOIN_MATCH_S = "ACTION_ACCEPT_JOIN_MATCH_S"
        const val ACTION_ACCEPT_JOIN_MATCH_F = "ACTION_ACCEPT_JOIN_MATCH_F"
        const val ACTION_JOIN_MATCH_S = "ACTION_JOIN_MATCH_S"
        const val ACTION_JOIN_MATCH_F = "ACTION_JOIN_MATCH_F"
        const val ACTION_CREATE_MATCH_S = "ACTION_CREATE_MATCH_S"
        const val ACTION_CREATE_MATCH_F = "ACTION_CREATE_MATCH_F"
    }
    @StringDef(value = *arrayOf(
            ACTION_GET_NEARBY_MATCHES_S,
            ACTION_GET_NEARBY_MATCHES_F,
            ACTION_GET_USER_MATCHES_S,
            ACTION_GET_USER_MATCHES_F,
            ACTION_UPDATE_NEW_MATCH_S,
            ACTION_UPDATE_NEW_MATCH_F,
            ACTION_JOIN_MATCH_S,
            ACTION_JOIN_MATCH_F,
            ACTION_CREATE_MATCH_S,
            ACTION_CREATE_MATCH_F))
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class MatchAction

    fun getUsersByMatchId(matchId: Long) {
        matchModel.getUsersByMatchId(matchId)
                .subscribe({
                    mDispatcher.dispatch(Action(ACTION_GET_USER_MATCHES_S, it))
                }, {
                    mDispatcher.dispatch(Action(ACTION_GET_USER_MATCHES_F, mUtils.getError(it)))
                })
    }

    fun getMatches(userId: Long) {
        matchModel.getMatches(userId)
                .subscribe({
                    mDispatcher.dispatch(Action(ACTION_GET_LATEST_USER_MATCHES_S, it))
                }, {
                    mDispatcher.dispatch(Action(ACTION_GET_LATEST_USER_MATCHES_F, mUtils.getError(it)))
                })
    }

    fun getNearbyMatches(latLng: String, maxDistance: Int, sportId: Long) {
        matchModel.getNearbyMatches(latLng, maxDistance, sportId)
                .subscribe({
                    mDispatcher.dispatch(Action(ACTION_GET_NEARBY_MATCHES_S, it))
                }, {
                    mDispatcher.dispatch(Action(ACTION_GET_NEARBY_MATCHES_F, mUtils.getError(it)))
                })
    }

    fun joinMatch(matchId: Long, group: Long) {
        matchModel.joinMatch(matchId, group)
                .subscribe({
                    mDispatcher.dispatch(Action(ACTION_JOIN_MATCH_S, it))
                }, {
                    mDispatcher.dispatch(Action(ACTION_JOIN_MATCH_F, mUtils.getError(it)))
                })
    }

    fun updateNewMatch(match: MatchJson) {
        mDispatcher.dispatch(Action(ACTION_UPDATE_NEW_MATCH_S, match))
    }

    fun createMatch(newMatch: MatchJson) {
        matchModel.createMatch(newMatch)
                .subscribe({
                    mDispatcher.dispatch(Action(ACTION_CREATE_MATCH_S, it))
                }, {
                    mDispatcher.dispatch(Action(ACTION_CREATE_MATCH_F, mUtils.getError(it)))
                })
    }

    fun acceptJoinMatch(userMatch: UserMatch) {
        matchModel.acceptJoinMatch(userMatch)
                .subscribe({
                    mDispatcher.dispatch(Action(ACTION_ACCEPT_JOIN_MATCH_S, it))
                }, {
                    mDispatcher.dispatch(Action(ACTION_ACCEPT_JOIN_MATCH_F, mUtils.getError(it)))
                })
    }
}
