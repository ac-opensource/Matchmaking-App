package com.youniversals.playupgo.newmatch.step


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Location
import com.youniversals.playupgo.flux.action.MatchActionCreator
import com.youniversals.playupgo.flux.store.MatchStore
import kotlinx.android.synthetic.main.fragment_set_location_step.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SetLocationStepFragment : Fragment(), Step {

    @Inject lateinit var matchActionCreator: MatchActionCreator
    @Inject lateinit var matchStore: MatchStore
    val PLACE_PICKER_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayUpApplication.fluxComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_set_location_step, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = createStaticMapUrl("14.6091,121.0223")
        Glide.with(this).load(url).fitCenter().centerCrop().into(staticMap)

        setLocationButton.setOnClickListener {
            val builder = PlacePicker.IntentBuilder().setLatLngBounds(LatLngBounds(LatLng(14.424057,120.848934), LatLng(14.7782442,121.1636333)))
            startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(context, data)
                val toastMsg = String.format("Place: %s", place.name)
                val url = createStaticMapUrl("${place.latLng.latitude},${place.latLng.longitude}", 18)
                Glide.with(this).load(url).fitCenter().centerCrop().into(staticMap)
                matchActionCreator.updateNewMatch(matchStore.newMatch!!.copy(location = Location(place.latLng.latitude, place.latLng.longitude), locationName = "${place.name}"))
                Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun createStaticMapUrl(location: String, zoom: Int = 11): String {
        return "https://maps.googleapis.com/maps/api/staticmap?center=$location&zoom=$zoom&size=600x400" +
                "&markers=color:red|$location"
    }

    override fun verifyStep(): VerificationError? {
        val newMatch = matchStore.newMatch ?: return VerificationError("No sport selected!")
        if (newMatch.location == null) return VerificationError("Pick a play location!")
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
