package com.youniversals.playupgo.newmatch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.flux.BaseActivity
import com.youniversals.playupgo.flux.action.MatchActionCreator
import com.youniversals.playupgo.flux.store.MatchStore
import kotlinx.android.synthetic.main.activity_add_new_match.*
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class AddNewMatchActivity : BaseActivity() {

    @Inject lateinit var matchActionCreator: MatchActionCreator
    @Inject lateinit var matchStore: MatchStore

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent(context, AddNewMatchActivity::class.java)
            // Pass data object in the bundle and populate details activity.
//            intent.putExtra(MatchDetailsActivity.EXTRA_MATCH, match)
//            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, sharedMatchCardView as View, "match")
            context.startActivity(intent)
        }

    }

    private var completeButtonView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_match)
        PlayUpApplication.fluxComponent.inject(this)
        title = "Create New Match"
        initFlux()
        stepperLayout.setAdapter(NewMatchStepperAdapter(supportFragmentManager, this))
        stepperLayout.setListener(object : StepperLayout.StepperListener {
            override fun onStepSelected(newStepPosition: Int) {

            }

            override fun onReturn() {

            }

            override fun onCompleted(completeButton: View?) {
                completeButtonView = completeButton
                completeButtonView?.isEnabled = false
                matchActionCreator.createMatch(matchStore.newMatch!!)
            }

            override fun onError(verificationError: VerificationError?) {

            }
        })
    }

    private fun initFlux() {
        addSubscriptionToUnsubscribe(
                matchStore.observable()
                        .filter { it.action == MatchActionCreator.ACTION_CREATE_MATCH_S ||
                                it.action == MatchActionCreator.ACTION_CREATE_MATCH_F
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            if (it.error != null) {
                                completeButtonView?.isEnabled = true
                                return@subscribe
                            }
                            finish()
                        }
        )
    }


}
