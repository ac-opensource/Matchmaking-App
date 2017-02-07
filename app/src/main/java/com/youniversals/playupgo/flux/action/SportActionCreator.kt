package com.youniversals.playupgo.flux.action

import android.support.annotation.StringDef
import com.youniversals.playupgo.flux.Action
import com.youniversals.playupgo.flux.Dispatcher
import com.youniversals.playupgo.flux.Utils
import com.youniversals.playupgo.flux.model.SportModel

/**
 * @author Gian Darren Aquino
 * *
 * @createdAt 31/08/2016
 */
class SportActionCreator(private val mDispatcher: Dispatcher, private val mSportModel: SportModel, private val mUtils: Utils) {

    companion object {
        const val ACTION_GET_SPORTS_S = "ACTION_GET_SPORTS_S"
        const val ACTION_GET_SPORTS_F = "ACTION_GET_SPORTS_F"
    }
    @StringDef(value = *arrayOf(ACTION_GET_SPORTS_S, ACTION_GET_SPORTS_F))
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class SportAction

    fun getSports() {
        mSportModel.getSports()
                .subscribe({ sports ->
                    mDispatcher.dispatch(Action(ACTION_GET_SPORTS_S, sports))
                }, { throwable ->
                    mDispatcher.dispatch(Action(ACTION_GET_SPORTS_F, mUtils.getError(throwable)))
                })
    }
}
