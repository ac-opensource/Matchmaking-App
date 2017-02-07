package com.youniversals.playupgo.matchdetail


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.Toolbar
import android.text.format.DateUtils
import android.view.View
import com.bumptech.glide.Glide
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Match
import com.youniversals.playupgo.flux.BaseActivity
import com.youniversals.playupgo.flux.action.MatchActionCreator
import com.youniversals.playupgo.flux.store.MatchStore
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.content_match_details.*
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class MatchDetailsActivity : BaseActivity() {
    @Inject lateinit var matchActionCreator: MatchActionCreator
    @Inject lateinit var matchStore: MatchStore

    lateinit private var match: Match

    companion object {

        private val EXTRA_MATCH = "EXTRA_MATCH"
        fun startActivity(context: Context, match: Match, sharedMatchCardView: View) {
            val intent = Intent(context, MatchDetailsActivity::class.java)
            // Pass data object in the bundle and populate details activity.
            intent.putExtra(MatchDetailsActivity.EXTRA_MATCH, match)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, sharedMatchCardView as View, "match")
            context.startActivity(intent, options.toBundle())
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayUpApplication.fluxComponent.inject(this)
        setContentView(R.layout.activity_match_details)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        match = intent.getParcelableExtra<Match>(EXTRA_MATCH)

        matchDateTextView.text = DateUtils.getRelativeTimeSpanString(match.date, System.currentTimeMillis(), 0)
        matchTitleTextView.text = match.title
        matchDetailsTextView.text = match.description
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initFlux()


        matchActionCreator.getUsersByMatchId(match.id)
    }

    private fun initFlux() {
        addSubscriptionToUnsubscribe(
                matchStore.observable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.error != null) {
                                return@subscribe
                            }

                            team1members.removeAllViews()
                            team2members.removeAllViews()

                            playersJoinedTextView.text = "${it.usersByMatch?.size}/10"
                            it.usersByMatch?.forEach {
                                val circleImageView = CircleImageView(this, null, R.style.TeamMemberStyle)
                                Glide.with(this).load("http://graph.facebook.com/v2.2/${it.user.username.replace("facebook.", "")}/picture")
                                if (it.group == 1L) {
                                    team1members.addView(circleImageView)
                                } else {
                                    team2members.addView(circleImageView)
                                }
                            }
                        }, {

                        })
        )
    }

}
