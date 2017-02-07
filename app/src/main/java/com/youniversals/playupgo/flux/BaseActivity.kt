package com.youniversals.playupgo.flux

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * @author Gian Darren Aquino
 */
abstract class BaseActivity : AppCompatActivity(), AliveUiThread {

    companion object {
        private val MAIN_THREAD_HANDLER = Handler(Looper.getMainLooper())
    }

    /**
     * Subscriptions to unsubcribe in [.onDestroy]
     */
    private val mSubscriptions = CompositeSubscription()

    override fun onDestroy() {
        super.onDestroy()
        mSubscriptions.clear()
    }

    /**
     * Unsubscribe the subscription automatically when the [Activity] is destroyed.

     * @param subscription The subscription to unsubscribe.
     */
    protected fun addSubscriptionToUnsubscribe(subscription: Subscription) {
        mSubscriptions.add(subscription)
    }

    override fun runOnUiThreadIfAlive(runnable: Runnable) {
        runOnUiThreadIfAlive(runnable, 0)
    }

    /**
     * Execute the runnable on ui thread with specified delay.

     * @param runnable The runnable to execute.
     * *
     * @param delayMillis The delay before executing the runnable.
     */
    override fun runOnUiThreadIfAlive(runnable: Runnable, delayMillis: Long) {
        if (isFinishing) {
            return
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
            return
        }
        MAIN_THREAD_HANDLER.postDelayed(runnable, delayMillis)
    }


}