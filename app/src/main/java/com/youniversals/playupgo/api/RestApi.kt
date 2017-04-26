package com.youniversals.playupgo.api

import com.youniversals.playupgo.data.*
import retrofit2.http.*
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
    @POST("/auth/facebook/token")
    fun login(@Query("access_token") accessToken: String): Observable<AccessToken>

    @GET("/api/matches")
    fun nearMatches(@Query("filter[where][location][near]") location: String,
                    @Query("filter[where][location][maxDistance]") maxDistance: Int = 5,
                    @Query("filter[where][location][unit]") unit: String = "kilometers",
                    @Query("filter[where][sportId]") sportId: Long = 1,
                    @Query("filter[where][date][gt]") date: Long = System.currentTimeMillis()): Observable<List<Match>>

    @POST("/api/matches")
    fun createMatch(@Body match: MatchJson): Observable<Match>

    @GET("/api/sports")
    fun getSports(): Observable<List<Sport>>

    @GET("/api/sports/{id}")
    fun getSport(@Path("id") id: Long): Observable<Sport>

    @POST("/api/userMatches")
    fun joinMatch(@Body userMatch: UserMatch): Observable<UserMatch>

    @PUT("/api/userMatches")
    fun acceptJoinMatch(@Body userMatch: UserMatch): Observable<UserMatch>

    @GET("/api/userMatches")
    fun getUserMatch(@Query("[where][userId]") userId: Long,
                     @Query("[where][matchId]") matchId: Long): Observable<UserMatch>

    @GET("/api/notifications")
    fun getNotifications(@Query("[where][userId]") userId: Long): Observable<List<Notification>>

    //{"where":{"matchId": 4}, "include":[{"relation": "user", "scope": {"include": [ "identities" ]}}] }
    //localhost:3000/api/UserMatches?filter={"where":{"matchId": 4}, "include":[{"relation": "user"}]}
    @GET("/api/userMatches")
    fun getUsersByMatchId(@Query("filter", encoded = true) filter: String): Observable<List<UserMatch>>

    @GET("/api/userIdentities/findByExternalId/{externalId}")
    fun getUserProfile(@Path("externalId") externalId: String): Observable<UserProfile>

    @GET("/api/matches/latest/{userId}")
    fun getMatches(@Path("userId") userId: Long): Observable<List<Match>>

}