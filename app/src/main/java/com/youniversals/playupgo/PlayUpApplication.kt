package com.youniversals.playupgo

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.support.multidex.MultiDex
import com.facebook.FacebookSdk
import com.onesignal.OneSignal
import com.pixplicity.easyprefs.library.Prefs
import com.youniversals.playupgo.di.AppModule
import com.youniversals.playupgo.di.RestModule
import com.youniversals.playupgo.di.flux.DaggerFluxComponent
import com.youniversals.playupgo.di.flux.FluxComponent

class PlayUpApplication : Application() {

    companion object {
        lateinit var fluxComponent: FluxComponent
    }

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(applicationContext)
        // Initialize the Prefs class
        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .build()

        fluxComponent = DaggerFluxComponent.builder()
                .appModule(AppModule(this))
                .restModule(RestModule())
                .build()

        OneSignal.startInit(this).init();
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        try {
            MultiDex.install(this)
        } catch (ignored: RuntimeException) {
            // Multidex support doesn't play well with Robolectric yet
        }
    }
}

