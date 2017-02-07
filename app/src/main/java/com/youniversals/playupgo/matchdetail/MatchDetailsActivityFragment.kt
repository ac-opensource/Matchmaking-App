package com.youniversals.playupgo.matchdetail

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.youniversals.playupgo.R

/**
 * A placeholder fragment containing a simple view.
 */
class MatchDetailsActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_match_details, container, false)
    }
}
