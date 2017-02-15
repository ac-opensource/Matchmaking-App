package com.youniversals.playupgo.di

import com.youniversals.playupgo.flux.Dispatcher
import com.youniversals.playupgo.flux.store.MatchStore
import com.youniversals.playupgo.flux.store.NotificationStore
import com.youniversals.playupgo.flux.store.SportStore
import com.youniversals.playupgo.flux.store.UserStore
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

/**
 * Dagger module to provide flux components. This class will contain the Dispatcher,
 * ActionCreators, and Stores.

 * @author Gian Darren Aquino
 */
@Module
class StoreModule {

    @Singleton
    @Provides
    fun providesDispatcher(userStore: UserStore, matchStore: MatchStore, sportStore: SportStore, notificationStore: NotificationStore): Dispatcher {
        return Dispatcher(
                Arrays.asList(
                        userStore,
                        matchStore,
                        sportStore,
                        notificationStore
                )
        )
    }

    @Singleton
    @Provides
    fun providesUserStore(): UserStore {
        return UserStore()
    }

    @Singleton
    @Provides
    fun providesMatchStore(): MatchStore {
        return MatchStore()
    }

    @Singleton
    @Provides
    fun providesSportStore(): SportStore {
        return SportStore()
    }

    @Singleton
    @Provides
    fun providesNotificationStore(): NotificationStore {
        return NotificationStore()
    }
}
