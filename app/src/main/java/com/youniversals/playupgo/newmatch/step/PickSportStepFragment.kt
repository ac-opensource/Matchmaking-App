package com.youniversals.playupgo.newmatch.step


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.MatchJson
import com.youniversals.playupgo.flux.action.MatchActionCreator
import com.youniversals.playupgo.flux.action.SportActionCreator
import com.youniversals.playupgo.flux.action.SportActionCreator.Companion.ACTION_GET_SPORTS_S
import com.youniversals.playupgo.flux.store.MatchStore
import com.youniversals.playupgo.flux.store.SportStore
import com.youniversals.playupgo.flux.store.UserStore
import com.youniversals.playupgo.newmatch.SportView
import com.youniversals.playupgo.newmatch.SportsAdapter
import kotlinx.android.synthetic.main.fragment_pick_sport_step.*
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class PickSportStepFragment : Fragment(), Step {

    @Inject lateinit var sportActionCreator: SportActionCreator
    @Inject lateinit var sportStore: SportStore
    @Inject lateinit var matchActionCreator: MatchActionCreator
    @Inject lateinit var matchStore: MatchStore
    @Inject lateinit var userStore: UserStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayUpApplication.fluxComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_pick_sport_step, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //update UI when selected
        val spanCount = 3
        val layoutManager = GridLayoutManager(context, spanCount)

        val sportsAdapter = SportsAdapter(View.OnClickListener { v ->
            if (v is SportView) {
                val data = v.data ?: return@OnClickListener
                Log.d("SPORT_DETAILS", v.data.toString())
//                val user = userStore.user() ?: return@OnClickListener
                val match = matchStore.newMatch ?: MatchJson(
//                        userId = user.userId,
                        userId = Prefs.getLong("userId", 0),
                        sportId = data.id,
                        location = null,
                        locationName = "",
                        description = "",
                        title = "",
                        date = 0,
                        status = "new"
                )

                matchActionCreator.updateNewMatch(match.copy(sportId = data.id))
            }
        })

        sportsAdapter.spanCount = spanCount
        layoutManager.spanSizeLookup = sportsAdapter.spanSizeLookup
        sportsRecyclerView?.layoutManager = layoutManager
        sportsRecyclerView?.adapter = sportsAdapter

        sportStore.observable()
                .filter { it.action == ACTION_GET_SPORTS_S }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.error != null) return@subscribe
                    sportsAdapter.addSports(it.sports)
                }, {

                })

        sportActionCreator.getSports()

    }

    override fun verifyStep(): VerificationError? {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        val newMatch = matchStore.newMatch ?: return VerificationError("No sport selected!")
        if (newMatch.sportId == 0L) return VerificationError("No sport selected!")
        return null
    }

    override fun onSelected() {

    }

    override fun onError(error: VerificationError) {
        //handle error inside of the fragment, e.g. show error on EditText
        Toast.makeText(context, error.errorMessage, Toast.LENGTH_SHORT).show()
    }
}

