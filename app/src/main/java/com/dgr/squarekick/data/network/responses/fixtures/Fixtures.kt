package com.dgr.squarekick.data.network.responses.fixtures

import android.os.Parcel
import android.os.Parcelable

/*
Copyright (c) 2019 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class Fixtures(
    val fixture_id: Int,
    val league_id: Int,
    val event_date: String?,
    val event_timestamp: Int,
    val firstHalfStart: Int,
    val secondHalfStart: Int,
    val round: String?,
    val status: String?,
    val statusShort: String?,
    val elapsed: Int,
    val venue: String?,
    val referee: String?,
    val homeTeam: HomeTeam?,
    val awayTeam: AwayTeam?,
    val goalsHomeTeam: Int,
    val goalsAwayTeam: Int,
    val score: Score?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(HomeTeam::class.java.classLoader),
        parcel.readParcelable(AwayTeam::class.java.classLoader),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(Score::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(fixture_id)
        parcel.writeInt(league_id)
        parcel.writeString(event_date)
        parcel.writeInt(event_timestamp)
        parcel.writeInt(firstHalfStart)
        parcel.writeInt(secondHalfStart)
        parcel.writeString(round)
        parcel.writeString(status)
        parcel.writeString(statusShort)
        parcel.writeInt(elapsed)
        parcel.writeString(venue)
        parcel.writeString(referee)
        parcel.writeParcelable(homeTeam, flags)
        parcel.writeParcelable(awayTeam, flags)
        parcel.writeInt(goalsHomeTeam)
        parcel.writeInt(goalsAwayTeam)
        parcel.writeParcelable(score, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Fixtures> {
        override fun createFromParcel(parcel: Parcel): Fixtures {
            return Fixtures(parcel)
        }

        override fun newArray(size: Int): Array<Fixtures?> {
            return arrayOfNulls(size)
        }
    }
}