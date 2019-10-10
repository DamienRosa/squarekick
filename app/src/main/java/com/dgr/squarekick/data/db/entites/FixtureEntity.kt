package com.dgr.squarekick.data.db.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FixtureEntity(
    @PrimaryKey(autoGenerate = false)
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
    val goalsHomeTeam: Int,
    val goalsAwayTeam: Int
)