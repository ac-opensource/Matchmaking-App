package com.youniversals.playupgo.di

import android.content.Context
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.flux.Utils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: PlayUpApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app
    }

    @Provides
    @Singleton
    fun provideApplication(): PlayUpApplication {
        return app
    }

    @Provides
    @Singleton
    fun provideUtils(context: Context): Utils {
        return Utils(context)
    }
}