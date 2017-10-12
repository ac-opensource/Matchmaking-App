package com.youniversals.playupgo.main

import android.content.Context
import android.support.annotation.LayoutRes
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Match
import kotlinx.android.synthetic.main.match_list_view_item.view.*


/**
 * YOYO HOLDINGS
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 28/12/2016
 */

class MatchListAdapter(var onClickListener: View.OnClickListener) : EpoxyAdapter() {
    private var selectedPosition: Int = 0

    init {
//        addModels(CreateMatchModel("My Photos"))
    }

    fun addMatches(matches: Collection<Match>?) {
        matches?.forEach {
            addModel(MatchModel(it, onClickListener))
        }

        if (matches == null || matches.isEmpty()) {
            addModel(NoMatchFoundModel())
        }

        addModel(AddMatchModel(onClickListener))
    }
}

class MatchModel(private val match: Match, private val onClickListener: View.OnClickListener) : EpoxyModel<MatchView>() {

    init {
        id(match.id)
    }

    @LayoutRes
    public override fun getDefaultLayout(): Int {
        return R.layout.match_model
    }

    override fun bind(matchView: MatchView?) {
        matchView?.data = match
        matchView?.title = match.title
        matchView?.date = DateUtils.getRelativeTimeSpanString(match.date, System.currentTimeMillis(), 0).toString()
        matchView?.distance = ""
        matchView?.setOnClickListener(onClickListener)
    }

    override fun unbind(sportView: MatchView?) {
        super.unbind(sportView)
        sportView?.setOnClickListener(null)
    }
}

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

class NoMatchFoundModel : EpoxyModel<NoMatchFoundView>() {
    init {
        id(0)
    }
    override fun getDefaultLayout(): Int {
        return R.layout.match_model_nothing_found
    }
}


class AddMatchModel(private val onClickListener: View.OnClickListener) : EpoxyModel<AddMatchView>() {
    init {
        id(1)
    }
    override fun getDefaultLayout(): Int {
        return R.layout.match_model_add_new
    }

    override fun bind(addMatchView: AddMatchView?) {
        addMatchView?.setOnClickListener(onClickListener)
    }

    override fun unbind(addMatchView: AddMatchView?) {
        super.unbind(addMatchView)
        addMatchView?.setOnClickListener(null)
    }
}

class NoMatchFoundView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        init()
    }

    private fun init() {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.match_list_view_item_no_match_found, this)
    }

    fun clear() {

    }
}

class AddMatchView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        init()
    }

    private fun init() {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.match_list_view_item_add_new_match, this)
    }

    fun clear() {

    }
}
