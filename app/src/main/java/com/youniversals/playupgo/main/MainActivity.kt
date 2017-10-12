package com.youniversals.playupgo.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.design.widget.BottomNavigationView.*
import android.support.design.widget.BottomSheetBehavior
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.analytics.FirebaseAnalytics
import com.pixplicity.easyprefs.library.Prefs
import com.tbruyelle.rxpermissions.RxPermissions
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Sport
import com.youniversals.playupgo.flux.BaseActivity
import com.youniversals.playupgo.flux.action.MatchActionCreator
import com.youniversals.playupgo.flux.action.UserActionCreator
import com.youniversals.playupgo.flux.store.MatchStore
import com.youniversals.playupgo.newmatch.AddNewMatchActivity
import com.youniversals.playupgo.newmatch.step.PickSportStepFragment
import com.youniversals.playupgo.notifications.NotificationActivity
import com.youniversals.playupgo.profile.ProfileActivity
import hotchemi.android.rate.AppRate
import kotlinx.android.synthetic.main.activity_main.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : BaseActivity(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, PickSportStepFragment.SportPickerListener {

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    @Inject lateinit var userActionCreator: UserActionCreator
    @Inject lateinit var matchActionCreator: MatchActionCreator
    @Inject lateinit var matchStore: MatchStore
    lateinit var preferredSportImage: String
    var preferredSportId: Long = 1

//    @Inject lateinit var googleMapsApi: GoogleApi
    private var matchPickerBottomSheetDialog: MatchPickerBottomSheetDialogFragment? = null

    private var mMap: GoogleMap? = null

    companion object {
        fun startActivity(context: Context) {
            val intent: Intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var  mGoogleApiClient: GoogleApiClient? = null

    private val PLACE_PICKER_REQUEST: Int = 1000
    private val REQUEST_SPORT: Int = 2000

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayUpApplication.fluxComponent.inject(this)
        setContentView(R.layout.activity_main)
        preferredSportImage = Prefs.getString("preferredSport.image", "https://maxcdn.icons8.com/Color/PNG/512/Sports/basketball-512.png")
        preferredSportId = Prefs.getLong("preferredSport.id", 1L)
        Glide.with(this).load(preferredSportImage).fitCenter().into(sportFilter)
        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(1) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .monitor()

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this)
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)

        // change the state of the bottom sheet
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        initFlux()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()

        mapFragment.getMapAsync(this)
        rippleBackground.visibility = GONE
        locationMarkerText.setOnClickListener {
            rippleBackground.visibility = VISIBLE
            locationMarkerText.visibility = INVISIBLE
            rippleBackground.startRippleAnimation()
            mMap?.uiSettings?.setAllGesturesEnabled(false)
            rippleBackground.postDelayed({
                val latLng = "${mMap?.cameraPosition?.target?.latitude},${mMap?.cameraPosition?.target?.longitude}"
                val bundle = Bundle()
                bundle.putString("coordinates", latLng)
                bundle.putString("address", locationAddressTextView.text?.toString())
                FirebaseAnalytics.getInstance(this).logEvent("search_nearby", bundle)
                matchActionCreator.getNearbyMatches(latLng, radiusSeekBar?.progress!!, preferredSportId)
            }, 3000)
        }

        locationAddressCard.setOnClickListener {
            val builder = PlacePicker.IntentBuilder().setLatLngBounds(LatLngBounds(LatLng(14.424057,120.848934), LatLng(14.7782442,121.1636333)))
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        }

        sportFilter.setOnClickListener {
            val dialogFragment = PickSportStepFragment()
//            dialogFragment.setTargetFragment(PickSportStepFragment(), REQUEST_SPORT)
            dialogFragment.show(supportFragmentManager, "PickSportStepFragment")
        }

        locationMarker.setOnClickListener { onMapReady(mMap!!) }
        radiusSeekBar.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
            override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
                radiusTextView.text = ("Search game(s) within: ${seekBar?.progress}km")
            }
        })

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_new_match -> {
                    AddNewMatchActivity.startActivity(this)
                }
                R.id.action_notifications -> {
                    NotificationActivity.startActivity(this)
                }
                R.id.action_profile -> {
                    ProfileActivity.startActivity(this, Prefs.getLong("externalId", -1).toString())
                }
                R.id.action_more -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
                else -> {

                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.action_notifications -> {
                NotificationActivity.startActivity(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initFlux() {
        matchStore.observable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mMap?.uiSettings?.setAllGesturesEnabled(true)
                    rippleBackground.stopRippleAnimation()
                    rippleBackground.visibility = GONE
                    locationMarkerText.visibility = VISIBLE
                    if (it.error != null) return@subscribe
                    when (it.action) {
                        MatchActionCreator.ACTION_GET_NEARBY_MATCHES_S -> {
                            matchPickerBottomSheetDialog = MatchPickerBottomSheetDialogFragment.newInstance()
                            matchPickerBottomSheetDialog?.show(supportFragmentManager, MatchPickerBottomSheetDialogFragment::class.java.simpleName)
                        }
                    }
                }, {
                    mMap?.uiSettings?.setAllGesturesEnabled(true)
                    rippleBackground.stopRippleAnimation()
                    rippleBackground.visibility = GONE
                    locationMarkerText.visibility = VISIBLE
                })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (isFinishing || isDestroyed) return
        addSubscriptionToUnsubscribe(
                RxPermissions(this).request(Manifest.permission.ACCESS_FINE_LOCATION)
                        .subscribe ({
                            if (it) { // Always true pre-M
                                val result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null)
                                result.setResultCallback { likelyPlaces ->
                                    var lastLikelyhood = 0
                                    var mostLikelyPlace = ""
                                    var mostLikelyCoordinate: LatLng = LatLng(14.6091, 121.0223)
                                    Observable.from(likelyPlaces)

                                    for (placeLikelihood in likelyPlaces) {
                                        Log.i("MainActivity", String.format("Place '%s' has likelihood: %g",
                                                placeLikelihood.place.name,
                                                placeLikelihood.likelihood))

                                        if (placeLikelihood.likelihood >= lastLikelyhood) {
                                            mostLikelyPlace = placeLikelihood.place.name.toString()
                                            mostLikelyCoordinate = placeLikelihood.place.latLng
                                        }
                                    }
                                    locationAddressTextView.text = mostLikelyPlace
                                    mMap?.setOnCameraIdleListener {}
                                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mostLikelyCoordinate, 16f))
                                    setUpMapCameraIdleListener()
                                    likelyPlaces.release()
                                }
                            } else {
                                // Oups permission denied
                                // Add a marker in Sydney and move the camera
                                val metroManila = LatLng(14.6091, 121.0223)
                                mMap?.setOnCameraIdleListener {}
                                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(metroManila, 14f))
                                setUpMapCameraIdleListener()
                            }
                        })
        )

        userActionCreator.preload()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(this, data)
                locationAddressTextView.text = place.name
                mMap?.setOnCameraIdleListener {}
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 16f))
                setUpMapCameraIdleListener()
            }
        }
    }

    private fun setUpMapCameraIdleListener() {
        val geocoder = Geocoder(this, Locale.getDefault())
        val targetObservable : PublishSubject<LatLng> = PublishSubject.create()
        targetObservable
                .debounce(100, TimeUnit.MILLISECONDS)
                .map { geocoder.getFromLocation(it.latitude, it.longitude, 1) }
                .subscribeOn(Schedulers.io())
                .filter { it != null }
                .filter { it.size > 0 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val addressLine = if (it[0].maxAddressLineIndex > 0) it[0].getAddressLine(0) + ", " else ""
                    val city = if (TextUtils.isEmpty(it[0].locality)) "" else it[0].locality + ", "
                    val state = if (TextUtils.isEmpty(it[0].adminArea)) "" else it[0].adminArea + ", "
                    val country = if (TextUtils.isEmpty(it[0].countryName)) "" else it[0].countryName
                    locationAddressTextView.text = "$addressLine$city$state$country"
                }, {})
        mMap?.setOnCameraMoveListener {
            val target: LatLng? = mMap?.cameraPosition?.target
            if (target != null) {
                targetObservable.onNext(target)
            }
        }
    }

    override fun onSportPicked(sport: Sport) {
        Prefs.putString("preferredSport.image", sport.icon)
        Prefs.getLong("preferredSport.id", sport.id)
        preferredSportImage = sport.icon
        preferredSportId = sport.id
        Glide.with(this).load(sport.icon).fitCenter().into(sportFilter)
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            return
        }
        super.onBackPressed()
    }
}
