package com.youniversals.playupgo.matchdetail


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.pixplicity.easyprefs.library.Prefs
import com.yarolegovich.lovelydialog.LovelyChoiceDialog
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Match
import com.youniversals.playupgo.data.UserMatch
import com.youniversals.playupgo.flux.BaseActivity
import com.youniversals.playupgo.flux.action.MatchActionCreator
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_ACCEPT_JOIN_MATCH_S
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_GET_USER_MATCHES_S
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_JOIN_MATCH_F
import com.youniversals.playupgo.flux.action.MatchActionCreator.Companion.ACTION_JOIN_MATCH_S
import com.youniversals.playupgo.flux.action.SportActionCreator
import com.youniversals.playupgo.flux.action.SportActionCreator.Companion.ACTION_GET_SPORT_BY_ID_S
import com.youniversals.playupgo.flux.store.MatchStore
import com.youniversals.playupgo.flux.store.SportStore
import com.youniversals.playupgo.profile.ProfileActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_match_details.*
import kotlinx.android.synthetic.main.content_match_details.*
import kotlinx.android.synthetic.main.part_match_details.*
import rx.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject



class MatchDetailsActivity : BaseActivity() {
    @Inject lateinit var matchActionCreator: MatchActionCreator
    @Inject lateinit var matchStore: MatchStore
    @Inject lateinit var sportActionCreator: SportActionCreator
    @Inject lateinit var sportStore: SportStore

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
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp)
            toolbar.setNavigationOnClickListener {
                finish()
            }
        }
        initFlux()

        match = intent.getParcelableExtra<Match>(EXTRA_MATCH)
        val matchDateCal = Calendar.getInstance()
        matchDateCal.timeInMillis = match.date
        timeTextView.text = SimpleDateFormat("h:mm a", Locale.getDefault()).format(matchDateCal.time)
        dateTextView.text = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(matchDateCal.time)
        locationNameTextView.text = match.locationName
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

        seeLocationTextView.setOnClickListener { viewOnMap() }
        setCalendarEventTextView.setOnClickListener { setCalendarEvent() }
        joinButton.isIndeterminateProgressMode = true
        joinButton.setOnClickListener {
            if (joinButton.progress != 100) {
                joinButton.progress = 1
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "match")
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, match.title)
                bundle.putString(FirebaseAnalytics.Param.LOCATION, match.locationName)
                FirebaseAnalytics.getInstance(this).logEvent("join_match", bundle)
                matchActionCreator.joinMatch(match.id)
            }

        }
        matchActionCreator.getUsersByMatchId(match.id)
        sportActionCreator.getSport(match.sportId)
    }

    private fun setCalendarEvent() {
        val matchStart = Calendar.getInstance()
        matchStart.timeInMillis = match.date
        val matchEnd = Calendar.getInstance()
        matchEnd.timeInMillis = match.date
        matchEnd.add(Calendar.HOUR, 1)
        val intent = Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, matchStart.timeInMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, matchEnd.timeInMillis)
                .putExtra(Events.TITLE, match.title)
                .putExtra(Events.DESCRIPTION, match.description)
                .putExtra(Events.EVENT_LOCATION, match.locationName)
                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
        startActivity(intent)
    }

    private fun viewOnMap() {
        val uri = "http://maps.google.com/maps?q=loc:${match.location?.lat},${match.location?.lng} (${match.locationName})"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        intent.flags = Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
        intent.data = Uri.parse(uri)
        startActivity(intent)
    }

    private fun initFlux() {
        addSubscriptionToUnsubscribe(matchStore.observable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.action) {
                        ACTION_GET_USER_MATCHES_S -> setupMatchPlayersViews(it)
                        ACTION_JOIN_MATCH_S -> {
                            joinButton.progress = 100
                            matchActionCreator.getUsersByMatchId(match.id)
                            ShareCompat.IntentBuilder.from(this)
                                    .setChooserTitle("Share")
                                    .setSubject("I joined a ${whatToDoTextView.text ?: "sports"} game")
                                    .setText("I'm playing near here: http://maps.google.com?q=${match.location?.lat},${match.location?.lng}, you can play with me by using PlayUpGo (PlayStore link)")
                                    .setText("Let's play ${whatToDoTextView.text ?: "sports"}! You can play with me by using PlayUpGo https://play.google.com/store/apps/details?id=com.youniversals.playupgo")
                                    .setType("text/plain")
                                    .startChooser()
                        }
                        ACTION_ACCEPT_JOIN_MATCH_S -> {
                            matchActionCreator.getUsersByMatchId(match.id)
                        }
                        ACTION_JOIN_MATCH_F -> joinButton.progress = 0
                    }
                }, {
                    Log.e("A-ar", it.message, it)
                })
        )

        addSubscriptionToUnsubscribe(
                sportStore.observableWithFilter(ACTION_GET_SPORT_BY_ID_S)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.error != null) return@subscribe
                            it.sports?.let {
                                whatToDoTextView.text = it[0].name
                            }
                        }, {
                            Log.e("A-ar", it.message, it)
                        })
        )
    }

    fun showContextDialog(userMatch: UserMatch, profileId: String) {
        val adapter = ProfileClickActionsAdapter(this, listOf("Accept Request", "View Profile"))
        LovelyChoiceDialog(this)
                .setTopColorRes(R.color.material_color_indigo_900)
                .setTitle("Accept Request?")
//                .setIcon(R.drawable.ic_local_atm_white_36dp)
                .setMessage("When you accept a request, it moves the match request for the player from pending to the match players.")
                .setItems(adapter) { position, item ->
                    when (position) {
                        0 -> matchActionCreator.acceptJoinMatch(userMatch)
                        1 -> ProfileActivity.startActivity(this, profileId)
                    }
                }
                .show()
    }

    private fun setupMatchPlayersViews(it: MatchStore) {
        pendingJoinsList.removeAllViews()
        team1members.removeAllViews()
        team2members.removeAllViews()
        playersJoinedTextView.text = "${it.usersByMatch?.size}/10"
        it.usersByMatch?.forEach {
            if (it.userId == Prefs.getLong("userId", -1)) {
                joinButton.progress = 100
            }
            addImageForPlayer(it)
        }
        pendingJoinsList.getChildAt(0)?.let { child ->
            child.layoutParams = team1params
        }
        team1members.getChildAt(0)?.let { child ->
            child.layoutParams = team1params
        }
        team2members.getChildAt(0)?.let { child ->
            child.layoutParams = team2params
        }
    }

    private fun addImageForPlayer(userMatch: UserMatch) {
        val circleImageView = CircleImageView(this, null, R.style.TeamMemberStyle)
        val profileId = userMatch.user!!.username.replace("facebook.", "")
        circleImageView.setOnClickListener {
            if (Prefs.getLong("userId", -1) == match.userId && userMatch.group == 0L) {
                showContextDialog(userMatch, profileId)
            } else {
                ProfileActivity.startActivity(this, profileId)
            }
        }

        circleImageView.layoutParams = if (userMatch.group == 1L || userMatch.group == 0L) team1params2 else team2params2
        Glide.with(this).load("http://graph.facebook.com/v2.2/$profileId/picture").into(circleImageView)
        when (userMatch.group) {
            0L -> pendingJoinsList.addView(circleImageView)
            1L -> team1members.addView(circleImageView)
            2L -> team2members.addView(circleImageView)
        }
    }

}
