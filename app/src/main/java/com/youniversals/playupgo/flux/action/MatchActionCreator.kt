package com.youniversals.playupgo.flux.action

import android.support.annotation.StringDef
import com.youniversals.playupgo.data.MatchJson
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
        const val ACTION_GET_USER_MATCHES_S = "ACTION_GET_USER_MATCHES_S"
        const val ACTION_GET_USER_MATCHES_F = "ACTION_GET_USER_MATCHES_F"
        const val ACTION_UPDATE_NEW_MATCH_S = "ACTION_UPDATE_NEW_MATCH_S"
        const val ACTION_UPDATE_NEW_MATCH_F = "ACTION_UPDATE_NEW_MATCH_F"
    }
    @StringDef(value = *arrayOf(
            ACTION_GET_NEARBY_MATCHES_S,
            ACTION_GET_NEARBY_MATCHES_F,
            ACTION_GET_USER_MATCHES_S,
            ACTION_GET_USER_MATCHES_F,
            ACTION_UPDATE_NEW_MATCH_S,
            ACTION_UPDATE_NEW_MATCH_F))
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

    fun getNearbyMatches(latLng: String, maxDistance: Int) {
        matchModel.getNearbyMatches(latLng, maxDistance)
                .subscribe({
                    mDispatcher.dispatch(Action(ACTION_GET_NEARBY_MATCHES_S, it))
                }, {
                    mDispatcher.dispatch(Action(ACTION_GET_NEARBY_MATCHES_F, mUtils.getError(it)))
                })
    }

    fun updateNewMatch(match: MatchJson) {
        mDispatcher.dispatch(Action(ACTION_UPDATE_NEW_MATCH_S, match))
    }
}
