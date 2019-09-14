package com.dgr.squarekick.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dgr.squarekick.data.db.entites.LeagueEntity

@Dao
interface LeagueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllLeagues(leagueList : List<LeagueEntity>)

    @Query("SELECT * FROM LeagueEntity")
    fun getLeaguesList(): LiveData<List<LeagueEntity>>
}