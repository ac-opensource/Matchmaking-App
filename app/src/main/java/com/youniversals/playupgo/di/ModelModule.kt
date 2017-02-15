package com.youniversals.playupgo.di

import com.youniversals.playupgo.api.RestApi
import com.youniversals.playupgo.flux.model.MatchModel
import com.youniversals.playupgo.flux.model.NotificationModel
import com.youniversals.playupgo.flux.model.SportModel
import com.youniversals.playupgo.flux.model.UserModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModelModule {

    @Singleton
    @Provides
    fun providesUserModel(restApi: RestApi): UserModel {
        return UserModel(restApi)
    }

    @Singleton
    @Provides
    fun providesMatchModel(restApi: RestApi): MatchModel {
        return MatchModel(restApi)
    }

    @Singleton
    @Provides
    fun providesSportModel(restApi: RestApi): SportModel {
        return SportModel(restApi)
    }

    @Singleton
    @Provides
    fun providesNotificationModel(restApi: RestApi): NotificationModel {
        return NotificationModel(restApi)
    }
}
