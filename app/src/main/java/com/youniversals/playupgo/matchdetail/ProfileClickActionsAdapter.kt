package com.youniversals.playupgo.matchdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.youniversals.playupgo.R

class ProfileClickActionsAdapter(context: Context, actions: List<String>) : ArrayAdapter<String>(context, 0, actions) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val vh: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_profile_action, parent, false)
            vh = ViewHolder(convertView)
            convertView!!.tag = vh
        } else {
            vh = convertView.tag as ViewHolder
        }

        val option = getItem(position)
        vh.description.text = option
        return convertView
    }

    private class ViewHolder(v: View) {
        internal var description: TextView = v.findViewById(R.id.description) as TextView
    }
}