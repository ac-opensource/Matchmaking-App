package com.youniversals.playupgo.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.tbruyelle.rxpermissions.RxPermissions
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.flux.BaseActivity
import com.youniversals.playupgo.flux.action.MatchActionCreator
import com.youniversals.playupgo.flux.action.UserActionCreator
import com.youniversals.playupgo.flux.store.MatchStore
import kotlinx.android.synthetic.main.activity_main.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : BaseActivity(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    @Inject lateinit var userActionCreator: UserActionCreator
    @Inject lateinit var matchActionCreator: MatchActionCreator
    @Inject lateinit var matchStore: MatchStore
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayUpApplication.fluxComponent.inject(this)
        setContentView(R.layout.activity_main)

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
                matchActionCreator.getNearbyMatches(latLng, radiusSeekBar?.progress!!)
            }, 3000)
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
        if (isFinishing) return
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

        userActionCreator.preload()
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
}
