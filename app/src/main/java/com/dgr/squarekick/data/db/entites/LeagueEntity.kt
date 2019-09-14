package com.dgr.squarekick.data.db.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LeagueEntity(
    @PrimaryKey(autoGenerate = false)
    val league_id: Int,
    val name: String,
    val country: String,
    val season: Int,
    val season_start: String,
    val season_end: String,
    val image: String? = null,
    val standings: Int,
    val is_current: Int)