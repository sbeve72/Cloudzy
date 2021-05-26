package com.dbtechprojects.cloudstatustest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dbtechprojects.cloudstatustest.model.AwsItem

@Database(entities = [AwsItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun cloudStatusDao(): CloudStatusDAO

    companion object {

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CacheDatabase? = null

        fun getDatabase(context: Context): CacheDatabase {

            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CacheDatabase::class.java,
                    "cloud_Status"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
