package com.dgr.squarekick.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dgr.squarekick.data.db.entites.FixtureEntity

@Dao
interface FixtureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllFixtures(fixturesList: List<FixtureEntity>)

    @Query("SELECT * FROM FixtureEntity WHERE fixture_id = :fid")
    fun getFixtureById(fid : Int) : LiveData<List<FixtureEntity>>

    @Query("SELECT * FROM FixtureEntity WHERE event_date = :date")
    fun getFixtureByDate(date: String): LiveData<List<FixtureEntity>>
}