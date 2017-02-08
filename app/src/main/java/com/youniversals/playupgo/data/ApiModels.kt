package com.youniversals.playupgo.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import java.util.*

/**
 * YOYO HOLDINGS

 * @author A-Ar Andrew Concepcion
 * *
 * @since 14/12/2016
 */

data class AccessToken(
        val id: String, // AccessToken
        val ttl: Long,
        val created: String,
        val userId: Long,
        @Json(name = "_user") val userIdentity: UserIdentity
)

data class User(
        val id: Long,
        val username: String,
        val email: String = ""
)

data class Name(
        val familyName: String,
        val givenName: String
)

data class UserIdentity(
        val id: Long,
        val provider: String,
        val profileUrl: String,
        val externalId: Long,
        val name: Name,
        val userId: Long
)

data class Sport(
        val id: Long,
        val name: String,
        val description: String,
        val icon: String
)

data class UserSport(
        val id: Long,
        val sportId: Long,
        val userId: Long
)

data class Location(
        val lat: Double,
        val lng: Double
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Location> = object : Parcelable.Creator<Location> {
            override fun createFromParcel(source: Parcel): Location = Location(source)
            override fun newArray(size: Int): Array<Location?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readDouble(), source.readDouble())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeDouble(lat)
        dest?.writeDouble(lng)
    }
}

data class Match(
        val id: Long = 0,
        val userId: Long = 0,
        val sportId: Long = 0,
        val location: Location? = null,
        val description: String = "",
        val title: String = "",
        val date: Long = 0,
        val status: String = ""
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Match> = object : Parcelable.Creator<Match> {
            override fun createFromParcel(source: Parcel): Match = Match(source)
            override fun newArray(size: Int): Array<Match?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readLong(), source.readLong(), source.readLong(), source.readParcelable<Location>(Location::class.java.classLoader), source.readString(), source.readString(), source.readLong(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeLong(id)
        dest?.writeLong(userId)
        dest?.writeLong(sportId)
        dest?.writeParcelable(location, flags)
        dest?.writeString(description)
        dest?.writeString(title)
        dest?.writeLong(date)
        dest?.writeString(status)
    }
}

data class MatchJson(
        val userId: Long,
        val sportId: Long,
        val location: Location?,
        val description: String,
        val title: String,
        val date: Long,
        val status: String
)

data class UserMatch(
        val matchId: Long,
        val userId: Long,
        val group: Long? = 1,
        val user: User? = null
)

data class Party(
        val id: Long,
        val name: String,
        val partyLeaderUserId: Long,
        val sportId: Long,
        val location: Location,
        val members: List<UserIdentity>
)

data class UserParty(
        val id: Long,
        val userId: Long,
        val partyId: Long
)

data class UserFriend(
        val id: Long,
        val userId: Long,
        val friendId: Long
)

data class Notification(
        val id: Long,
        val title: String,
        val message: String,
        val isRead: Boolean,
        val from: UserIdentity,
        val date: Long
)

data class UserNotification(
        val id: Long,
        val userId: Long,
        val notificationId: Long
)

data class GroupMessages(
        val id: Long,
        val partyId: Long,
        val matchId: Long,
        val messages: ArrayList<Message>
)

data class UserMessageThread(
        val id: Long,
        val messageThreadId: Long,
        val userId: Long
)

data class MessageThread(
        val id: Long,
        val handle: Long, // user1 x user2 in `asc` order or matchId
        val userIdentities: ArrayList<UserIdentity>
)

data class Message(
        val id: Long,
        val threadId: Long,
        val userId: Long,
        val message: String
)