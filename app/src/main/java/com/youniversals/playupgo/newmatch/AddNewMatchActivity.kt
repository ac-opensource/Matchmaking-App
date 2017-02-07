package com.youniversals.playupgo.newmatch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.youniversals.playupgo.R
import kotlinx.android.synthetic.main.activity_add_new_match.*

class AddNewMatchActivity : AppCompatActivity() {

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent(context, AddNewMatchActivity::class.java)
            // Pass data object in the bundle and populate details activity.
//            intent.putExtra(MatchDetailsActivity.EXTRA_MATCH, match)
//            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, sharedMatchCardView as View, "match")
            context.startActivity(intent)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_match)

        stepperLayout.setAdapter(NewMatchStepperAdapter(supportFragmentManager, this))

    }




}
