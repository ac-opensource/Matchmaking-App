package com.youniversals.playupgo.main

import android.content.Context
import android.graphics.Color
import android.support.annotation.LayoutRes
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyViewHolder
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Match


/**
 *
 *
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
            addModels(NoMatchFoundModel(), AddMatchModel(onClickListener))
        }
    }

    override fun onBindViewHolder(holder: EpoxyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (selectedPosition === position) {
            // Here I am just highlighting the background
            holder.itemView.setBackgroundColor(Color.GREEN)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.itemView.setOnClickListener {
            // Updating old as well as new positions
            notifyItemChanged(selectedPosition)
            selectedPosition = position
            notifyItemChanged(selectedPosition)

            // Do your another stuff for your onClick
        }
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
