package com.youniversals.playupgo.newmatch

import android.content.Context
import android.graphics.Color
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.bumptech.glide.Glide
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Sport
import kotlinx.android.synthetic.main.sport_grid_view_item.view.*


/**
 * YOYO HOLDINGS
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 28/12/2016
 */

public class SportsAdapter(val onClickListener: View.OnClickListener) : EpoxyAdapter() {
    init {
        enableDiffing()
    }

    var selectedPosition: Int = 0
    var previousView: View? = null
    public fun addSports(sports: List<Sport>?) {
        sports?.forEach {
            addModel(SportModel(it, View.OnClickListener {
                previousView?.setBackgroundColor(Color.TRANSPARENT)
                it.setBackgroundColor(Color.GREEN)
                previousView = it
                onClickListener.onClick(it)
            }))
        }
    }
}

class SportModel(private val sport: Sport, private val onClickListener: View.OnClickListener) : EpoxyModel<SportView>() {

    init {
        id(sport.id)
    }

    @LayoutRes
    public override fun getDefaultLayout(): Int {
        return R.layout.sport_model
    }

    override fun bind(sportView: SportView?) {
        sportView?.data = sport
        sportView?.setBackgroundColor(Color.TRANSPARENT)
        sportView?.setOnClickListener(onClickListener)
    }

    override fun unbind(sportView: SportView?) {
        super.unbind(sportView)
        sportView?.setBackgroundColor(Color.TRANSPARENT)
        sportView?.setOnClickListener(null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        if (!super.equals(other)) return false

        other as SportModel

        if (sport != other.sport) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + sport.hashCode()
        return result
    }


}


public class SportView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var data: Sport? = null
        set(value) {
            field = value
            name = value?.name
            icon = value?.icon
        }

    var sportCard: View? = null

    var view: View? = null

    var icon: String? = null
        set(value) {
            field = value
            if (value != null) {
                Glide.with(context).load(value).into(sportIconImageView)
            } else {
                sportIconImageView.setImageDrawable(null)
            }
        }

    var name: String? = null
        set(value) {
            field = value
            sportNameTextView.text = value
        }

    init {
        init()
    }

    private fun init() {
        orientation = LinearLayout.VERTICAL
        view = View.inflate(context, R.layout.sport_grid_view_item, this)
        sportCard = sportCardView
    }

    fun clear() {}
}