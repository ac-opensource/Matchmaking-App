package com.youniversals.playupgo.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Match
import kotlinx.android.synthetic.main.match_list_view_item.view.*

/**
 *
 *
 * YOYO HOLDINGS
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 28/12/2016
 */



class MatchView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var matchCard: View? = null

    var data: Match? = null

    var title: String? = null
        set(value) {
            field = value
            matchTitleTextView.text = value
        }

    var distance: String? = null
        set(value) {
            field = value
            distanceTextView.text = value
        }

    var date: String? = null
        set(value) {
            field = value
            matchDateTextView.text = value
        }

    init {
        init()
    }

    private fun init() {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.match_list_view_item, this)
        matchCard = matchCardView
    }

    fun clear() {
    }
}