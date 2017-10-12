package com.youniversals.playupgo.newmatch.step


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
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
import kotlinx.android.synthetic.main.fragment_add_details_step.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AddDetailsStepFragment : Fragment(), Step {

    @Inject lateinit var matchActionCreator: MatchActionCreator
    @Inject lateinit var matchStore: MatchStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayUpApplication.fluxComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_add_details_step, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun verifyStep(): VerificationError? {
        val newMatch = matchStore.newMatch ?: return VerificationError("No sport selected!")

        val title = titleTextView.text
        val details = detailsTextView.text
        if (TextUtils.isEmpty(title)) return VerificationError("Please add a title")
        if (TextUtils.isEmpty(details)) return VerificationError("Please add details")

        matchActionCreator.updateNewMatch(newMatch.copy(title = title.toString(), description = details.toString()))

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
