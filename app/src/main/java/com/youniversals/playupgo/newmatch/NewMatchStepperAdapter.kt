package com.youniversals.playupgo.newmatch

import android.content.Context
import android.support.v4.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel


class NewMatchStepperAdapter(fm: FragmentManager, context: Context) : AbstractFragmentStepAdapter(fm, context) {

    override fun createStep(position: Int): Step {
        when (position) {
            0 -> return PickSportStepFragment()
            1 -> return AddDetailsStepFragment()
            2 -> return AddDetailsStepFragment()
            3 -> return AddDetailsStepFragment()
            else -> return AddDetailsStepFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getViewModel(position: Int): StepViewModel {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        var title = ""
        when (position) {
            0 -> title = "Pick a Sport"
            1 -> title = "Add Title and Details"
            2 -> title = "Set Date and Time"
            3 -> title = "Set a Location"
        }
        return StepViewModel.Builder(context)
                .setTitle(title) //can be a CharSequence instead
                .create()

    }
}