package com.youniversals.playupgo.newmatch


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.youniversals.playupgo.R

/**
 * A simple [Fragment] subclass.
 */
class AddDetailsStepFragment : Fragment(), Step {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_pick_sport_step, container, false)
    }

    override fun verifyStep(): VerificationError? {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null
    }

    override fun onSelected() {
        //update UI when selected
    }

    override fun onError(error: VerificationError) {
        //handle error inside of the fragment, e.g. show error on EditText
    }
}// Required empty public constructor
