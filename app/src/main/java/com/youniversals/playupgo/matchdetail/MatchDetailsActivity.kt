package com.youniversals.playupgo.matchdetail


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.Toolbar
import android.text.format.DateUtils
import android.util.TypedValue
import android.view.View
import com.bumptech.glide.Glide
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Match
import com.youniversals.playupgo.flux.BaseActivity
import com.youniversals.playupgo.flux.action.MatchActionCreator
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_GET_USER_MATCHES_S
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_JOIN_MATCH_F
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_JOIN_MATCH_S
import com.youniversals.playupgo.flux.store.MatchStore
import com.youniversals.playupgo.util.SocialUtils
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

        initFlux()

        match = intent.getParcelableExtra<Match>(EXTRA_MATCH)
        matchDateTextView.text = DateUtils.getRelativeTimeSpanString(match.date, System.currentTimeMillis(), 0)
        matchTitleTextView.text = match.title
        matchDetailsTextView.text = match.description
        joinButton.setOnClickListener {
            if (joinButton.progress != 100) {
                joinButton.progress = 1
                matchActionCreator.joinMatch(match.id)
            }

        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        matchActionCreator.getUsersByMatchId(match.id)
    }

    private fun initFlux() {
        addSubscriptionToUnsubscribe(
                matchStore.observable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            when (it.action) {
                                ACTION_GET_USER_MATCHES_S -> setupMatchPlayersViews(it)
                                ACTION_JOIN_MATCH_S -> joinButton.progress == 100
                                ACTION_JOIN_MATCH_F -> joinButton.progress == 0
                            }
                        }, {
                        })
        )
    }

    private fun setupMatchPlayersViews(it: MatchStore) {
        val team1params = team1members.getChildAt(0).layoutParams
        val team1params2 = team1members.getChildAt(1).layoutParams
        val team2params = team2members.getChildAt(0).layoutParams
        val team2params2 = team2members.getChildAt(1).layoutParams
        team1members.removeAllViews()
        team2members.removeAllViews()
        playersJoinedTextView.text = "${it.usersByMatch?.size}/10"
        it.usersByMatch?.forEach {
//            if (it.userId == 1L) {
//                joinButton.progress == 100
//            }
            val circleImageView = CircleImageView(this, null, R.style.TeamMemberStyle)
            val profileId = it.user!!.username.replace("facebook.", "")
            circleImageView.setOnClickListener {
                SocialUtils.viewFacebookProfile(this, profileId)
            }
            if (it.group == 1L) {
                circleImageView.layoutParams = team1params2
                circleImageView.invalidate()
                Glide.with(this).load("http://graph.facebook.com/v2.2/$profileId/picture").into(circleImageView)
                team1members.addView(circleImageView)
            } else {
                circleImageView.layoutParams = team2params2
                circleImageView.invalidate()
                Glide.with(this).load("http://graph.facebook.com/v2.2/$profileId/picture").into(circleImageView)
                team2members.addView(circleImageView)
            }
        }
        team1members.getChildAt(0)?.let { child ->
            child.layoutParams = team1params
        }
        team2members.getChildAt(0)?.let { child ->
            child.layoutParams = team2params
        }
    }

}
