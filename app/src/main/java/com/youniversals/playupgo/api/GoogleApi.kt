package com.youniversals.playupgo.api

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 *
 *
 * YOYO HOLDINGS
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 27/12/2016
 */


interface GoogleApi {
//    http://maps.googleapis.com/maps/api/geocode/json?latlng=32,75&sensor=true&maxresults=1

    @GET("/geocode/json")
    fun reverseGeocode(@Query("latlng") latlng: String): Observable<GeocodeResults>

}

class GeocodeResults(val results: List<GeocodeResult>)
class GeocodeResult(@Json(name = "formatted_address") val formattedAddress: String)