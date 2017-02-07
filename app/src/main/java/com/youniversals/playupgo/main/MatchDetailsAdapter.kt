package com.youniversals.playupgo.main

import android.support.annotation.LayoutRes
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Match
import com.youniversals.playupgo.data.UserMatch


/**
 *
 *
 * YOYO HOLDINGS
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 28/12/2016
 */

class MatchDetailsAdapter(onMatchDetailsClickListener: OnMatchDetailsClickListener,
                          match: Match,
                          userMatches: List<UserMatch>) : EpoxyAdapter() {
    init {
        addModel(VersusModel(userMatches, onMatchDetailsClickListener))
    }
}

interface OnMatchDetailsClickListener {
    fun onProfileClicked(facebookId: String)
    fun onJoinMatchClicked(matchId: String)
}

class VersusModel(private val userMatches: List<UserMatch>, private val onMatchDetailsClickListener: OnMatchDetailsClickListener) : EpoxyModel<MatchView>() {

    init {
        if (userMatches.isNotEmpty()) {
            userMatches.first()
        }
    }

    @LayoutRes
    public override fun getDefaultLayout(): Int {
        return R.layout.match_model
    }



    override fun bind(matchView: MatchView?) {
//        matchView?.data = match
//        matchView?.title = match.title
        matchView?.date = "12/12/2016"
        matchView?.distance = "12km"
        matchView?.setOnClickListener({
        })
    }

    override fun unbind(sportView: MatchView?) {
        super.unbind(sportView)
        sportView?.setOnClickListener(null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        if (!super.equals(other)) return false

        other as VersusModel

//        if (match != other.match) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
//        result = 31 * result + match.hashCode()
        return result
    }
}