package com.youniversals.playupgo.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.youniversals.playupgo.R
import com.youniversals.playupgo.flux.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*




class ProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

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
        val profileId = 10208575991616235
        Glide.with(this)
                .load("http://graph.facebook.com/v2.2/$profileId/picture?type=large")
                .asBitmap()
                .into(object : SimpleTarget<Bitmap>(SimpleTarget.SIZE_ORIGINAL, SimpleTarget.SIZE_ORIGINAL) {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        profilePictureFrissonView.setBitmap(resource!!)
                    }
                })
    }
}
