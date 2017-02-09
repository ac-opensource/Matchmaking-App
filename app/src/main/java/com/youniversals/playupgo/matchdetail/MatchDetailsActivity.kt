package com.youniversals.playupgo.matchdetail


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.ShareCompat
import android.support.v7.widget.Toolbar
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Match
import com.youniversals.playupgo.data.UserMatch
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
    lateinit var team1params: ViewGroup.LayoutParams
    lateinit var team1params2: ViewGroup.LayoutParams
    lateinit var team2params: ViewGroup.LayoutParams
    lateinit var team2params2: ViewGroup.LayoutParams

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

        Glide.with(this).load(R.drawable.bg_ice)
                .asGif()
                .error(R.color.material_color_light_blue_400)
                .placeholder(R.color.material_color_light_blue_400)
                .fitCenter()
                .centerCrop()
                .into(team1backgroundImageView)

        Glide.with(this).load(R.drawable.bg_fire)
                .asGif()
                .error(R.color.material_color_red_400)
                .placeholder(R.color.material_color_red_400)
                .fitCenter()
                .centerCrop()
                .into(team2backgroundImageView)
        team1params = team1members.getChildAt(0).layoutParams
        team1params2 = team1members.getChildAt(1).layoutParams
        team2params = team2members.getChildAt(0).layoutParams
        team2params2 = team2members.getChildAt(1).layoutParams

        joinButton.isIndeterminateProgressMode = true
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
                                ACTION_JOIN_MATCH_S -> {
                                    joinButton.progress = 100
                                    setupMatchPlayersViews(it)

                                    ShareCompat.IntentBuilder.from(this)
                                            .setChooserTitle("Share")
                                            .setSubject("I joined a Play")
                                            .setText("I'm playing near here: http://maps.google.com?q=${match.location?.lat},${match.location?.lng}, you can play with me by using PlayUpGo (PlayStore link)")
                                            .setText("You can play with me by using PlayUpGo (PlayStore link)")
                                            .setType("text/plain")
                                            .startChooser()
                                }
                                ACTION_JOIN_MATCH_F -> joinButton.progress = 0
                            }
                        }, {
                            Log.e("A-ar", it.message, it)
                        })
        )
    }

    private fun setupMatchPlayersViews(it: MatchStore) {
        team1members.removeAllViews()
        team2members.removeAllViews()
        playersJoinedTextView.text = "${it.usersByMatch?.size}/10"
        it.usersByMatch?.forEach {
//            if (it.userId == 1L) {
//                joinButton.progress = 100
//            }
            addImageForPlayer(it)
        }
        team1members.getChildAt(0)?.let { child ->
            child.layoutParams = team1params
        }
        team2members.getChildAt(0)?.let { child ->
            child.layoutParams = team2params
        }
    }

    private fun addImageForPlayer(it: UserMatch) {
        val circleImageView = CircleImageView(this, null, R.style.TeamMemberStyle)
        val profileId = it.user!!.username.replace("facebook.", "")
        circleImageView.setOnClickListener {
            SocialUtils.viewFacebookProfile(this, profileId)
        }

        circleImageView.layoutParams = if (it.group == 1L) team1params2 else team2params2
        Glide.with(this).load("http://graph.facebook.com/v2.2/$profileId/picture").into(circleImageView)
        if (it.group == 1L) {
            team1members.addView(circleImageView)
        } else {
            team2members.addView(circleImageView)
        }
    }

}
