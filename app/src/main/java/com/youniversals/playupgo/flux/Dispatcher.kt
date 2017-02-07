package com.youniversals.playupgo.flux

import android.os.Handler
import android.os.Looper
import java.util.*

/**
 * Class responsible for dispatching actions throughout the application to the stores.

 * @author Gian Darren Aquino
 */
class Dispatcher(stores: List<Store<*>>) {

    private val mStores: List<Store<*>>

    private val mMainThreadHandler = Handler(Looper.getMainLooper())

    init {
        mStores = Collections.unmodifiableList(stores)
    }

    /**
     * Dispatch actions to the stores in UI Thread
     */
    fun dispatch(action: Action) {
        mMainThreadHandler.post {
            for (store in mStores) {
                store.dispatchAction(action)
            }
        }
    }
}
