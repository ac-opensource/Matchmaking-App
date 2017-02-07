package com.youniversals.playupgo.api

import com.youniversals.playupgo.data.AccessToken
import com.youniversals.playupgo.data.Match
import com.youniversals.playupgo.data.Sport
import com.youniversals.playupgo.data.UserMatch
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import rx.Observable

/**
 *
 *
 * YOYO HOLDINGS
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 14/12/2016
 */
interface RestApi {
    @GET("/auth/facebook/token")
    fun login(@Query("access_token") accessToken: String): Observable<AccessToken>

    @GET("/api/matches")
    fun nearMatches(@Query("filter[where][location][near]") location: String,
                    @Query("filter[where][location][maxDistance]") maxDistance: Int = 5): Observable<List<Match>>

    @POST("/api/matches")
    fun createMatch(@Body match: Match): Observable<Match>

    @GET("/api/sports")
    fun getSports(): Observable<List<Sport>>

    @POST("/api/userMatches")
    fun joinMatch(@Body userMatch: UserMatch): Observable<UserMatch>

    @GET("/api/userMatches")
    fun getUserMatch(@Query("[where][userId]") userId: Long,
                     @Query("[where][matchId]") matchId: Long): Observable<UserMatch>

    //{"where":{"matchId": 4}, "include":[{"relation": "user", "scope": {"include": [ "identities" ]}}] }
    //localhost:3000/api/UserMatches?filter={"where":{"matchId": 4}, "include":[{"relation": "user"}]}
    @GET("/api/userMatches")
    fun getUsersByMatchId(@Query("filter", encoded = true) filter: String): Observable<List<UserMatch>>
}