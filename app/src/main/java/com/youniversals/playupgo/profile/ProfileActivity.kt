package com.youniversals.playupgo.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.flux.BaseActivity
import com.youniversals.playupgo.flux.action.UserActionCreator
import com.youniversals.playupgo.flux.action.UserActionCreator.Companion.ACTION_GET_USER_PROFILE_S
import com.youniversals.playupgo.flux.store.UserStore
import com.youniversals.playupgo.util.SocialUtils
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.content_profile.*
import javax.inject.Inject

class ProfileActivity : BaseActivity() {

    @Inject lateinit var userActionCreator: UserActionCreator
    @Inject lateinit var userStore: UserStore

    companion object {
        private val EXTRA_EXTERNAL_ID: String = "EXTRA_EXTERNAL_ID"

        fun startActivity(context: Context, externalId: String) {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(EXTRA_EXTERNAL_ID, externalId)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayUpApplication.fluxComponent.inject(this)
        setContentView(R.layout.activity_profile)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val externalId = intent.getStringExtra(EXTRA_EXTERNAL_ID)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            SocialUtils.viewFacebookProfile(this, externalId)
        }

        initFlux()
        userActionCreator.getUserProfile(externalId)
//        heartLottieView.setOnClickListener {
//            heartLottieView.playAnimation()
//        }

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp)
            toolbar.setNavigationOnClickListener {
                finish()
            }
        }
//
//        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
//            override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarStateChangeListener.State) {
//                when (state) {
//                    State.COLLAPSED -> {
//                        appBar.background = ContextCompat.getDrawable(this@ProfileActivity, R.color.colorPrimary)
//                    }
//                    State.EXPANDED -> {
//                        appBar.background = ContextCompat.getDrawable(this@ProfileActivity, R.color.transparent)
//                    }
//                }
//            }
//        })

        Glide.with(this)
                .load("http://graph.facebook.com/v2.2/$externalId/picture?type=large")
                .asBitmap()
                .into(object : SimpleTarget<Bitmap>(SimpleTarget.SIZE_ORIGINAL, SimpleTarget.SIZE_ORIGINAL) {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        profilePictureFrissonView.setBitmap(resource!!)
                    }
                })
    }

    private fun initFlux() {
        userStore.observableWithFilter(ACTION_GET_USER_PROFILE_S)
                .subscribe {
                    if (it.error() != null) return@subscribe
                    val name = it.userProfile!!.profile.name
                    nameTextView.text = "${name.givenName} ${name.familyName}"
                }
    }
}
