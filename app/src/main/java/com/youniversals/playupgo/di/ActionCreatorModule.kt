package com.youniversals.playupgo.di

import com.youniversals.playupgo.flux.Dispatcher
import com.youniversals.playupgo.flux.Utils
import com.youniversals.playupgo.flux.action.MatchActionCreator
import com.youniversals.playupgo.flux.action.SportActionCreator
import com.youniversals.playupgo.flux.action.UserActionCreator
import com.youniversals.playupgo.flux.model.MatchModel
import com.youniversals.playupgo.flux.model.SportModel
import com.youniversals.playupgo.flux.model.UserModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Gian Darren Aquino
 */
@Module
class ActionCreatorModule {

    @Singleton @Provides
    fun providesUserActionCreator(dispatcher: Dispatcher,
                                           userModel: UserModel,
                                           utils: Utils): UserActionCreator {
        return UserActionCreator(dispatcher, userModel, utils)
    }

    @Singleton @Provides
    fun providesMatchActionCreator(dispatcher: Dispatcher,
                                           matchModel: MatchModel,
                                           utils: Utils): MatchActionCreator {
        return MatchActionCreator(dispatcher, matchModel, utils)
    }

    @Singleton @Provides
    fun providesSportActionCreator(dispatcher: Dispatcher,
                                           sportModel: SportModel,
                                           utils: Utils): SportActionCreator {
        return SportActionCreator(dispatcher, sportModel, utils)
    }
}
