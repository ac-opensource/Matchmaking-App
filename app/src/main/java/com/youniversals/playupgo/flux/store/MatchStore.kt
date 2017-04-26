package com.youniversals.playupgo.flux.store

import com.youniversals.playupgo.data.Match
import com.youniversals.playupgo.data.MatchJson
import com.youniversals.playupgo.data.UserMatch
import com.youniversals.playupgo.flux.Action
import com.youniversals.playupgo.flux.AppError
import com.youniversals.playupgo.flux.Store
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_ACCEPT_JOIN_MATCH_S
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_CREATE_MATCH_F
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_CREATE_MATCH_S
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_GET_LATEST_USER_MATCHES_S
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_GET_NEARBY_MATCHES_S
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_GET_USER_MATCHES_S
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_JOIN_MATCH_F
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_JOIN_MATCH_S
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_UPDATE_NEW_MATCH_S
import rx.Observable

/**
 * @author Gian Darren Aquino
 * *
 * @createdAt 17/08/2016
 */
class MatchStore : Store<MatchStore>() {

    var usersByMatch: List<UserMatch>? = null
        private set
    var error: AppError? = null
        private set
    var action = Action.ACTION_NO_ACTION
        private set
    var matches: List<Match>? = null
        private set
    var newMatch: MatchJson? = null
        private set

    fun observableWithFilter(action: String): Observable<MatchStore> {
        return observable().filter { action == it.action }
    }

    override fun onReceiveAction(action: Action) {
        when (action.type) {
            ACTION_GET_USER_MATCHES_S -> {
                updateState()
                updateData(action)
                updateError(action)
                updateAction(action)
                notifyStoreChanged(this)
            }
            ACTION_GET_LATEST_USER_MATCHES_S,
            ACTION_GET_NEARBY_MATCHES_S -> {
                updateState()
                updateData(action)
                updateError(action)
                updateAction(action)
                notifyStoreChanged(this)
            }
            ACTION_UPDATE_NEW_MATCH_S -> {
                updateState()
                updateData(action)
                updateError(action)
                updateAction(action)
                notifyStoreChanged(this)
            }
            ACTION_CREATE_MATCH_S -> {
                updateState()
                updateData(action)
                updateError(action)
                updateAction(action)
                notifyStoreChanged(this)
            }
            ACTION_ACCEPT_JOIN_MATCH_S,
            ACTION_JOIN_MATCH_S -> {
                updateState()
                updateData(action)
                updateError(action)
                updateAction(action)
                notifyStoreChanged(this)
            }
            ACTION_JOIN_MATCH_F -> {
                updateState()
                updateData(action)
                updateError(action)
                updateAction(action)
                notifyStoreChanged(this)
            }
            ACTION_CREATE_MATCH_F -> {
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
        usersByMatch = null
        matches = null
        newMatch = null
        action.data ?: return
        if (action.data is List<*> && action.data.size > 0) {
            if (action.data[0] is UserMatch) {
                usersByMatch = action.data as List<UserMatch>?
            } else if (action.data[0] is Match)
                matches = action.data as List<Match>?
        } else if (action.data is MatchJson) {
            newMatch = action.data
        } else if (action.data is UserMatch) {
            if (usersByMatch == null) {
                usersByMatch = listOf(action.data)
            } else {
                usersByMatch?.plus(action.data)
            }
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
