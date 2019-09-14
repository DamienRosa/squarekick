package com.dgr.squarekick.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dgr.squarekick.BuildConfig
import com.dgr.squarekick.data.db.dao.LeagueDao
import com.dgr.squarekick.data.db.entites.LeagueEntity

@Database(entities = [LeagueEntity::class], version = 1)
abstract class SquareKickDataBase : RoomDatabase() {

    abstract fun getLeagueDao() : LeagueDao

    companion object {
        @Volatile
        private var instance: SquareKickDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDataBase(context).also {
                instance = it
            }
        }

        private fun buildDataBase(context: Context): SquareKickDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                SquareKickDataBase::class.java,
                BuildConfig.DataBaseName
            ).build()
        }
    }
}