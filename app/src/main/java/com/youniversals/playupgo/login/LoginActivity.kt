package com.youniversals.playupgo.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.pixplicity.easyprefs.library.Prefs
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.flux.BaseActivity
import com.youniversals.playupgo.flux.action.UserActionCreator
import com.youniversals.playupgo.flux.action.UserActionCreator.Companion.ACTION_LOGIN_SUCCESS
import com.youniversals.playupgo.flux.store.UserStore
import com.youniversals.playupgo.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class LoginActivity : BaseActivity() {
    @Inject lateinit var userStore: UserStore
    @Inject lateinit var userActionCreator: UserActionCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayUpApplication.fluxComponent.inject(this)
        initialize()
        setContentView(R.layout.activity_login)
        if (isLoggedIn()) {
            if (Prefs.getLong("userId", 0) != 0L) {
                MainActivity.startActivity(this)
            } else {
                userActionCreator.login(com.facebook.AccessToken.getCurrentAccessToken().token)
            }
        }
        initFacebookLogin()
    }

    private fun initialize() {
        addSubscriptionToUnsubscribe(
                userStore.observableWithFilter(ACTION_LOGIN_SUCCESS).subscribe({
                    it.user()?.let {
                        Prefs.putLong("userId", it.userId)
                        Prefs.putLong("externalId", it.id)
                        Prefs.putString("username", "${it.provider}.${it.id}")
                        Prefs.putString("email", "${it.id}@loopback.${it.provider}.com")

                    }
                    MainActivity.startActivity(this)
                }, {
                    Log.e("A-Ar", it.message, it)
                })
        )

    }

    fun isLoggedIn(): Boolean {
        val accessToken = com.facebook.AccessToken.getCurrentAccessToken()
        return accessToken != null
    }

    private var mCallbackManager: CallbackManager? = null

    private fun initFacebookLogin() {
        // Initialize Facebook Login buttonf
        mCallbackManager = com.facebook.CallbackManager.Factory.create()
        facebookSignIn.setReadPermissions("email", "public_profile")
        facebookSignInObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ userActionCreator.login(it) }, {  })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun facebookSignInObservable() : Observable<String> {
        return Observable.create<String> {
            facebookSignIn.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("", "facebook:onSuccess:$loginResult")
                    it.onNext(loginResult.accessToken.token)
                }

                override fun onCancel() {
                    Log.d("", "facebook:onCancel")
                    // ...
                }

                override fun onError(error: FacebookException) {
                    Log.d("", "facebook:onError", error)
                    // ...
                }
            })
        }
    }
}