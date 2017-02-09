package com.youniversals.playupgo.newmatch


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.flux.action.MatchActionCreator
import com.youniversals.playupgo.flux.store.MatchStore
import kotlinx.android.synthetic.main.fragment_set_date_and_time_step.*
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SetDateAndTimeStepFragment : Fragment(), Step {

    @Inject lateinit var matchActionCreator: MatchActionCreator
    @Inject lateinit var matchStore: MatchStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayUpApplication.fluxComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_set_date_and_time_step, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun verifyStep(): VerificationError? {
        val newMatch = matchStore.newMatch ?: return VerificationError("No sport selected!")
        val gameDate = Calendar.getInstance()
        gameDate.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
        gameDate.set(Calendar.MONTH, datePicker.month)
        gameDate.set(Calendar.YEAR, datePicker.year)

        var hour = 0
        var min = 0

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            hour = timePicker.hour
            min = timePicker.minute
        } else {
            hour = timePicker.currentHour
            min = timePicker.currentMinute
        }

        gameDate.set(Calendar.HOUR_OF_DAY, hour)
        gameDate.set(Calendar.MINUTE, min)

        matchActionCreator.updateNewMatch(newMatch.copy(date = gameDate.timeInMillis))

        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null
    }

    override fun onSelected() {
        //update UI when selected
    }

    override fun onError(error: VerificationError) {
        //handle error inside of the fragment, e.g. show error on EditText
        Toast.makeText(context, error.errorMessage, Toast.LENGTH_SHORT).show()

    }
}
